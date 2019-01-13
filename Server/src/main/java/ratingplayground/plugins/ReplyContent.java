package ratingplayground.plugins;

import java.util.HashMap;
import java.util.Map;

public class ReplyContent {
	private Map<String, Object> content;
	private final String DEFAULT_KEY = "content";
	
	public ReplyContent() {
		content = new HashMap<>();
	}
	
	public ReplyContent(Object content) {
		this();
		addAttribute(DEFAULT_KEY, content);
	}

	public ReplyContent(String key, Object content) {
		this();
		addAttribute(key, content);
	}

	public Map<String, Object> getContent() {
		return content;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content;
	}
	
	public void addAttribute(String key, Object content) {
		this.content.put(key, content);
	}
	
	@Override
	public String toString() {
		return content.toString();
	}
}

