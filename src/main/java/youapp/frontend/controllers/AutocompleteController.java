package youapp.frontend.controllers;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import youapp.exception.GenericException;
import youapp.frontend.interceptors.InterceptorUtils;
import youapp.frontend.model.JsonPerson;
import youapp.frontend.model.builder.JsonModelBuilder;
import youapp.model.Person;
import youapp.model.filter.NameFilter;
import youapp.model.filter.NameFilter.SearchType;
import youapp.model.filter.PersonBlockedFilter;
import youapp.model.filter.PersonBlockedFilter.BlockDirection;
import youapp.services.PersonService;

@Controller
@RequestMapping("/*")
public class AutocompleteController
    extends ExceptionHandlingController
{
    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(AutocompleteController.class);

    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }

    private JsonModelBuilder jsonModelBuilder;

    @Autowired
    public void setJsonModelBuilder(JsonModelBuilder jsonModelBuilder)
    {
        this.jsonModelBuilder = jsonModelBuilder;
    }

    @RequestMapping(value = "autocomplete/searchPeople", method = RequestMethod.GET)
    @ResponseBody
    public List<JsonPerson> searchPeople(@RequestParam(value = "name", required = true)
    String name, @RequestParam(value = "limit", required = false)
    Integer limit, HttpSession session) throws Exception
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

        List<Person> people = personService.getByFilters(new NameFilter(name, SearchType.CONTAINS), new PersonBlockedFilter(personId, BlockDirection.BOTHDIRECTIONS, true));
        List<JsonPerson> jsonPeople = new LinkedList<JsonPerson>();
        for (Person person : people)
        {
            jsonPeople.add(jsonModelBuilder.getJsonPerson(person, personId));
        }
        return jsonPeople;
    }

    @RequestMapping(value = "autocomplete/searchLocation", method = RequestMethod.GET)
    @ResponseBody
    public  JsonNode searchLocation(@RequestParam(value = "locationNameSearchTerm", required = true)
    String locationNameStartsWith) throws Exception
    {
        URL url = new URL("http://ws.geonames.org/searchJSON?username=youapp&featureClass=P&style=full&maxRows=12&name_startsWith=" + locationNameStartsWith);
        
        URLConnection urlConnection = url.openConnection();
        InputStream in = urlConnection.getInputStream();
        String encoding = urlConnection.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        String body = IOUtils.toString(in, encoding);
        in.close();  

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getJsonFactory();
        JsonParser jp = factory.createJsonParser(body);
        JsonNode actualObj = mapper.readTree(jp);
        return actualObj;
    }
}
