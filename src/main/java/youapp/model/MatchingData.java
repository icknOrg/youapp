package youapp.model;

import java.util.List;

public class MatchingData {
	
	private List<Reply> repliesA;
	
	private List<Reply> repliesB;
	
	private List<ReplyPair> commonReplies;
	
	
	private List<FacebookPerson> friendsA;
	
	private List<FacebookPerson> friendsB;
	
	private List<FacebookPerson> commonFriends;
	
	
	private List<FacebookGroup> groupsA;
	
	private List<FacebookGroup> groupsB;
	
	private List<FacebookGroup> commonGroups;
	
	
	private List<FacebookPage> pagesA;
	
	private List<FacebookPage> pagesB;
	
	private List<FacebookPage> commonPages;
	
	
	public List<Reply> getRepliesA() {
		return repliesA;
	}

	public void setRepliesA(List<Reply> repliesA) {
		this.repliesA = repliesA;
	}

	public int getNumberOfRepliesA() {
		if (repliesA == null) {
			return 0;
		}
		return repliesA.size();
	}
	
	public List<Reply> getRepliesB() {
		return repliesB;
	}

	public void setRepliesB(List<Reply> repliesB) {
		this.repliesB = repliesB;
	}

	public int getNumberOfRepliesB() {
		if (repliesB == null) {
			return 0;
		}
		return repliesB.size();
	}
	
	public int getNumberOfCommonReplies() {
		if (commonReplies == null) {
			return 0;
		}
		return this.commonReplies.size();
	}
	
	public long getUnionOfReplies() {
		return new Long(getNumberOfRepliesA() + getNumberOfRepliesB() - getNumberOfCommonReplies());
	}

	public List<ReplyPair> getCommonReplies() {
		return commonReplies;
	}

	public void setCommonReplies(List<ReplyPair> commonReplies) {
		this.commonReplies = commonReplies;
	}

	public List<FacebookPerson> getFriendsA() {
		return friendsA;
	}

	public void setFriendsA(List<FacebookPerson> friendsA) {
		this.friendsA = friendsA;
	}

	public int getNumberOfFriendsA() {
		if (friendsA == null) {
			return 0;
		}
		return friendsA.size();
	}

	public List<FacebookPerson> getFriendsB() {
		return friendsB;
	}

	public void setFriendsB(List<FacebookPerson> friendsB) {
		this.friendsB = friendsB;
	}

	public int getNumberOfFriendsB() {
		if (friendsB == null) {
			return 0;
		}
		return friendsB.size();
	}

	public List<FacebookPerson> getCommonFriends() {
		return commonFriends;
	}

	public void setCommonFriends(List<FacebookPerson> commonFriends) {
		this.commonFriends = commonFriends;
	}

	public int getNumberOfCommonFriends() {
		if (commonFriends == null) {
			return 0;
		}
		return commonFriends.size();
	}
	
	public long getUnionOfFriends() {
		return new Long(getNumberOfFriendsA() + getNumberOfFriendsB() - getNumberOfCommonFriends());
	}

	public List<FacebookGroup> getGroupsA() {
		return groupsA;
	}

	public void setGroupsA(List<FacebookGroup> groupsA) {
		this.groupsA = groupsA;
	}

	public int getNumberOfGroupsA() {
		if (groupsA == null) {
			return 0;
		}
		return groupsA.size();
	}

	public List<FacebookGroup> getGroupsB() {
		return groupsB;
	}

	public void setGroupsB(List<FacebookGroup> groupsB) {
		this.groupsB = groupsB;
	}

	public int getNumberOfGroupsB() {
		if (groupsB == null) {
			return 0;
		}
		return groupsB.size();
	}

	public List<FacebookGroup> getCommonGroups() {
		return commonGroups;
	}

	public void setCommonGroups(List<FacebookGroup> commonGroups) {
		this.commonGroups = commonGroups;
	}

	public int getNumberOfCommonGroups() {
		if (commonGroups == null) {
			return 0;
		}
		return commonGroups.size();
	}
	
	public long getUnionOfGroups() {
		return new Long(getNumberOfGroupsA() + getNumberOfGroupsB() - getNumberOfCommonGroups());
	}

	public List<FacebookPage> getPagesA() {
		return pagesA;
	}

	public void setPagesA(List<FacebookPage> pagesA) {
		this.pagesA = pagesA;
	}

	public int getNumberOfPagesA() {
		if (pagesA == null) {
			return 0;
		}
		return pagesA.size();
	}
	
	public List<FacebookPage> getPagesB() {
		return pagesB;
	}

	public void setPagesB(List<FacebookPage> pagesB) {
		this.pagesB = pagesB;
	}

	public int getNumberOfPagesB() {
		if (pagesB == null) {
			return 0;
		}
		return pagesB.size();
	}

	public List<FacebookPage> getCommonPages() {
		return commonPages;
	}

	public void setCommonPages(List<FacebookPage> commonPages) {
		this.commonPages = commonPages;
	}
	
	public int getNumberOfCommonPages() {
		if (commonPages == null) {
			return 0;
		}
		return commonPages.size();
	}

	public long getUnionOfPages() {
		return new Long(getNumberOfPagesA() + getNumberOfPagesB() - getNumberOfCommonPages());
	}
	
}
