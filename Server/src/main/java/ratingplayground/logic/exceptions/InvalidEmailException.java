package ratingplayground.logic.exceptions;

public class InvalidEmailException extends RuntimeException{
	private static final long serialVersionUID = 6505346834100479929L;
	
	public InvalidEmailException() {
	}

	public InvalidEmailException(String message) {
		super(message);
	}

	public InvalidEmailException(Throwable cause) {
		super(cause);
	}

	public InvalidEmailException(String message, Throwable cause) {
		super(message, cause);
	}

}
