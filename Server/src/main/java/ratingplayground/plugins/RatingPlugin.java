package ratingplayground.plugins;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import ratingplayground.dal.ActivityDao;
import ratingplayground.dal.UserDao;
import ratingplayground.logic.AppConstants;
import ratingplayground.logic.activities.ActivityEntity;
import ratingplayground.logic.exceptions.UserNotFoundException;
import ratingplayground.logic.users.UserEntity;
@Component
public class RatingPlugin implements PlaygroundPlugin {

	private ObjectMapper jackson;
	private ActivityDao activities;
	private UserDao users;
	
	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
	}
	
	@Autowired
	public void setActivities(ActivityDao activities, UserDao users) {
		this.activities = activities;
		this.users = users;
	}

	@Override
	public Object invokeOperation(ActivityEntity activity) {
		try {
			Optional<ActivityEntity> previousRating = activities.findByPlayerEmailEqualsAndPlayerPlaygroundEqualsAndElementIdEqualsAndElementPlaygroundEqualsAndTypeEquals(
					activity.getPlayerEmail(), activity.getPlayerPlayground(), activity.getElementId(), activity.getElementPlayground(), activity.getType());
			Rating rating = this.jackson.readValue( 
					activity.getJsonAttributes(),
					Rating.class);

			if(rating.getRating() > 10 || rating.getRating() < 0) {
				throw new RuntimeException("Invalid Rating!");
				//return new ReplyContent("Invalid Rating");
			}
			if (!previousRating.isPresent()) {
				// Add points to player
				List<ActivityEntity> allRatings = activities.findAllByElementIdEqualsAndElementPlaygroundEqualsAndTypeEquals(
						activity.getElementId(), activity.getElementPlayground(), AppConstants.TYPE_RATING); 
				float avg = calcRatingAvg(allRatings, rating);
				// Calculate points granted to user.
				int points = calcPoints(rating.getRating(), avg);
				
				//update user points
				UserEntity user = getUser(activity.getPlayerPlayground(), activity.getPlayerEmail());
				long oldPoints = user.getPoints();
				long newPoints = Math.max(0, oldPoints + points);
				
				user.setPoints(newPoints);
				saveUser(user);
				
				// Create response. Movie rating is saved at service
				ReplyContent r = new ReplyContent();
				r.addAttribute("Rating", rating.getRating());
				r.addAttribute("Average", avg);
				r.addAttribute("Points", points);
				r.addAttribute("TotalPoints", newPoints);
				return r;
			}else {
				throw new RuntimeException("You already rated this element!");
				//new ReplyContent("You already rated this movie");
			}
		} catch (Exception e) {
			if(e.getMessage() != null)
				throw new RuntimeException(e.getMessage());
			else
				throw new RuntimeException(e);
		}
	}

	private int calcPoints(int rating, float avg) {
		return 10 - (int)(Math.abs(rating - avg) * 5);
	}

	private float calcRatingAvg(List<ActivityEntity> allRatings, Rating rating) {
		float sum = 0;
		for (ActivityEntity activityEntity : allRatings) {
			Map<String, Object> content = (Map<String, Object>) activityEntity.getAttributes().get("content");
			
			sum += (int)content.get(AppConstants.TYPE_RATING);
		}
		sum += rating.getRating();
		float avg = sum/(allRatings.size()+1);
		return Float.parseFloat(String.format("%.1f", avg));
	}
	
	
	@Transactional(readOnly=true)
	public UserEntity getUser(String playground, String email) throws UserNotFoundException{
		
		String key = playground + AppConstants.DELIMITER + email;
		
		return this.users.findById(key).orElseThrow(()->
		new UserNotFoundException("User not found for key : " + key));
	}

	@Transactional(readOnly=false)
	public void saveUser(UserEntity user) {
		this.users.save(user);
	}
}