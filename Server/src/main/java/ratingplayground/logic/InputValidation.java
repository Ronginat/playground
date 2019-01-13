package ratingplayground.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ratingplayground.logic.exceptions.InvalidConfirmationCodeException;
import ratingplayground.logic.exceptions.InvalidEmailException;

public class InputValidation {
	
	 private static Pattern regexPattern;
	 private static Matcher regMatcher;
	 
	 
	 public static void validateEmailAddress(String emailAddress) throws InvalidEmailException {

	        regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
	        regMatcher   = regexPattern.matcher(emailAddress);
	        if(!regMatcher.matches()) {
	            throw new InvalidEmailException("Email not valid");
	        }
	    }
	 
	 
	 public static void validateUserConfirmationCode(int code) throws InvalidConfirmationCodeException{
		 if (code < 0 || code > 99999) {
	            throw new InvalidConfirmationCodeException("Illegal confirmation code");
		 }
	 }
}
