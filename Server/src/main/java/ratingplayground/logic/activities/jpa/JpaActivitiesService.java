package ratingplayground.logic.activities.jpa;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ratingplayground.aop.annotations.PlayerVerification;
import ratingplayground.dal.ActivityDao;
import ratingplayground.logic.activities.ActivityEntity;
import ratingplayground.logic.activities.ActivityService;
import ratingplayground.logic.exceptions.ActivityTypeInvalidException;
import ratingplayground.plugins.PlaygroundPlugin;

@Service
public class JpaActivitiesService implements ActivityService{

	private ActivityDao activities;
	private ActivityIdGeneratorDao activityIdGenerator;
	private ApplicationContext spring;
	private ObjectMapper jackson;

	
	@Autowired
	public JpaActivitiesService(ActivityDao activities, ActivityIdGeneratorDao activityIdGenerator, ApplicationContext spring) {
		this.activities = activities;
		this.activityIdGenerator = activityIdGenerator;
		this.spring = spring;
		jackson = new ObjectMapper();
	}
	
	@PlayerVerification
	@Override
	public ActivityEntity startActivity(String userPlayground, String email, ActivityEntity activity) throws ActivityTypeInvalidException{
		if(activity == null || activity.getPlayground() == null || activity.getId() != null) {
			throw new RuntimeException("startActivity - null values");
		}
		if(!userPlayground.equals(activity.getPlayerPlayground()) || !email.equals(activity.getPlayerEmail())) {
			throw new RuntimeException("startActivity - player mismatch");
		}
		
		if(activity.getType() != null) {
			try {
				String type = activity.getType();
				String className = "ratingplayground.plugins." + type + "Plugin";
				Class<?> theClass = Class.forName(className);
				
				PlaygroundPlugin plugin = (PlaygroundPlugin) this.spring.getBean(theClass);
				Object rv = plugin.invokeOperation(activity);
				Map<String, Object> rvMap = this.jackson.readValue(
						this.jackson.writeValueAsString(rv),
						Map.class);
				activity.getAttributes().putAll(rvMap);
				
				System.err.println(activity.getAttributes());
				
				
			} catch (Exception e) {
				if(e.getMessage() != null)
					throw new RuntimeException(e.getMessage());
				else
					throw new RuntimeException(e);
			}
		}
		ActivityIdGenerator tmp = this.activityIdGenerator.save(new ActivityIdGenerator()); 
		Long dummyId = tmp.getId();
		this.activityIdGenerator.delete(tmp);
		
		activity.setId("" + dummyId);
		
		return this.activities.save(activity);
	}

	@Override
	public ActivityEntity getActivityByIdentifiers(String elementId, String type, String playerEmail) {
		List<ActivityEntity> matching = activities.findByElementIdEqualsAndTypeEqualsAndPlayerEmailEquals(elementId, type, playerEmail);
		return matching.isEmpty() ? null : matching.get(0);
	}
	
	@Override
	public List<ActivityEntity> findAllByElementIdEqualsAndTypeEquals(String elementId, String type, Pageable p) {
		List<ActivityEntity> matching =  activities.findAllByElementIdEqualsAndTypeEquals(elementId, type, p);
		return matching.isEmpty() ? null : matching;
	}
	
	public void cleanup() {
		this.activities.deleteAll();
	}
}
