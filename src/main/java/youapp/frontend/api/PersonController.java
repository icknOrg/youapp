package youapp.frontend.api;


import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import youapp.model.Person;
import youapp.model.Picture;
import youapp.model.TagSet;
import youapp.services.PersonService;

@Controller
@RequestMapping("/rest/persons")
public class PersonController {

	@Autowired
	private PersonService personService;

	@RequestMapping(value = "index", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getPerson(@RequestParam(value = "personId", required = true)
    Long personId) {
		try {
			Person p1 = personService.getById(personId);
		
			String res  = null;
			Gson gson = new Gson();
			p1.deletePictures();
			JsonElement jsonElement = gson.toJsonTree(p1);
			jsonElement.getAsJsonObject().addProperty("pictureURL", "/profile/picture.html?type=normal&personId="+p1.getId());
			res = gson.toJson(jsonElement);
			return res;
		} catch (Exception e) {
			return e.toString();
		}
	}
	
	@RequestMapping(value = "index", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Boolean updatePerson(@RequestBody String json) {
		try {
			Gson gson=  new GsonBuilder().setDateFormat("MMM dd, yy").create();
			Person person = gson.fromJson(json, Person.class);
			
			Person oldPerson = personService.getById(person.getId());
            for (Iterator<Picture> iterator = oldPerson.getStoredPictures(); iterator.hasNext();)
            {
                person.addPicture(iterator.next());
            }

            TagSet oldTags = oldPerson.getTags();
            TagSet newTags = person.getTags();
            oldTags.removeAll(newTags);
            person.removeTags(oldTags);
            personService.update(person);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@RequestMapping(value = "fbidexists", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Boolean fbIdExists(@RequestParam(value = "fbId", required = true)
    Long FbId) {
		try {
			return personService.exists(FbId, true);
		} catch (Exception e) {
			return false;
		}
	}
	
	@RequestMapping(value = "getid", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getPersonId(@RequestParam(value = "fbId", required = true)
    Long FbId) {
		try {
            Person person = personService.getByFbId(FbId);
            String res = null;
            Gson gson = new Gson();
			res = gson.toJson(person.getId());
            return res;
		} catch (Exception e) {
			return e.toString();
		}
	}
}

