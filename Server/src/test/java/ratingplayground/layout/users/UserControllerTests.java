package ratingplayground.layout.users;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import ratingplayground.logic.AppConstants;
import ratingplayground.logic.exceptions.InvalidConfirmationCodeException;
import ratingplayground.logic.exceptions.UserAlreadyExistException;
import ratingplayground.logic.exceptions.UserNotFoundException;
import ratingplayground.logic.users.UserEntity;
import ratingplayground.logic.users.UserService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
	@LocalServerPort
	private int port;
	
	private String usersUrl;
	private RestTemplate restTemplate;
	private ObjectMapper jsonMapper;
	
	@Autowired
	private UserService userService;
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.usersUrl = "http://localhost:" + port + "/playground/users";
		this.jsonMapper = new ObjectMapper();
	}
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void teardown() {
		this.userService.cleanup();
	}

	@Test
	public void testServerIsBootingCorrectly() throws Exception {
		
	}
	
	
	//=================== [Feature: Get Specific User] ===================

	@Test
	public void testGetSpecificUserSuccessfully() throws Exception {
		
		//test URL : playground/users/login/{playground}/{email}
			
		String email = "ran@gmail.com";
		String playground = "ratingplayground";
		String role = "player";
		String avatar = "dummy";
		String username = "ran";
		
		//given database contains { "ratingplayground@@ran@gmail.com":{"email":"ran@gmail.com","playground":"ratingplayground" , "role":"player", "avatar":"dummy","username":"ran","points":0}}
		 
		UserEntity userEntity = new UserEntity(email, playground, username, avatar, role, 0);
		userEntity.setEmail(email);
		userEntity.setPlayground(playground);
		
		UserEntity rv = this.userService.registerUser(userEntity);
		this.userService.verifyNewUser(playground, email, rv.getVerificationCode());
		
		//when I invoke GET this.usersUrl + "/login/ratingplayground/ran@gmail.com"
		UserTO actualUser = this.restTemplate.getForObject(this.usersUrl + "/login/{playground}/{email}", UserTO.class,playground,email);
		
		assertThat(actualUser)
		.isNotNull()
		.extracting("email" , "playground")
		.containsExactly(email, playground);
	}
	
	@Test(expected=Exception.class)
	public void testGetSpecificUserWithInvalidParameters() throws Exception {
		//test URL : playground/users/login/{playground}/{email}
	
		String email = "ran@gmail.com";
		String playground = "ratingplayground";
		
		//given empty database
		
		//when I invoke GET this.usersUrl + "/login/ratingplayground/ran@gmail.com"
		this.restTemplate.getForObject(this.usersUrl + "/login/{playground}/{email}", UserTO.class,playground,email );
	}
	
	@Test(expected=Exception.class)
	public void testGetUnverifiedUser() throws Exception {
		//test URL : playground/users/login/{playground}/{email}
	
		String email = "ran@gmail.com";
		String playground = "ratingplayground";
		String username = "ran";
		String avatar = "dummy";
		String role = "player";
		
		UserEntity userEntity = new UserEntity(email, playground, username, avatar ,role, 0);
		
		//given user that not verified yet in the database
		this.userService.registerUser(userEntity);
		
		
		//when I invoke GET this.usersUrl + "/login/ratingplayground/ran@gmail.com"
		this.restTemplate.getForObject(this.usersUrl + "/login/{playground}/{email}", UserTO.class,playground,email );
	}
	
	
	//=================== [Feature: Register User] ===================
	
	@Test
	public void testRegisterUserSuccessfully()throws Exception {
	
		//test URL : playground/users
		
		String email = "ran@gmail.com";
		String playground = "ratingplayground";
		String username =  "ran";
		String role = "player";
		String avatar = "dummy";
		
		//when POST /playground/users with this NewUserForm
		
		NewUserForm newUserForm = new NewUserForm(email, username, avatar, role);
		
		//set the expected UserEntity that will be in the database after registration complete
		UserEntity expected = new UserEntity(email, playground, username, avatar, role, 0);
		expected.setVerificationCode(-1);
		
		//register the new user
		UserEntity userEntityRV = this.userService.registerUser(newUserForm.toEntity());
		
		//verify with the returned code - expecting verification code to be changed to -1
		this.userService.verifyNewUser(playground, email, userEntityRV.getVerificationCode());
		
		//retrieve user from database
		assertThat(this.userService.getUser(playground, email))
		.isNotNull()
		.usingComparator((u1,u2)-> {
			int rv = u1.getPlayground().compareTo(u2.getPlayground());
			if (rv == 0) {
				rv = u1.getEmail().compareTo(u2.getEmail());
				if (rv == 0) {
					rv = u1.getUsername().compareTo(u2.getUsername());
					if (rv == 0) {
						rv = u1.getVerificationCode() ^ u2.getVerificationCode();
					}
				}
			}
			return rv;
		})
		.isEqualTo(expected);
	}
	

	
	
	@Test(expected=Exception.class)
	public void testRegisterAlreadyExistingUser() throws Exception{
	
		//test URL : playground/users
	
		String playground = "ratingplayground";
		String email = "email";
		
		//given the following user in the database
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(email);
		userEntity.setPlayground(playground);
		this.userService.registerUser(userEntity);
		
		//when posting userTo with same parameters
		UserTO postUser = new UserTO();
		postUser.setEmail(email);
		postUser.setPlayground(playground);
		
		//expected to throw exception
		this.restTemplate.postForObject(this.usersUrl, postUser, UserTO.class);
	}
	
	
	//=================== [Feature: Update an existing User] ===================
	
	@Test
	public void testUpdateUserSuccessfully() throws Exception{
		
		//test URL : playground/users/{playground}/{email}
		
		String email = "ran@gmail.com";
		String playground = "ratingplayground";
		String expectetUpdatedUsername = "changedUsername";
		String expectedUpdatedAvatar = "changedAvatar";
		String expectedUpdatedRole = AppConstants.MANAGER;
		
		
		// given the database contains { "email":"ran@gmail.com", "playground":"ratingplayground", "username":"ran", "avatar":"avatar","role":"player" , "code" : -1}
		String entityJson = "{\"email\":\"ran@gmail.com\", \"playground\":\"ratingplayground\", \"username\":\"ran\", \"avatar\":\"avatar\" , \"role\":\"player\"}";
		UserEntity oldUser = this.jsonMapper.readValue(entityJson, UserEntity.class);
		
		//register user with verification code = -1
		this.userService.testRegisterUser(oldUser);		
		
		//when
		String userToString = "{\"email\":\"ran@gmail.com\", \"playground\":\"ratingplayground\", \"username\":\"changedUsername\", \"avatar\":\"changedAvatar\" , \"role\":\"manager\"}";

		UserTO to = this.jsonMapper.readValue(userToString, UserTO.class);
		this.restTemplate.put(this.usersUrl + "/{playground}/{email}", to ,playground,email );
		
		//then
		UserEntity actualEntity = this.userService.getUser(playground, email);
		assertThat(actualEntity)
		.isNotNull()
		.extracting("playground" , "email" , "username" , "avatar" , "role")
		.containsExactly(playground , email ,expectetUpdatedUsername, expectedUpdatedAvatar , expectedUpdatedRole );
	}
	
	
	@Test(expected=Exception.class)
	public void testUpdateWithNonExistingUser() throws Exception{
		
		//test URL : playground/users/{playground}/{email}
		String playground = "ratingplayground";
		String email = "nonexisting@gmail.com";
		
		//when 
		String userToString = "{\"email\":\"nonexisting@gmail.com\", \"playground\":\"ratingplayground\", \"username\":\"nonexisting\"}";
		UserTO to = this.jsonMapper.readValue(userToString, UserTO.class);
		
		this.restTemplate.put(this.usersUrl + "/{playground}/{email}", to, playground,email);

	}
	
	
	//=================== [Feature: Verify new User] ===================
	
	@Test()
	public void  verifyNewUserSuccessfully()throws Exception{ 
	
//		//test URL : playground/users/confirm/{playground}/{email}/{code}
		
		String email = "ran@gmail.com";
		String playground = "ratingplayground";

		//given database contains { "ratingplayground@@ran@gmail.com":{"email":"ran@gmail.com","playground":"ratingplayground"}}
		
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(email);
		userEntity.setPlayground(playground);
		
		UserEntity rv = this.userService.registerUser(userEntity);
		int generatedCode = rv.getVerificationCode();
			
		//when I invoke GET this.usersUrl + "/confirm/ratingplayground/{generatedCode}"
		UserTO actualUser = this.restTemplate.getForObject(this.usersUrl + "/confirm/{playground}/{email}/{code}", UserTO.class,playground,email,generatedCode);
		
		assertThat(actualUser)
		.isNotNull()
		.extracting("email" , "playground" , "code")
		.containsExactly(email, playground , -1);
	}
	
	@Test(expected=Exception.class)
	public void verifyNewUserWithWrongCode() throws Exception{ 
	
		//test URL : playground/users/confirm/{playground}/{email}/{code}
		String email = "ran@gmail.com";
		String playground = "ratingplayground";
		int wrongVerificationCode = -1;

		//given database contains { "ratingplayground@@ran@gmail.com":{"email":"ran@gmail.com","playground":"ratingplayground" ,"verificationCode": {[1000,9999]}
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(email);
		userEntity.setPlayground(playground);
		this.userService.registerUser(userEntity);
	
		//when I invoke GET this.usersUrl + "/confirm/ratingplayground/ran@gmail.com/-1"
		this.restTemplate.getForObject(this.usersUrl + "/confirm/{playground}/{email}/{code}", UserTO.class,playground,email,wrongVerificationCode);
}
	
	
	
}
