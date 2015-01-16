package youapp.frontend.controllers;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import youapp.exception.GenericException;
import youapp.frontend.interceptors.InterceptorUtils;
import youapp.frontend.model.JsonPerson;
import youapp.frontend.model.builder.JsonModelBuilder;
import youapp.model.Conversation;
import youapp.model.Message;
import youapp.model.Person;
import youapp.services.MessagingService;
import youapp.services.PersonBlockerService;
import youapp.services.PersonService;
import youapp.services.SoulmatesService;

@Controller
@RequestMapping(value = "/*")
public class MessagingController
    extends ExceptionHandlingController
{
    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(MessagingController.class);

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

    private PersonBlockerService personBlockerService;

    @Autowired
    public void setPersonBlockerService(PersonBlockerService personBlockerService)
    {
        this.personBlockerService = personBlockerService;
    }
    
    private JsonModelBuilder jsonModelBuilder;

    @Autowired
    public void setJsonModelBuilder(JsonModelBuilder jsonModelBuilder)
    {
        this.jsonModelBuilder = jsonModelBuilder;
    }

    @RequestMapping(value = "messaging/createMessage", method = RequestMethod.GET)
    public ModelAndView createMessageForm(@RequestParam(value = "conversationParticipants", required = false)
    Set<Long> conversationParticipants, @RequestParam(value = "text", required = false)
    String text, HttpSession session) throws Exception
    {
        ModelAndView mav = new ModelAndView("messaging/createMessage");

        if ((conversationParticipants != null) && conversationParticipants.size() > 0)
        {
            // Get person id.
            if (session == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Session is null.");
                }
                throw new GenericException("Session is null.");
            }
            Long personId = (Long) session.getAttribute("personId");
            if (personId == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Person id is null. Cleaning up session data.");
                }
                InterceptorUtils.clearSessionAttributesComplete(session);
                throw new GenericException("Person id is null.");
            }
            if (log.isDebugEnabled())
            {
                log.debug("Person id: " + personId);
            }

            
            List<JsonPerson> jsonPeople = new LinkedList<JsonPerson>();
            for (Long conversationParticipantId : conversationParticipants)
            {
                Person conversationParticipant = personService.getById(conversationParticipantId);
                jsonPeople.add(jsonModelBuilder.getJsonPerson(conversationParticipant, personId));
            }
            
            ObjectMapper jsonMapper = new ObjectMapper();
            mav.addObject("preFillConversationParticipants", jsonMapper.writeValueAsString(jsonPeople));
        }

        mav.addObject("text", text);
        return mav;
    }

    @RequestMapping(value = "messaging/createMessage", method = RequestMethod.POST)
    public ModelAndView createMessage(@RequestParam(value = "conversationParticipants", required = true)
    Set<Long> conversationParticipants, @RequestParam(value = "text", required = true)
    String text, HttpSession session) throws Exception
    {
        if ((conversationParticipants == null) || conversationParticipants.size() == 0)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Conversation Participants are null or emtpy.");
            }
            throw new GenericException("Conversation Participants have to be set.");
        }
        if (StringUtils.isBlank(text))
        {
            if (log.isDebugEnabled())
            {
                log.debug("Text is null or empty.");
            }
            throw new GenericException("Text has to be set.");
        }

        // Get person id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        Message message = new Message();
        message.setSenderId(personId);
        message.setText(text.trim());
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));

        messagingService.createMessage(message, conversationParticipants);

        return new ModelAndView("empty");
    }

    @RequestMapping(value = "messaging/createMessageByGroupId", method = RequestMethod.POST)
    public ModelAndView createMessage(@RequestParam(value = "conversationGroupId", required = true)
    Long conversationGroupId, @RequestParam(value = "text", required = true)
    String text, HttpSession session) throws Exception
    {
        if (conversationGroupId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Conversation Group Id is null.");
            }
            throw new GenericException("Conversation Group Id has to be set.");
        }
        if (StringUtils.isBlank(text))
        {
            if (log.isDebugEnabled())
            {
                log.debug("Text is null or empty.");
            }
            throw new GenericException("Message Text has to be set.");
        }

        // Get person id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        Message message = new Message();
        message.setSenderId(personId);
        message.setText(text.trim());
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));

        Long newMessageId = messagingService.createMessage(message, conversationGroupId);
        message.setMessageId(newMessageId);

        List<Message> messageList = new LinkedList<Message>();
        messageList.add(message);

        Conversation conversation = new Conversation();
        conversation.setConversationGroupId(conversationGroupId);
        conversation.setConversationMessages(messageList);

        Map<Long, Person> conversationParticipantsMap = new HashMap<Long, Person>();
        conversationParticipantsMap.put(personId, personService.getById(personId));

        ModelAndView mav = new ModelAndView("messaging/showMessagesList");
        mav.addObject("conversation", conversation);
        mav.addObject("conversationParticipantsMap", conversationParticipantsMap);
        mav.addObject("ownPersonId", personId);

        return mav;
    }

    @RequestMapping(value = "messaging/showMyConversations", method = RequestMethod.GET)
    public ModelAndView showMyConversations(HttpSession session) throws Exception
    {
        // Get person id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        List<Conversation> conversations = messagingService.getLastConversationsOfParticipant(personId);
        Map<Long, Person> conversationParticipantsMap = new HashMap<Long, Person>();
        Map<Long, Boolean> isConverversationParticiapantPublicMap = new HashMap<Long, Boolean>();

        for (Conversation conversation : conversations)
        {
            for (Long conversationParticipantId : conversation.getConversationMembers())
            {
                if (!conversationParticipantsMap.containsKey(conversationParticipantId))
                {
                    conversationParticipantsMap.put(conversationParticipantId,
                        personService.getById(conversationParticipantId));
                    isConverversationParticiapantPublicMap.put(conversationParticipantId,
                        !soulmatesService.areSoulmates(personId, conversationParticipantId));
                }
            }
        }

        ModelAndView mav = new ModelAndView("messaging/showMyConversations");
        mav.addObject("conversations", conversations);
        mav.addObject("conversationParticipantsMap", conversationParticipantsMap);
        mav.addObject("isConversationParticipantPublicMap", isConverversationParticiapantPublicMap);
        mav.addObject("ownPersonId", personId);
        return mav;
    }

    @RequestMapping(value = "messaging/showConversation", method = RequestMethod.GET)
    public ModelAndView showConversation(@RequestParam(value = "conversationGroupId", required = true)
    Long conversationGroupId, HttpSession session) throws Exception
    {
        // Get person id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        Timestamp newestMessageTimestamp = null;
        Integer offset = 0;
        Integer resultSize = 10;

        Conversation conversation =
            messagingService.getConversationById(conversationGroupId, personId, offset, resultSize);

        if ((conversation.getConversationMessages() != null) && conversation.getConversationMessages().size() > 0)
        {
            newestMessageTimestamp = conversation.getConversationMessages().get(0).getTimestamp();
            // Reverse order of messages, so the order will be ASC of timestamp
            Collections.reverse(conversation.getConversationMessages());
        }

        Map<Long, Person> conversationParticipantsMap = new HashMap<Long, Person>();
        Map<Long, Boolean> isConverversationParticiapantPublicMap = new HashMap<Long, Boolean>();
        Map<Long, Boolean> isConverversationParticiapantBlockedMap = new HashMap<Long, Boolean>();
        for (Long conversationParticipantId : conversation.getConversationMembers())
        {
            conversationParticipantsMap
                .put(conversationParticipantId, personService.getById(conversationParticipantId));
            isConverversationParticiapantPublicMap.put(conversationParticipantId,
                !soulmatesService.areSoulmates(personId, conversationParticipantId));
            isConverversationParticiapantBlockedMap.put(conversationParticipantId,
                personBlockerService.blockedAnyDirection(personId, conversationParticipantId));
        }

        ModelAndView mav = new ModelAndView("messaging/showConversation");
        mav.addObject("messagesOffset", offset + resultSize);
        mav.addObject("messagesResultSize", resultSize);
        mav.addObject("newestMessageTimestamp", newestMessageTimestamp);
        mav.addObject("conversation", conversation);
        mav.addObject("conversationParticipantsMap", conversationParticipantsMap);
        mav.addObject("isConversationParticipantPublicMap", isConverversationParticiapantPublicMap);
        mav.addObject("isConversationParticipantBlockedMap", isConverversationParticiapantBlockedMap);
        mav.addObject("ownPersonId", personId);

        return mav;
    }

    @RequestMapping(value = "messaging/showMessagesList", method = RequestMethod.GET)
    public ModelAndView showMessagesList(@RequestParam(value = "conversationGroupId", required = true)
    Long conversationGroupId, @RequestParam(value = "startDate", required = true)
    Timestamp startDate, @RequestParam(value = "offset", required = true)
    int offset, @RequestParam(value = "resultSize", required = true)
    int resultSize, HttpSession session) throws Exception
    {
        // Get person id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        Conversation conversation =
            messagingService.getConversationById(conversationGroupId, personId, startDate, offset, resultSize);

        // Reverse order of messages, so the order will be ASC of timestamp
        Collections.reverse(conversation.getConversationMessages());

        Map<Long, Person> conversationParticipantsMap = new HashMap<Long, Person>();
        Map<Long, Boolean> isConverversationParticiapantPublicMap = new HashMap<Long, Boolean>();
        Map<Long, Boolean> isConverversationParticiapantBlockedMap = new HashMap<Long, Boolean>();
        for (Long conversationParticipantId : conversation.getConversationMembers())
        {
            conversationParticipantsMap
                .put(conversationParticipantId, personService.getById(conversationParticipantId));
            isConverversationParticiapantPublicMap.put(conversationParticipantId,
                !soulmatesService.areSoulmates(personId, conversationParticipantId));
            isConverversationParticiapantBlockedMap.put(conversationParticipantId,
                personBlockerService.blockedAnyDirection(personId, conversationParticipantId));
        }

        ModelAndView mav = new ModelAndView("messaging/showMessagesList");
        mav.addObject("conversation", conversation);
        mav.addObject("conversationParticipantsMap", conversationParticipantsMap);
        mav.addObject("isConversationParticipantPublicMap", isConverversationParticiapantPublicMap);
        mav.addObject("isConversationParticipantBlockedMap", isConverversationParticiapantBlockedMap);
        mav.addObject("ownPersonId", personId);

        return mav;
    }

    @RequestMapping(value = "messaging/clearConversation", method = RequestMethod.POST)
    @ResponseBody
    public String clearConversation(@RequestParam(value = "conversationGroupId", required = true)
    Long conversationGroupId, HttpSession session) throws Exception
    {
        // Get person id.
        if (session == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Session is null.");
            }
            throw new GenericException("Session is null.");
        }
        Long personId = (Long) session.getAttribute("personId");
        if (personId == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person id is null. Cleaning up session data.");
            }
            InterceptorUtils.clearSessionAttributesComplete(session);
            throw new GenericException("Person id is null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Person id: " + personId);
        }

        messagingService.clearConversationForParticipant(personId, conversationGroupId);

        return "";
    }
}
