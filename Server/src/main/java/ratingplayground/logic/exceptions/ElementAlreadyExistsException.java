package ratingplayground.logic.exceptions;

public class ElementAlreadyExistsException extends RuntimeException{
	private static final long serialVersionUID = 4912451018026556029L;
	
	public ElementAlreadyExistsException() {
	}

	public ElementAlreadyExistsException(String message) {
		super(message);
	}

	public ElementAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public ElementAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
