package youapp.jobs;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Group;
import com.restfb.types.Page;
import com.restfb.types.User;

import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.model.FacebookGroup;
import youapp.model.FacebookPage;
import youapp.model.FacebookPerson;
import youapp.services.FacebookGroupService;
import youapp.services.FacebookPageService;
import youapp.services.FacebookFriendshipService;
import youapp.services.PersonService;

public class FacebookSyncJob implements Runnable {
	
	/**
	 * Logger.
	 */
	private static final Log log = LogFactory.getLog(FacebookSyncJob.class);
	
	/**
	 * Access token for communicating with Facebook.
	 */
	private String accessToken;
	
	/**
	 * Provides access to the database.
	 */
	private PersonService personService;
	
	/**
	 * Provides access to the database.
	 */
	private FacebookFriendshipService facebookFriendshipService;
	
	/**
	 * Provides access to the database.
	 */
	private FacebookGroupService facebookGroupService;
	
	/**
	 * Provides access to the database.
	 */
	private FacebookPageService facebookPageService;
	
	/**
	 * Creates a new Facebook synchronization job which uses the given access token for accessing the necessary data on Facebook.
	 * @param accessToken the access token necessary for the Facebook communication.
	 */
	public FacebookSyncJob(String accessToken, PersonService personService, FacebookFriendshipService friendshipService, FacebookGroupService facebookGroupService, FacebookPageService facebookPageService) {
		if (accessToken == null) {
			throw new IllegalArgumentException("Access token must not be null.");
		}
		if (personService == null) {
			throw new IllegalArgumentException("Person service must not be null.");
		}
		if (friendshipService == null) {
			throw new IllegalArgumentException("Friendship service must not be null.");
		}
		if (facebookGroupService == null) {
			throw new IllegalArgumentException("Facebook group service must not be null.");
		}
		if (facebookPageService == null) {
			throw new IllegalArgumentException("Facebook page service must not be null.");
		}
		this.accessToken = accessToken;
		this.personService = personService;
		this.facebookFriendshipService = friendshipService;
		this.facebookGroupService = facebookGroupService;
		this.facebookPageService = facebookPageService;
	}

	@Override
	public void run() {
		if (log.isDebugEnabled()) {
			log.debug("Synchronizing with facebook user data.");
		}
		try {
			// Set up Facebook client.
			FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
			
			// Fetch basic user data.
			User user = facebookClient.fetchObject("me", User.class, Parameter.with("fields", "id, name, first_name, last_name, gender, birthday"));
			String fbUserId = user.getId();
			Long fbId = Long.parseLong(fbUserId);
			if (personService.exists(fbId, true) && personService.isActive(fbId, true)) {
				// Update existing Facebook data about the user.
				Long youappUserId = personService.getAlternativeId(fbId, true);
				
				// Get all of the user's friends from the database.
//				if (log.isDebugEnabled()) {
//					log.debug("Synchronizing user's friendships.");
//				}
				Map<Long, FacebookPerson> friendshipsDatabase = facebookFriendshipService.getAllFacebookFriendsAsMap(fbId, true);
//				if (log.isDebugEnabled()) {
//					log.debug("Found " + friendshipsDatabase.size() + " facebook friendship connections for person with facebook id " + fbId + ".");
//				}
				Connection<User> friends = facebookClient.fetchConnection("me/friends", User.class);
				List<User> friendshipsFacebook = friends.getData();
				Long friendFbId = null;
				// Check whether there are new friends.
				for (User u : friendshipsFacebook) {
					try {
						friendFbId = Long.parseLong(u.getId());
						if (!friendshipsDatabase.containsKey(friendFbId)) {
							// Create link between the new friend and the person.
//							if (log.isDebugEnabled()) {
//								log.debug("Creating friendship between person with facebook id " + fbId + " and person with id " + friendFbId + ".");
//							}
							facebookFriendshipService.createFacebookFriendship(fbId, friendFbId);
						}
						// Remove corresponding friendship from map so that the deleted Facebook friendships can be seen.
						friendshipsDatabase.remove(friendFbId);
					} catch (NumberFormatException e) {
//						if (log.isDebugEnabled()) {
//							log.debug("Exception occurred while parsing user id: " + u.getId() + ". Reason: " + e);
//							log.debug("Skipping person with facebook id: " + u.getId());
//						}
					} catch (Exception e) {
//						if (log.isDebugEnabled()) {
//							log.debug("Skipping friendship between person A with facebook id " + fbId + " and person B with facebook id " + friendFbId);
//							log.debug("Exception occurred while creating new friendship. Reason: " + e);
//							e.printStackTrace();
//						}
					}
				}
				// Remove friendships that are no longer existent.
				if (!friendshipsDatabase.isEmpty()) {
//					if (log.isDebugEnabled()) {
//						log.debug("Some Facebook friendships have been deleted. Deleting them from the database.");
//					}
					Collection<FacebookPerson> deletedFriendships = friendshipsDatabase.values();
					for (FacebookPerson friendId : deletedFriendships) {
						if (friendId != null && friendId.getFbId() != null) {
//							if (log.isDebugEnabled()) {
//								log.debug("Deleting friendship between person with id " + fbId + " and person with id " + friendId.getFbId() + ".");
//							}
							facebookFriendshipService.deleteFacebookFriendship(fbId, friendId.getFbId());
						} else {
//							if (log.isDebugEnabled()) {
//								log.debug("Facebook id of the friend of person with id " + fbId + " was not set. Skipping friendship.");
//							}
						}
					}
				}
				
				// Get all of the user's groups from the database.
//				if (log.isDebugEnabled()) {
//					log.debug("Synchronizing user's groups.");
//				}
				Map<Long, FacebookGroup> fbGroupsDatabase = facebookGroupService.getByPersonAsMap(youappUserId, false);
//				if (log.isDebugEnabled()) {
//					log.debug("Found " + fbGroupsDatabase.size() + " facebook group associations for person with facebook id " + fbId + ".");
//				}
				Connection<Group> groups = facebookClient.fetchConnection("me/groups", Group.class);
				List<Group> groupsFacebook = groups.getData();
				Long fbGroupId = null;
				FacebookGroup newGroup = null;
				for (Group g : groupsFacebook) {
//					if (log.isDebugEnabled()) {
//						log.debug("Group id: " + g.getId());
//						log.debug("Group name: " + g.getName());
//					}
					try {
						fbGroupId = Long.parseLong(g.getId());
						if (!fbGroupsDatabase.containsKey(fbGroupId)) {
							// Create group if not yet present and add association between the new group and the person.
//							if (log.isDebugEnabled()) {
//								log.debug("Creating association between person with id " + fbId + " and group with id " + fbGroupId + ".");
//							}
							newGroup = new FacebookGroup();
							newGroup.setgId(fbGroupId);
							newGroup.setName(g.getName());
							facebookGroupService.createAndAssociate(youappUserId, newGroup, false);
						}
						// Remove corresponding group from map so that the deleted Facebook groups can be seen.
						fbGroupsDatabase.remove(fbGroupId);
					} catch (NumberFormatException e) {
//						if (log.isDebugEnabled()) {
//							log.debug("Exception occurred while parsing group id: " + g.getId() + ". Reason: " + e);
//							log.debug("Skipping group with facebook id: " + g.getId());
//						}
					} catch (Exception e) {
//						if (log.isDebugEnabled()) {
//							log.debug("Exception occurred while associating new group with id: " + g.getId() + " and person with id: " + fbId + ". Reason: " + e);
//							log.debug("Skipping group with facebook id: " + g.getId());
//						}
					}
				}
				// Remove group associations that are no longer existent.
				if (!fbGroupsDatabase.isEmpty()) {
//					if (log.isDebugEnabled()) {
//						log.debug("Some Facebook group associations have been deleted. Deleting them from the database.");
//					}
					Collection<FacebookGroup> deletedGroups = fbGroupsDatabase.values();
					for (FacebookGroup facebookGroup : deletedGroups) {
//						if (log.isDebugEnabled()) {
//							log.debug("Deleting group association between person with id " + fbId + " and group with id " + facebookGroup.getgId() + ".");
//						}
						facebookGroupService.removeAssociation(youappUserId, facebookGroup.getId());
					}
				}
				
				// Get all of a user's likes from the database.
//				if (log.isDebugEnabled()) {
//					log.debug("Synchronizing user's pages.");
//				}
				Map<Long, FacebookPage> fbPagesDatabase = facebookPageService.getByPersonAsMap(youappUserId, true);
//				if (log.isDebugEnabled()) {
//					log.debug("Found " + fbPagesDatabase.size() + " facebook page associations for person with facebook id " + fbId + ".");
//				}
				//String query = "SELECT source_id, target_id, target_type, is_following FROM connection WHERE source_id=me() and target_type =\"page\"";
				String query = "SELECT uid, page_id, type, profile_section, created_time FROM page_fan WHERE uid=me()";
				List<FqlPageFan> fqlPageFan = facebookClient.executeQuery(query, FqlPageFan.class);
//				if (log.isDebugEnabled()) {
//					log.debug("Found " + fqlPageFan.size() + " facebook pages for person with facebook id " + fbId + " on facebook.");
//				}
				Long fbPageId = null;
				Long createdTime = null;
				FacebookPage newPage = null;
				for (FqlPageFan pageFan : fqlPageFan) {
					try {
						fbPageId = Long.parseLong(pageFan.page_id);
						createdTime = Long.parseLong(pageFan.created_time);
						if (!fbPagesDatabase.containsKey(fbPageId)) {
							// Create page if not yet present and add association between the new page and the person.
							if (!facebookPageService.exists(fbPageId, true)) {
								// Only get more information about the page from Facebook if that pages is not yet stored in the database.
								Page page = facebookClient.fetchObject(pageFan.page_id, Page.class);
//								log.info("Page name: " + page.getName());
//								log.info("Page category: " + page.getCategory());
//								log.info("Page type: " + pageFan.type);
//								log.info("Page profile section: " + pageFan.profile_section);
								newPage = new FacebookPage();
								newPage.setpId(fbPageId);
								newPage.setName(page.getName());
								newPage.setCategory(page.getCategory());
								newPage.setType(pageFan.type);
								facebookPageService.createAndAssociate(youappUserId, newPage, false, pageFan.profile_section, createdTime);
							} else {
								// The page exists already, adding association only.
								facebookPageService.addAssociation(youappUserId, fbPageId, true, false, pageFan.profile_section, createdTime);
							}
						}
						// Remove corresponding page from map so that the deleted Facebook pages can be seen.
						fbPagesDatabase.remove(fbPageId);
					} catch (NumberFormatException e) {
//						if (log.isDebugEnabled()) {
//							log.debug("Exception occurred while parsing page id: " + pageFan.page_id + ". Reason: " + e);
//							log.debug("Skipping page with facebook id: " + pageFan.page_id);
//						}
					} catch (Exception e) {
//						if (log.isDebugEnabled()) {
//							log.debug("Exception occurred while associating new page with id: " + pageFan.page_id + " and person with id: " + fbId + ". Reason: " + e);
//							log.debug("Skipping page with facebook id: " + pageFan.page_id);
//						}
					}
				}
				// Remove page associations that are no longer existent.
				if (!fbPagesDatabase.isEmpty()) {
//					if (log.isDebugEnabled()) {
//						log.debug("Some Facebook page associations have been deleted. Deleting them from the database.");
//					}
					Collection<FacebookPage> deletedPages = fbPagesDatabase.values();
					for (FacebookPage facebookPage : deletedPages) {
//						if (log.isDebugEnabled()) {
//							log.debug("Deleting page association between person with id " + fbId + " and page with id " + facebookPage.getpId() + ".");
//						}
						facebookPageService.removeAssociation(youappUserId, facebookPage.getId(), false);
					}
				}
			} else {
//				if (log.isDebugEnabled()) {
//					log.debug("Facebook data cannot be synchronized because user is not registered yet.");
//				}
			}
		} catch (NumberFormatException e) {
//			if (log.isErrorEnabled()) {
//				log.error(e);
//			}
		} catch (DataAccessLayerException e) {
//			if (log.isErrorEnabled()) {
//				log.error(e);
//			}
			e.printStackTrace();
		}
	}

}
