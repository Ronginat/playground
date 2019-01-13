package ratingplayground.logic.exceptions;

public class ElementNotFoundException extends RuntimeException{
	private static final long serialVersionUID = -6719568713629064262L;
	
	public ElementNotFoundException() {
	}

	public ElementNotFoundException(String message) {
		super(message);
	}

	public ElementNotFoundException(Throwable cause) {
		super(cause);
	}

	public ElementNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
