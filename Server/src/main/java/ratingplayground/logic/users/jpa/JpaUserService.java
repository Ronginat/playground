package ratingplayground.logic.users.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ratingplayground.aop.annotations.UserVerification;
import ratingplayground.dal.UserDao;
import ratingplayground.logic.AppConstants;
import ratingplayground.logic.MailSender;
import ratingplayground.logic.NumberGeneratorService;
import ratingplayground.logic.exceptions.InvalidConfirmationCodeException;
import ratingplayground.logic.exceptions.UserAlreadyExistException;
import ratingplayground.logic.exceptions.UserNotFoundException;
import ratingplayground.logic.users.UserEntity;
import ratingplayground.logic.users.UserService;


@Service
public class JpaUserService implements UserService{
	
	private UserDao users;
	private NumberGeneratorService numberGenerator;
	private MailSender sender;
	
	@Autowired
	public JpaUserService(UserDao users , NumberGeneratorService numberGenerator) {
		this.users = users;
		this.numberGenerator = numberGenerator;
	}
	
	@Override
	@UserVerification
	@Transactional(readOnly=true)
	public UserEntity getUser(String playground, String email) 
			throws UserNotFoundException{
		
		String key = playground + AppConstants.DELIMITER + email;
		
		return this.users.findById(key).orElseThrow(()->
		new UserNotFoundException("no user found for key : " + key));
	}

	
	@Override
	@Transactional
	public UserEntity verifyNewUser(String playground, String email, int code)
			throws UserNotFoundException , UserAlreadyExistException ,InvalidConfirmationCodeException {
		
		//getUser already throw the user not found exception in case needed
		UserEntity rv = this.getUser(playground, email);
		
		if (rv.getVerificationCode() == AppConstants.VERIFIED_USER_CODE) {
			throw new UserAlreadyExistException("user already confirmed");
		}
		
		//user code is not as the code stored in the database
		if (rv.getVerificationCode() != code) {
			throw new InvalidConfirmationCodeException("code missmatch: " + code);
		}
		
		
		rv.setVerificationCode(AppConstants.VERIFIED_USER_CODE);
		return this.users.save(rv);

	}

	@Override
	@UserVerification
	@Transactional
	public void updateUser(String playground, String email, UserEntity userEntityUpdates) 
			throws UserNotFoundException {
		
		//getUser already throw the user not found exception in case needed
		this.getUser(playground, email);
		UserEntity userToBeUpdated = this.getUser(userEntityUpdates.getPlayground() ,userEntityUpdates.getEmail());
		
		if (userEntityUpdates.getUsername() != null 
				&& !userEntityUpdates.getUsername().isEmpty()) {
			userToBeUpdated.setUsername(userEntityUpdates.getUsername());
		}

		if (userEntityUpdates.getAvatar() != null) {
			userToBeUpdated.setAvatar(userEntityUpdates.getAvatar());
		}
		
		if (userEntityUpdates.getRole() != null) {
			userToBeUpdated.setRole(userEntityUpdates.getRole());
		}
		
		this.users.save(userToBeUpdated);
	}

	
	
	@Override
	@Transactional
	public UserEntity registerUser(UserEntity newUserEntity) 
			throws UserAlreadyExistException {	
		String newUserkey = newUserEntity.getPlayground() + AppConstants.DELIMITER + newUserEntity.getEmail();
	
		
		if (!this.users.existsById(newUserkey)) {
			
			int code = this.numberGenerator.generateVerificationCode();
			
			newUserEntity.setVerificationCode(code);
			
			sender = new MailSender(newUserEntity.getEmail(), "" + code);
			
			return this.users.save(newUserEntity);
		
		} else {
			throw new UserAlreadyExistException("user with key: "+ newUserkey + " already registered");
		}
	}

	
	@Override
	@Transactional
	public void cleanup() {
		this.users.deleteAll();
	}


	@Override
	public UserEntity testRegisterUser(UserEntity newUserEntity) throws UserAlreadyExistException {
		String newUserkey = newUserEntity.getPlayground() + AppConstants.DELIMITER + newUserEntity.getEmail();
		
		if (!this.users.existsById(newUserkey)) {
			newUserEntity.setVerificationCode(AppConstants.VERIFIED_USER_CODE);
			return this.users.save(newUserEntity);
		
		} else {
			throw new UserAlreadyExistException("user with key: "+ newUserkey + " already registered");
		}
	}

	
	@Override
	public List<UserEntity> testGetAllUsers() throws UserAlreadyExistException {
		return (List<UserEntity>) this.users.findAll();
	}
}
