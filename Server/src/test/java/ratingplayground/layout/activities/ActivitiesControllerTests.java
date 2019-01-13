package ratingplayground.layout.activities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import ratingplayground.logic.AppConstants;
import ratingplayground.logic.activities.ActivityEntity;
import ratingplayground.logic.activities.ActivityService;
import ratingplayground.logic.elements.ElementEntity;
import ratingplayground.logic.elements.ElementService;
import ratingplayground.logic.users.UserEntity;
import ratingplayground.logic.users.UserService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ActivitiesControllerTests {
	@LocalServerPort
	private int port;
	private String url;
	private RestTemplate restTemplate;
	
	private final String playground = "ratingplayground";
	private final String emailManager = "managerEmail@gmail.com";
	private final String emailPlayer = "playerEmail@gmail.com";
	
	@Autowired
	private ElementService elementService;
	@Autowired
	private UserService userService;
	@Autowired
	private ActivityService activityService;
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/playground/activities/";
	}
	
	@Before
	public void setup() {
		// register a new verified manager
		UserEntity user = new UserEntity(emailManager, playground, "manager1", "", AppConstants.MANAGER, 0);
		
		userService.testRegisterUser(user);
		
		// register a new verified player
		user = new UserEntity(emailPlayer, playground, "player1", "", AppConstants.PLAYER, 0);
		
		userService.testRegisterUser(user);
		
		// Add a message board
		ElementEntity messageBoard = new ElementEntity(playground, null, 0.0, 0.0, 
				"Message Board", new Date(),null, "Message Board", null, playground, emailManager);
		this.elementService.addNewElement(playground, emailManager, messageBoard);
		
		// Add a movie
		ElementEntity movie = new ElementEntity(playground, null, 0.0, 0.0, "Iron Man", new Date(),
				null, "Movie", null, playground, emailManager);
		this.elementService.addNewElement(playground, emailManager,	movie);
	}

	@After
	public void teardown() {
		this.elementService.cleanup();
		this.userService.cleanup();
		this.activityService.cleanup();
	}
	
	//============================================Feature: Start activity=======================================
	@Test
	public void testPostMessageSuccessfully() throws Exception{
		// given a user with manager role
		UserEntity player = getUserForTesting(playground, emailPlayer);
					
		// given database is empty
		String elementId = "1";
		String type = "PostMessage";
		String message = "Hello Integrative World";
			
		// when - POST
		ActivityTO postActivity = new ActivityTO
				(playground, null, playground, elementId, type, playground, emailPlayer);
		postActivity.setAttributes(new HashMap<>());
		postActivity.getAttributes().put("message", message);
						
		ActivityTO actualReturnedValue = this.restTemplate.postForObject
				(getUrlWithUserParams(player), postActivity, ActivityTO.class);
		assertThat(actualReturnedValue)
				.isNotNull()
				.extracting("playground", "type")
				.containsExactly(playground, type);
						
		//save the generated id
		String id = actualReturnedValue.getId();
		postActivity.setId(id);
					
		ActivityEntity expectedOutcome = postActivity.toEntity();
						
		// then
		assertThat(this.activityService.getActivityByIdentifiers(elementId, type, emailPlayer))
			.isNotNull()
			.usingComparator((a1,a2)->{
				int rv = (a1.getPlayground() + a1.getId()).compareTo(a2.getPlayground() + a2.getId());
				if (rv == 0) {
					rv = (a1.getType()).compareTo(a2.getType());
					if (rv == 0) {
						rv = (a1.getElementId()).compareTo(a2.getElementId());
					}
				}
				return rv;
			})
			.isEqualTo(expectedOutcome);
	}
	@Test
	public void testReadMessagesSuccessfully() throws Exception{
		// given a user with manager role
		UserEntity player = getUserForTesting(playground, emailPlayer);
					
		// given database is empty
		String elementId = "1";
		String pType = "PostMessage";
		String rType = "ReadMessages";
		String message = "Hello Integrative World";
		int page = 0;
		int size = 3;
						
		// when - POST
		ActivityTO postActivity = new ActivityTO
				(playground, null, playground, elementId, pType, playground, emailPlayer);
		postActivity.setAttributes(new HashMap<>());
		postActivity.getAttributes().put("message", message);
						
		ActivityTO postReturnedValue = this.restTemplate.postForObject
				(getUrlWithUserParams(player), postActivity, ActivityTO.class);
		assertThat(postReturnedValue)
				.isNotNull()
				.extracting("playground", "type")
				.containsExactly(playground, pType);
		
		ActivityTO readActivity = new ActivityTO
				(playground, null, playground, elementId, rType, playground, emailPlayer);
		readActivity.setAttributes(new HashMap<>());
		readActivity.getAttributes().put("page", page);
		readActivity.getAttributes().put("size", size);
						
		ActivityTO readReturnedValue = this.restTemplate.postForObject
				(getUrlWithUserParams(player), readActivity, ActivityTO.class);
		assertThat(readReturnedValue)
				.isNotNull()
				.extracting("playground", "type")
				.containsExactly(playground, rType);
						
		// then
		assertThat(this.activityService.findAllByElementIdEqualsAndTypeEquals(
				elementId, "PostMessage", PageRequest.of(page, size)))
			.isNotNull();
	}
	@Test
	public void testReadMessagesWithNoMessagesPosted() throws Exception{
		// given a user with manager role
		UserEntity player = getUserForTesting(playground, emailPlayer);
					
		// given database is empty
		String elementId = "1";
		String type = "ReadMessages";
		String playerEmail = emailPlayer;
		int page = 0;
		int size = 3;
						
		// when - POST
		ActivityTO postActivity = new ActivityTO
				(playground, null, playground, elementId, type, playground, playerEmail);
		postActivity.setAttributes(new HashMap<>());
		postActivity.getAttributes().put("page", page);
		postActivity.getAttributes().put("size", size);
						
		ActivityTO actualReturnedValue = this.restTemplate.postForObject
				(getUrlWithUserParams(player), postActivity, ActivityTO.class);
		assertThat(actualReturnedValue)
				.isNotNull()
				.extracting("playground", "type")
				.containsExactly(playground, type);
						
		//save the generated id
		String id = actualReturnedValue.getId();
		postActivity.setId(id);
						
		// then
		assertThat(this.activityService.findAllByElementIdEqualsAndTypeEquals(
				elementId, "PostMessage", PageRequest.of(page, size)))
			.isNull();
	}
	
	@Test
	public void testRateMovieSuccessfully() throws Exception{
		// given a user with manager role
		UserEntity player = getUserForTesting(playground, emailPlayer);
					
		// given database is empty
		String elementId = "1";
		String type = "Rating";
		String playerEmail = emailPlayer;
		float rating = 7.0f;
						
		// when - POST
		ActivityTO postActivity = new ActivityTO
				(playground, null, playground, elementId, type, playground, playerEmail);
		postActivity.setAttributes(new HashMap<>());
		postActivity.getAttributes().put("rating", rating);
						
		ActivityTO actualReturnedValue = this.restTemplate.postForObject
				(getUrlWithUserParams(player), postActivity, ActivityTO.class);
		assertThat(actualReturnedValue)
				.isNotNull()
				.extracting("playground", "type")
				.containsExactly(playground, type);
						
		//save the generated id
		String id = actualReturnedValue.getId();
		postActivity.setId(id);
					
		ActivityEntity expectedOutcome = postActivity.toEntity();
						
		// then
		assertThat(this.activityService.getActivityByIdentifiers(elementId, type, playerEmail))
			.isNotNull()
			.usingComparator((a1,a2)->{
				int rv = (a1.getPlayground() + a1.getId()).compareTo(a2.getPlayground() + a2.getId());
				if (rv == 0) {
					rv = (a1.getType()).compareTo(a2.getType());
					if (rv == 0) {
						rv = (a1.getElementId()).compareTo(a2.getElementId());
					}
				}
				return rv;
			})
			.isEqualTo(expectedOutcome);
	}
	
	@Test(expected=Exception.class)
	public void testRateSameMovieTwice() throws Exception{
		// given a user with manager role
		UserEntity player = getUserForTesting(playground, emailPlayer);
					
		// given database is empty
		String elementId = "1";
		String type = "Rating";
		String playerEmail = emailPlayer;
		float rating = 7.0f;
						
		// when - POST
		ActivityTO postActivity = new ActivityTO
				(playground, null, playground, elementId, type, playground, playerEmail);
		postActivity.setAttributes(new HashMap<>());
		postActivity.getAttributes().put("rating", rating);
						
		this.restTemplate.postForObject
				(getUrlWithUserParams(player), postActivity, ActivityTO.class);
		
		ActivityTO actualReturnedValue = this.restTemplate.postForObject
				(getUrlWithUserParams(player), postActivity, ActivityTO.class);
		
	}
	
	@Test(expected=Exception.class)
	public void testStartActivityWithInvalidType() throws Exception{
		// given a user with manager role
		UserEntity player = getUserForTesting(playground, emailPlayer);
							
		// given database is empty
		String elementId = "1";
		String type = "PostMessage";
		String playerEmail = emailPlayer;
		String rating = "Hello Integrative World";
						
		// when - POST
		ActivityTO postActivity = new ActivityTO
				(playground, null, playground, elementId, type, playground, playerEmail);
		postActivity.setAttributes(new HashMap<>());
		postActivity.getAttributes().put("rating", rating);
							
		this.restTemplate.postForObject(getUrlWithUserParams(player), postActivity, ActivityTO.class);
	}
	
	@Test(expected=Exception.class)
	public void testRateWithStringRating() throws Exception{
		// given a user with manager role
		UserEntity player = getUserForTesting(playground, emailPlayer);
							
		// given database is empty
		String elementId = "1";
		String type = "Rating";
		String playerEmail = emailPlayer;
		String message = "Hello Integrative World";
						
		// when - POST
		ActivityTO postActivity = new ActivityTO
				(playground, null, playground, elementId, type, playground, playerEmail);
		postActivity.setAttributes(new HashMap<>());
		postActivity.getAttributes().put("message", message);
							
		this.restTemplate.postForObject(getUrlWithUserParams(player), postActivity, ActivityTO.class);
	}
	
	private UserEntity getUserForTesting(String playground, String email) {
		return this.userService.getUser(playground, email);		
	}
	
	private String getUrlWithUserParams(UserEntity user) {
		return this.url + user.getPlayground() + "/" + user.getEmail();
	}
}
