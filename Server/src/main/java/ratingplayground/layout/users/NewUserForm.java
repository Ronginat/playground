package ratingplayground.layout.users;




import ratingplayground.logic.users.UserEntity;

public class NewUserForm {
	private String email;
	private String username;
	private String avatar;
	private String role;
	
	public NewUserForm() {
	}
	
	public NewUserForm(String email, String username, String avatar, String role) {
		super();
		this.email = email;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
	
	public UserEntity toEntity () {
		
		UserEntity rv = new UserEntity();
		rv.setUsername(this.username);
		rv.setEmail(this.email);
		rv.setRole(this.role);
		rv.setAvatar(this.avatar);
		rv.setPoints(0);
		rv.setPlayground("ratingplayground");
		return rv;
	}
	

}
