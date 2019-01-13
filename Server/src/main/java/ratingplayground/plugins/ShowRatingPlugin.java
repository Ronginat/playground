package ratingplayground.plugins;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ratingplayground.dal.ActivityDao;
import ratingplayground.dal.UserDao;
import ratingplayground.logic.AppConstants;
import ratingplayground.logic.activities.ActivityEntity;
import ratingplayground.logic.exceptions.UserNotFoundException;
import ratingplayground.logic.users.UserEntity;

@Component
public class ShowRatingPlugin implements PlaygroundPlugin {
	
	private ActivityDao activities;
	private UserDao users;
	
	
	@Autowired
	public void setActivities(ActivityDao activities, UserDao users) {
		this.activities = activities;
		this.users = users;
	}

	@Override
	public Object invokeOperation(ActivityEntity activity) {
		try {
			Optional<ActivityEntity> userRating = activities.findByPlayerEmailEqualsAndPlayerPlaygroundEqualsAndElementIdEqualsAndElementPlaygroundEqualsAndTypeEquals(
					activity.getPlayerEmail(), activity.getPlayerPlayground(), activity.getElementId(), activity.getElementPlayground(), AppConstants.TYPE_RATING);

			System.err.println("isActivityExists = " + userRating.isPresent());
			if (userRating.isPresent()) {
				// Add points to player
				List<ActivityEntity> allRatings = activities.findAllByElementIdEqualsAndElementPlaygroundEqualsAndTypeEquals(
						activity.getElementId(), activity.getElementPlayground(), AppConstants.TYPE_RATING); 
				float avg = calcRatingAvg(allRatings);
				
				// Create response.
				ReplyContent r = new ReplyContent();
				r.addAttribute("Rating", getRatingFromActivity(userRating.get()));
				r.addAttribute("Average", avg);
				
				return r;
			}else {
				throw new RuntimeException("You must rate this element first!");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private int getRatingFromActivity(ActivityEntity activityEntity) {
		Map<String, Object> content = (Map<String, Object>) activityEntity.getAttributes().get("content");
		
		return (int)content.get(AppConstants.TYPE_RATING);
	}

	private float calcRatingAvg(List<ActivityEntity> allRatings) {
		float sum = 0;
		for (ActivityEntity activityEntity : allRatings) {
			Map<String, Object> content = (Map<String, Object>) activityEntity.getAttributes().get("content");
			
			sum += (int)content.get(AppConstants.TYPE_RATING);
		}
		float avg = sum/allRatings.size();
		return Float.parseFloat(String.format("%.1f", avg));
	}
	
	
	@Transactional(readOnly=true)
	public UserEntity getUser(String playground, String email) throws UserNotFoundException{
		
		String key = playground + AppConstants.DELIMITER + email;
		
		return this.users.findById(key).orElseThrow(()->
		new UserNotFoundException("User not found for key : " + key));
	}

	
}
