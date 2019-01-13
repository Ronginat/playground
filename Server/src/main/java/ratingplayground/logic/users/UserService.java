package ratingplayground.logic.users;

import java.util.List;

import ratingplayground.logic.exceptions.InvalidConfirmationCodeException;
import ratingplayground.logic.exceptions.UserAlreadyExistException;
import ratingplayground.logic.exceptions.UserNotFoundException;

public interface UserService {

	public UserEntity getUser(String playground, String email) throws UserNotFoundException;
	
	public UserEntity verifyNewUser(String playground, String email, int code) throws UserNotFoundException, UserAlreadyExistException ,InvalidConfirmationCodeException;
	
	public void updateUser (String playground , String email, UserEntity userEntityUpdates) throws UserNotFoundException;
	
	public UserEntity registerUser (UserEntity newUserEntity) throws UserAlreadyExistException;
	
	public void cleanup();
	
	
	//test only methods
	public UserEntity testRegisterUser(UserEntity userEntity) throws UserAlreadyExistException;
	List<UserEntity> testGetAllUsers() throws UserAlreadyExistException;
	

	


}
