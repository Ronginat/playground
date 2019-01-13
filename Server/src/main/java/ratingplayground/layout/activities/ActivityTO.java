package ratingplayground.layout.activities;

import java.util.HashMap;
import java.util.Map;

import ratingplayground.logic.activities.ActivityEntity;

public class ActivityTO {
	private String playground;
	private String id;
	private String elementPlayground;
	private String elementId;
	private String type;
	private String playerPlayground;
	private String playerEmail;
	private Map<String,Object> attributes;
	
	public ActivityTO() {
		attributes = new HashMap<>();
	}
	
	public ActivityTO(String playground, String id, String elementPlayground, String elementId, String type,
			String playerPlayground, String playerEmail) {
		this();
		this.playground = playground;
		this.id = id;
		this.elementPlayground = elementPlayground;
		this.elementId = elementId;
		this.type = type;
		this.playerPlayground = playerPlayground;
		this.playerEmail = playerEmail;
	}
	
	public ActivityTO(ActivityEntity activity) {
		this();
		this.playground = activity.getPlayground();
		this.id = activity.getId();
		this.elementPlayground = activity.getElementPlayground();
		this.elementId = activity.getElementId();
		this.type = activity.getType();
		this.playerPlayground = activity.getPlayerPlayground();
		this.playerEmail = activity.getPlayerEmail();
		this.attributes = activity.getAttributes();
	}

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getElementPlayground() {
		return elementPlayground;
	}

	public void setElementPlayground(String elementPlayground) {
		this.elementPlayground = elementPlayground;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlayerPlayground() {
		return playerPlayground;
	}

	public void setPlayerPlayground(String playerPlayground) {
		this.playerPlayground = playerPlayground;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public ActivityEntity toEntity() {
		ActivityEntity rv = new ActivityEntity();
		rv.setPlayground(playground);
		rv.setId(id);
		rv.setElementPlayground(elementPlayground);
		rv.setElementId(elementId);
		rv.setType(type);
		rv.setPlayerPlayground(playerPlayground);
		rv.setPlayerEmail(playerEmail);
		rv.setAttributes(attributes) ;
		System.out.println(rv.toString());
		return rv;
	}

	@Override
	public String toString() {
		return "ActivityTO [playground=" + playground + ", id=" + id + ", elementPlayground=" + elementPlayground
				+ ", elementId=" + elementId + ", type=" + type + ", playerPlayground=" + playerPlayground
				+ ", playerEmail=" + playerEmail + ", attributes=" + attributes + "]";
	}
	
	
}