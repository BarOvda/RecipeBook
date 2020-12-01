package acs.boundary;

public class UserBoundary {
	
	private String email;
	private Role role;
	private String username;
	private String avatar;

	public UserBoundary() {
	}

	public UserBoundary(String email,String username,String avatar,Role role) {
		
		super();
		this.email=email;
		this.role=role;
		this.username=username;
		this.avatar=avatar;
		
	}

	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
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

	@Override
	public String toString() {
		return "UserBoundary email=" + email + ", role=" + role + ", username=" + username + ", avatar=" + avatar;
	}

	

}