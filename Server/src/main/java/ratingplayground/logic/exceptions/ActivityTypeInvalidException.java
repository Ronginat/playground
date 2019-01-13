package ratingplayground.logic.exceptions;

public class ActivityTypeInvalidException extends RuntimeException{
	private static final long serialVersionUID = 2283685234161282153L;

	public ActivityTypeInvalidException() {
	}

	public ActivityTypeInvalidException(String message) {
		super(message);
	}

	public ActivityTypeInvalidException(Throwable cause) {
		super(cause);
	}

	public ActivityTypeInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

}
