package youapp.frontend.api;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import youapp.model.Mood;
import youapp.model.StatusUpdate;
import youapp.services.MoodService;
import youapp.services.StatusUpdateService;

@Controller
@RequestMapping("/rest/statusupdates")
public class StatusController {
	
	@Autowired
	private StatusUpdateService statusUpdateService;
	
	@Autowired
	private MoodService moodService;
	
	@RequestMapping(value = "index", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getPersonStatus(@RequestParam(value = "personId", required = true)
    Long personId) {
		try {
			List<StatusUpdate> status = statusUpdateService.getByPersonAndSoulmates(personId, 0, 50);
			for(StatusUpdate s: status){
				s.getPerson().deletePictures();
			}
			String res  = null;
			Gson gson = new Gson();
			res = gson.toJson(status);
			return res;
		} catch (Exception e) {
			return e.toString();
		}
	}
	
	//Request URL: http://localhost:8080/YouApp/rest/statusupdates/index.html?moodRating=5
	/*Request Body:
	 * {"person":{"id":6,"fbId":1428377587,"accessLevel":{"id":2,"description":"User"},"firstName":"Livia","lastName":"Scapin","gender":"F","activated":true,"nickName":"Livi","description":"","memberSince":"Mai 3, 2014","lastOnline":"Mai 16, 2014","birthday":"Apr 6, 1991","useFBMatching":true,"useQuestionMatching":true,"useDistanceMatching":true,"useSymptomsMatching":true,"useMedicationMatching":true,"location":{"id":2659060,"name":"Rheinfelden, Aargau, Switzerland","longitude":7.794030000000000,"latitude":47.554370000000000},"tags":[{"id":19,"category":"ProfileTag","name":"blau"},{"id":18,"category":"Medication","name":"gelb"},{"id":20,"category":"Symptom","name":"rot"}],"tagsRemoved":[]},"when":"May 16, 2014 12:12:09 AM","description":""}
	 */
	@RequestMapping(value = "index", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String createStatus(@RequestBody String json, @RequestParam(value = "moodRating", required = true)
    Integer moodRating) {
		try {
			Gson gson=  new GsonBuilder().setDateFormat("MMM dd, yy").create();
			StatusUpdate statusUpdate = gson.fromJson(json, StatusUpdate.class);
			statusUpdate.setWhen(new Timestamp(System.currentTimeMillis()));
	        Mood mood = null;
	        if (moodRating != null)
	        {
	            mood = new Mood();
	            mood.setRating(moodRating);
	        }
	        statusUpdate.setMood(mood);
			statusUpdateService.create(statusUpdate);
			return "";
		} catch (Exception e) {
			return e.toString();
		}
	}
	
	
	//Request URL: YouApp/rest/statusupdates/index.html?personId=1&when=2012-09-20%2013:24:15
	@RequestMapping(value = "index", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Boolean deleteStatus(@RequestParam(value = "personId", required = true)
    Long personId, @RequestParam(value = "when", required = true)
    String ts) {
		try {
			Timestamp when = Timestamp.valueOf(ts);
	        if (statusUpdateService.exists(personId, when))
	        {
	            statusUpdateService.delete(personId, when);
	            return true;
	        }
	        return false;
		} catch (Exception e) {
			return false;
		}
	}
}
