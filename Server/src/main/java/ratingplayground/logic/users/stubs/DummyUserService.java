package ratingplayground.logic.users.stubs;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import ratingplayground.logic.exceptions.InvalidConfirmationCodeException;
import ratingplayground.logic.exceptions.UserAlreadyExistException;
import ratingplayground.logic.exceptions.UserNotFoundException;
import ratingplayground.logic.users.UserEntity;
import ratingplayground.logic.users.UserService;



//@Service
public class DummyUserService implements UserService{
	private Map<String, UserEntity> users;
	private String delimiter = "@@";
	
	@PostConstruct
	public void init() {
		this.users = Collections.synchronizedMap(new HashMap<>());
	}

	
	@Override
	public UserEntity getUser(String playground, String email) 
			throws UserNotFoundException{
		
		String key = playground + delimiter + email;
		UserEntity rv = this.users.get(key);
		
		//user not exist
		if (rv == null) {
			throw new UserNotFoundException("no user found for the key: " + key);
		}
		return rv;
	}

	
	@Override
	public UserEntity verifyNewUser(String playground, String email, int code)
			throws UserNotFoundException ,InvalidConfirmationCodeException  {
		
		String key = playground + delimiter + email;
		UserEntity rv = this.users.get(key);
		
		//user not exist
		if (rv == null) {
			throw new UserNotFoundException("no user found for the key: " + key);
		}
		
		if (rv.getVerificationCode() == -1) {
			throw new RuntimeException("user already confirmed");
		}
		
		//user code is not as the code stored in the database
		if (rv.getVerificationCode() != code) {
			throw new InvalidConfirmationCodeException("user with key: " + key + "- code missmatch: " + code);
		}
		
		rv.setVerificationCode(-1);
		return rv;
	}

	
	@Override
	public UserEntity registerUser(UserEntity newUserEntity) 
			throws UserNotFoundException,UserAlreadyExistException {
		
		//user not exist
		if (newUserEntity == null) {
			throw new UserNotFoundException("newUserEntity user is null");
		}
		
		String key = newUserEntity.getPlayground() + delimiter + newUserEntity.getEmail();
		
		//user already registered
		if (users.containsKey(key)) {
			throw new UserAlreadyExistException("user with key: "+ key + " already registered");
		}
		
		newUserEntity.setVerificationCode(9999);
		this.users.put(key, newUserEntity);
		return newUserEntity;
	}

	
	@Override
	public void updateUser(String playground, String email, UserEntity userEntityUpdates) 
			throws UserNotFoundException{
	
		String userPerformActionKey = playground + delimiter + email;
		String userToBeUpdatedKey = userEntityUpdates.getPlayground()+ delimiter + userEntityUpdates.getEmail();
		
		synchronized (this.users) {
			
			UserEntity userToBeUpdated = this.users.get(userToBeUpdatedKey);
			UserEntity userPerformAction = this.users.get(userPerformActionKey);
			
			//updated user not exist
			if (userToBeUpdated == null) {
				throw new UserNotFoundException("no user found for the key: " + userToBeUpdatedKey);
			}
			
			//active user not exist
			if (userPerformAction == null) {
				throw new UserNotFoundException("no user found for the key: " + userPerformActionKey);
			}
				
			boolean dirty = false;
				
			if (userEntityUpdates.getUsername() != null 
					&& !userEntityUpdates.getUsername().isEmpty()) {
			
				userToBeUpdated.setUsername(userEntityUpdates.getUsername());
				dirty = true;
			}
			
			
			if (userEntityUpdates.getAvatar() != null) {
				userToBeUpdated.setAvatar(userEntityUpdates.getAvatar());
				dirty = true;
			}
			
			if (dirty) { 
				this.users.put(userToBeUpdatedKey, userToBeUpdated);
			}
		}
	}


	@Override
	public void cleanup() {
		this.users.clear();		
	}


	@Override
	public UserEntity testRegisterUser(UserEntity userEntity) throws UserAlreadyExistException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserEntity> testGetAllUsers() throws UserAlreadyExistException {
		return null;
	}

	

	
}
