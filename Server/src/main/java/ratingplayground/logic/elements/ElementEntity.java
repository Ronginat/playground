package ratingplayground.logic.elements;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.ObjectMapper;

import ratingplayground.logic.AppConstants;

@Entity
@Table(name = "ELEMENTS")
public class ElementEntity {
	
	private final String defaultPlayground = AppConstants.PLAYGROUND;
	
	private String playground;
	private String id;
	private Double x;
	private Double y;
	private String name;
	private Date creationDate;
	private Date expirationDate;
	private String type;
	private Map<String, Object> attributes;
	private String creatorPlayground;
	private String creatorEmail;
	
	public ElementEntity() {
		this.playground = this.defaultPlayground;
		this.attributes = new HashMap<>();
		x = 0D;
		y = 0D;
	}
	
	public ElementEntity(String playground, String id, Double x, Double y, String name, Date creationDate,
			Date expirationDate, String type, Map<String, Object> attributes, String creatorPlayground,
			String creatorEmail) {
		this();
		this.playground = playground;
		this.id = id;
		this.x = x;
		this.y = y;
		this.name = name;
		this.creationDate = creationDate;
		this.expirationDate = expirationDate;
		this.type = type;
		this.attributes = attributes;
		this.creatorPlayground = creatorPlayground;
		this.creatorEmail = creatorEmail;
	}
	
	public ElementEntity(String name) {
		this();
		this.name = name;
	}
	
	@Id
	public String getPk() {
		return playground + AppConstants.DELIMITER + id;
	}
	
	public void setPk(String pk) {
		if (pk != null) {
			String[] arr = pk.split(AppConstants.DELIMITER); 
			this.playground = arr[0];
			this.id = arr[1];
		}
	}

	@Transient
	public String getPlayground() {
		return playground;
	}


	public void setPlayground(String playground) {
		this.playground = playground;
	}

	@Transient
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public void setX(Double x) {
		this.x = x;
	}
	
	public void setY(Double y) {
		this.y = y;
	}
	
	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationDate() {
		return creationDate;
	}
	
	// create creationDate automatically when initializing the Entity in DB
	@PrePersist
    protected void onCreate() {
		creationDate = new Date();
    }


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
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
	
	@Transient
	public Map<String, Object> getAttributes() {
		return attributes;
	}


	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	@Lob
	public String getJsonAttributes() {
		try {
			return new ObjectMapper().writeValueAsString(this.attributes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// unmarshal
	public void setJsonAttributes(String jsonAttributes) {
		try {
			this.attributes = new ObjectMapper().readValue(jsonAttributes, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

	@Override
	public String toString() {
		return "ElementEntity [defaultPlayground=" + defaultPlayground + ", playground=" + playground + ", id=" + id
				+ ", x=" + x + ", y=" + y + ", name=" + name + ", creationDate=" + creationDate + ", expirationDate="
				+ expirationDate + ", type=" + type + ", attributes=" + attributes + ", creatorPlayground="
				+ creatorPlayground + ", creatorEmail=" + creatorEmail + "]";
	}
	
	

}
