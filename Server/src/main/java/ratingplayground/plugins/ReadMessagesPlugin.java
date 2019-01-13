package ratingplayground.plugins;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ratingplayground.dal.ActivityDao;
import ratingplayground.logic.AppConstants;
import ratingplayground.logic.activities.ActivityEntity;
@Component
public class ReadMessagesPlugin implements PlaygroundPlugin {

	private ObjectMapper jackson;
	private ActivityDao activities;
	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
	}
	
	@Autowired
	public void setActivities(ActivityDao activities) {
		this.activities = activities;
	}
	
	@Override
	public Object invokeOperation(ActivityEntity activity) {
		try {
			PageData data = this.jackson.readValue( 
					activity.getJsonAttributes(),
					PageData.class);
			
			List<ActivityEntity> messages = activities.findAllByElementIdEqualsAndTypeEquals(
					activity.getElementId(), AppConstants.TYPE_POST_MESSAGE, PageRequest.of(data.getPage(), data.getSize()));
			
			return new BoardMessages(messages);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
