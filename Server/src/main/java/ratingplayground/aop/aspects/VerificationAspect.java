package ratingplayground.aop.aspects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
//import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ratingplayground.dal.UserDao;
import ratingplayground.logic.AppConstants;
import ratingplayground.logic.activities.ActivityEntity;
import ratingplayground.logic.exceptions.UserNotFoundException;
import ratingplayground.logic.users.UserEntity;

@Component
@Aspect
public class VerificationAspect {
	//private Log log = LogFactory.getLog(VerficationAspect.class);
	private UserDao users;
	
	
	@Autowired
	public VerificationAspect(UserDao users) {
		this.users = users;
	}
	
	@Transactional(readOnly=true)
	public UserEntity getUser(String playground, String email) 
			throws UserNotFoundException{
		
		String key = playground + AppConstants.DELIMITER + email;
		
		return this.users.findById(key).orElseThrow(()->
		new UserNotFoundException("no user found for key : " + key));
	}
	
	@Around("@annotation(ratingplayground.aop.annotations.ManagerVerification) && args(userPlayground, email,..)")
	public Object verifyManager (ProceedingJoinPoint joinPoint, String userPlayground, String email) throws Throwable {
		try {
			UserEntity user = getUser(userPlayground, email);
			if (user.getVerificationCode() != AppConstants.VERIFIED_USER_CODE) { 
				throw new RuntimeException("you are not a verified user!");
			}
			else if (!AppConstants.MANAGER.equals(user.getRole())) { 
				throw new RuntimeException("you are not a manager!");
			}
			
			return joinPoint.proceed();
			
		} catch (UserNotFoundException e) {
			throw e;
		}
	}
	
	@Around("@annotation(ratingplayground.aop.annotations.PlayerVerification) && args(userPlayground, email,..)")
	public Object verifyPlayer (ProceedingJoinPoint joinPoint, String userPlayground, String email) throws Throwable {
		try {
			UserEntity user = getUser(userPlayground, email);
			if (user.getVerificationCode() != AppConstants.VERIFIED_USER_CODE) { 
				throw new RuntimeException("you are not a verified user!");
			}
			else if (!AppConstants.PLAYER.equals(user.getRole())) { 
				throw new RuntimeException("you are not a player!");
			}
			
			return joinPoint.proceed();
			
		} catch (UserNotFoundException e) {
			throw e;
		}
	}
	
	@Around("@annotation(ratingplayground.aop.annotations.PlayerVerification) && args(activity)")
	public Object verifyPlayer (ProceedingJoinPoint joinPoint, ActivityEntity activity) throws Throwable {
		try {
			UserEntity user = getUser(activity.getPlayerPlayground(), activity.getPlayerEmail());
			if (user.getVerificationCode() != AppConstants.VERIFIED_USER_CODE) { 
				throw new RuntimeException("you are not a verified user!");
			}
			else if (!AppConstants.PLAYER.equals(user.getRole())) { 
				throw new RuntimeException("you are not a player!");
			}
			
			return joinPoint.proceed();
			
		} catch (UserNotFoundException e) {
			throw e;
		}
	}
	
	@Around("@annotation(ratingplayground.aop.annotations.UserVerification) && args(isManager, userPlayground, email,..)")
		public Object verifyUser (ProceedingJoinPoint joinPoint, Boolean isManager, String userPlayground, String email) throws Throwable {
			try {
				UserEntity user = getUser(userPlayground, email);
				if (user.getVerificationCode() != AppConstants.VERIFIED_USER_CODE) { 
					throw new RuntimeException("you are not a verified user!");
				} else if (user.getRole() == null) { 
					throw new RuntimeException("you need a role!");
				} else {
					Object[] params = joinPoint.getArgs();
					params[0] = AppConstants.MANAGER.equals(user.getRole());
					return joinPoint.proceed(params);
				}
					
			} catch (UserNotFoundException e) {
				throw e;
			}
		}
	
	
	@Around("@annotation(ratingplayground.aop.annotations.UserVerification) && args(playground, email,..)")
	public Object verifyUser (ProceedingJoinPoint joinPoint, String playground, String email) throws Throwable {
		try {
			UserEntity user = getUser(playground, email);
			if (user.getVerificationCode() != AppConstants.VERIFIED_USER_CODE) { 
				throw new RuntimeException("you are not a verified user!");
			} else if (user.getRole() == null) { 
				throw new RuntimeException("you need a role!");
			} else {
				return joinPoint.proceed();
			}
				
		} catch (UserNotFoundException e) {
			throw e;
		}
	}
}