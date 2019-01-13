package ratingplayground.plugins;

import java.util.ArrayList;
import java.util.List;

import ratingplayground.layout.activities.ActivityTO;
import ratingplayground.logic.activities.ActivityEntity;

public class BoardMessages {
	List<ActivityTO> messages;
	
	public BoardMessages() {
	}
	
	public BoardMessages(List<ActivityEntity> messages) {
		super();
		this.messages = new ArrayList<>();
		for (ActivityEntity activityEntity : messages) {
			this.messages.add(new ActivityTO(activityEntity));
		}
		
	}

	public List<ActivityTO> getMessages() {
		return messages;
	}

	public void setMessages(List<ActivityTO> messages) {
		this.messages = messages;
	}
	
}
