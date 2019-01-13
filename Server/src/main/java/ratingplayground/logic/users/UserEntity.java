package ratingplayground.logic.users;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ratingplayground.logic.AppConstants;


@Entity
@Table(name = "USERS")
public class UserEntity {
	
	
	private String email;
	private String playground;
	private String username;
	private String avatar;
	private String role;
	private long points;
	private int code;
	
	
	public UserEntity() {
	}
	
	public UserEntity(String email, String playground, String username, String avatar, String role, long points) {
		this.email = email;
		this.playground = playground;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
//		this.code = 0;
	}
	
	
	@Id
	public String getPk() {
		return playground + AppConstants.DELIMITER + email;
	}
	
	public void setPk(String pk) {
		if (pk != null) {
			String[] arr = pk.split(AppConstants.DELIMITER); 
			this.playground = arr[0];
			this.email = arr[1];
		}
	}
	
	
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPlayground() {
		return playground;
	}
	
	public void setPlayground(String playground) {
		this.playground = playground;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public long getPoints() {
		return points;
	}
	
	public void setPoints(long points) {
		this.points = points;
	}

	
	public int getVerificationCode() {
		return code;
	}

	public void setVerificationCode(int code) {
		this.code = code;
	}
	
	
}
