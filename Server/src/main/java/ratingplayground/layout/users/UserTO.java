package ratingplayground.layout.users;

import ratingplayground.logic.users.UserEntity;

public class UserTO {
	private String email;
	private String playground;
	private String username;
	private String avatar;
	private String role;
	private long points;
	private int code;
	
	
	public UserTO() {
	}
	
	public UserTO(String email, String playground, String username, String avatar, String role, long points) {
		this.email = email;
		this.playground = playground;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
	}
	

	public UserTO(UserEntity userEntity) {
		this();
		if (userEntity != null) {
			this.email = userEntity.getEmail();
			this.playground = userEntity.getPlayground();
			this.username = userEntity.getUsername();
			this.avatar = userEntity.getAvatar();
			this.role = userEntity.getRole();
			this.points = userEntity.getPoints();
			this.code = userEntity.getVerificationCode();
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
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
	public UserEntity toEntity () {
		
		return new UserEntity(this.email, this.playground,
				this.username, this.avatar, this.role, this.points);
		
	}
}
