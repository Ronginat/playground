package ratingplayground.logic.exceptions;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 6246607809055887272L;

	public UserNotFoundException() {
	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
