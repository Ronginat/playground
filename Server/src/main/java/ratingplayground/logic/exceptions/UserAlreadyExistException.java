package ratingplayground.logic.exceptions;

public class UserAlreadyExistException extends RuntimeException{
	private static final long serialVersionUID = -4187366665072470133L;
	
	public UserAlreadyExistException() {
	}

	public UserAlreadyExistException(String message) {
		super(message);
	}

	public UserAlreadyExistException(Throwable cause) {
		super(cause);
	}

	public UserAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

}
