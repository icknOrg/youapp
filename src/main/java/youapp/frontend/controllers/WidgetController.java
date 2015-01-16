package youapp.frontend.controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import youapp.model.Conversation;
import youapp.model.Person;
import youapp.services.MessagingService;
import youapp.services.PersonService;
import youapp.services.SoulmatesService;

@Controller
public class WidgetController
{
    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }

    private MessagingService messagingService;

    @Autowired
    public void setMessagingService(MessagingService messagingService)
    {
        this.messagingService = messagingService;
    }

    private SoulmatesService soulmatesService;

    @Autowired
    public void setSoulmatesService(SoulmatesService soulmatesService)
    {
        this.soulmatesService = soulmatesService;
    }

    /**
     * Get the ModelAndView Objects for the widget last conversations.
     * @param personId Own Person Id
     * @return ModelAndView Objects for widget last conversations
     * @throws Exception
     */
    public Map<String, Object> getLastConversationsObjects(Long personId) throws Exception
    {
        List<Conversation> allConversations = messagingService.getLastConversationsOfParticipant(personId);
        List<Conversation> firstConversations = new LinkedList<Conversation>();
        Map<Long, Person> conversationParticipantsMap = new HashMap<Long, Person>();
        Map<Long, Boolean> isConverversationParticiapantPublicMap = new HashMap<Long, Boolean>();

        // Create
        int i = 0;
        for (Iterator<Conversation> iterator = allConversations.iterator(); iterator.hasNext() && i <= 5;)
        {
            Conversation conversation = iterator.next();
            for (Long conversationParticipantId : conversation.getConversationMembers())
            {
                if (!conversationParticipantsMap.containsKey(conversationParticipantId))
                {
                    conversationParticipantsMap.put(conversationParticipantId, personService.getById(conversationParticipantId));
                    isConverversationParticiapantPublicMap.put(conversationParticipantId, !soulmatesService.areSoulmates(personId, conversationParticipantId));
                }
            }

            firstConversations.add(conversation);
            
            if (iterator.hasNext())
                i++;
        }

        Map<String, Object> mavObjects = new HashMap<String, Object>();
        mavObjects.put("widgetConversations", firstConversations);
        mavObjects.put("widgetConversationParticipantsMap", conversationParticipantsMap);
        mavObjects.put("widgetIsConversationParticipantPublicMap", isConverversationParticiapantPublicMap);
        mavObjects.put("widgetOwnPersonId", personId);
        return mavObjects;
    }

}
