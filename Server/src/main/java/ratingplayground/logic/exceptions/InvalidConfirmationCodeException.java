package ratingplayground.logic.exceptions;

public class InvalidConfirmationCodeException extends RuntimeException{
	private static final long serialVersionUID = 7310680631134426948L;
	
	public InvalidConfirmationCodeException() {
	}

	public InvalidConfirmationCodeException(String message) {
		super(message);
	}

	public InvalidConfirmationCodeException(Throwable cause) {
		super(cause);
	}

	public InvalidConfirmationCodeException(String message, Throwable cause) {
		super(message, cause);
	}

}
