package ratingplayground.layout.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ratingplayground.logic.InputValidation;
import ratingplayground.logic.NumberGeneratorService;
import ratingplayground.logic.exceptions.InvalidConfirmationCodeException;
import ratingplayground.logic.exceptions.InvalidEmailException;
import ratingplayground.logic.exceptions.UserAlreadyExistException;
import ratingplayground.logic.exceptions.UserNotFoundException;
import ratingplayground.logic.users.UserEntity;
import ratingplayground.logic.users.UserService;


@CrossOrigin
@RestController
public class UserController {

	private UserService userService;
	
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	
//////////////////////[ POST ] ////////////////////////////////////////

@RequestMapping(
method=RequestMethod.POST,
path="/playground/users",
produces=MediaType.APPLICATION_JSON_VALUE,
consumes=MediaType.APPLICATION_JSON_VALUE)
public UserTO registerUser (@RequestBody NewUserForm newUserForm) throws UserAlreadyExistException{
	
	UserEntity newUser = newUserForm.toEntity();
	return new UserTO(this.userService.registerUser(newUser));
}


	
//////////////////////[ GET ] ////////////////////////////////////////

@RequestMapping(
		method=RequestMethod.GET,
		path="/playground/users/confirm/{playground}/{email}/{code}",
		produces=MediaType.APPLICATION_JSON_VALUE)
public UserTO verifyNewUser (@PathVariable("playground") String playground ,
		@PathVariable("email") String email ,
		@PathVariable("code") int code) throws UserNotFoundException, UserAlreadyExistException ,InvalidConfirmationCodeException , InvalidEmailException {
	
	InputValidation.validateUserConfirmationCode(code);
	InputValidation.validateEmailAddress(email);
	
	return new UserTO(this.userService.verifyNewUser(playground, email, code));	
}

@RequestMapping(
		method=RequestMethod.GET,
		path="/playground/users/login/{playground}/{email}",
		produces=MediaType.APPLICATION_JSON_VALUE)
public UserTO getUser (@PathVariable("playground") String playground ,
		@PathVariable("email") String email) throws UserNotFoundException , InvalidEmailException {
	
	InputValidation.validateEmailAddress(email);
	
	return new UserTO(this.userService.getUser(playground, email));
}


//TESTING ONLY
@RequestMapping(
		method=RequestMethod.GET,
		path="/playground/users/all",
		produces=MediaType.APPLICATION_JSON_VALUE)
public List<UserTO> getAllUsers () throws UserNotFoundException , InvalidEmailException {
	List<UserEntity> entities= this.userService.testGetAllUsers();
	List<UserTO> rv = new ArrayList<>();
	for (UserEntity userEntity : entities) {
		rv.add(new UserTO(userEntity));
	}
	return rv;
}
	
//////////////////////[ PUT ] ////////////////////////////////////////

	@RequestMapping(
			method=RequestMethod.PUT,
			path="/playground/users/{playground}/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateUser (
			@PathVariable("playground") String playground,
			@PathVariable("email") String email,
			@RequestBody UserTO userTO) throws UserNotFoundException , InvalidEmailException {
		
		InputValidation.validateEmailAddress(email);
			
		this.userService.updateUser(playground, email, userTO.toEntity());
	}
	
}
