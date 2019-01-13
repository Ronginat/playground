package ratingplayground.layout.elements;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ratingplayground.logic.elements.ElementEntity;
import ratingplayground.logic.elements.Location;

public class ElementTO {
	
	private String playground;
	private String id;
	private Location location;
	private String name;
	private Date creationDate;
	private Date expirationDate;
	private String type;
	private Map<String, Object> attributes;
	private String creatorPlayground;
	private String creatorEmail;
	
	
	public ElementTO() {
		this.attributes = new HashMap<>();
	}
	
	public ElementTO(ElementEntity element) {
		this();
		if(element != null) {
			this.playground = element.getPlayground();
			this.id = element.getId();
			this.location = new Location(element.getX(), element.getY());
			this.name = element.getName();
			this.creationDate = element.getCreationDate();
			this.expirationDate = element.getExpirationDate();
			this.type = element.getType();
			this.attributes = element.getAttributes();
			this.creatorPlayground = element.getCreatorPlayground();
			this.creatorEmail = element.getCreatorEmail();
		}
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


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public Date getExpirationDate() {
		return expirationDate;
	}


	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Map<String, Object> getAttributes() {
		return attributes;
	}


	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}


	public String getCreatorPlayground() {
		return creatorPlayground;
	}


	public void setCreatorPlayground(String creatorPlayground) {
		this.creatorPlayground = creatorPlayground;
	}


	public String getCreatorEmail() {
		return creatorEmail;
	}


	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public ElementEntity toEntity() {
		ElementEntity rv = new ElementEntity();
		rv.setPlayground(this.playground);
		rv.setId(this.id);
		rv.setX(this.location.getX());
		rv.setY(this.location.getY());
		rv.setName(this.name);
		rv.setCreationDate(this.creationDate);
		rv.setExpirationDate(this.expirationDate);
		rv.setType(this.type);
		rv.setAttributes(this.attributes);
		rv.setCreatorPlayground(this.creatorPlayground);
		rv.setCreatorEmail(this.creatorEmail);
		return rv;
	}

}
