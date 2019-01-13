package ratingplayground.plugins;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ratingplayground.logic.activities.ActivityEntity;
import ratingplayground.logic.users.UserService;
@Component
public class PostMessagePlugin implements PlaygroundPlugin {
	
	private UserService userService;
	private ObjectMapper jackson;
	
	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public Object invokeOperation(ActivityEntity activity) {
		try {
			Message message = this.jackson.readValue( 
					activity.getJsonAttributes(),
					Message.class);
			if(message == null || "".equals(message.getMessage())) {
				throw new RuntimeException("message cannot be empty!");
			}
			String username = userService.getUser(
					activity.getPlayerPlayground(), activity.getPlayerEmail()).getUsername();
			message.setUsername(username);
			return message;
			
		} catch (Exception e) {
			if(e.getMessage() != null)
				throw new RuntimeException(e.getMessage());
			else
				throw new RuntimeException(e);
		}
	}

}
