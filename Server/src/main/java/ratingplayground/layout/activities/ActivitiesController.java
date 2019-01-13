package ratingplayground.layout.activities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ratingplayground.logic.InputValidation;
import ratingplayground.logic.activities.ActivityService;
import ratingplayground.logic.elements.ElementService;

@CrossOrigin
@RestController
public class ActivitiesController {
	private ActivityService activityService;
	
	@Autowired
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}
	
	
	////////////////////// [ POST ] ////////////////////////////////////////
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/activities/{userPlayground}/{email}",
			
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public Object performAction (@RequestBody ActivityTO activityTO,
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email) throws Exception {
		
		return new ActivityTO(this.activityService.startActivity(userPlayground, email, activityTO.toEntity()));
	}

}
