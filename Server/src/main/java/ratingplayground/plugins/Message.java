package ratingplayground.plugins;

import java.util.Date;

public class Message {
	private String message;
	private String date;
	private String username;

	public Message() {
		this.date = (new Date()).toString();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}	
}
