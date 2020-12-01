package acs.logic;

import java.util.List;

import acs.boundary.UserBoundary;


public interface UserService {
	
	public UserBoundary createUser(UserBoundary input);
	
	public UserBoundary login(String userEmail);
	
	public UserBoundary updateUserDetails(String userEmail, UserBoundary update);
	
	public List<UserBoundary> getAllUser(String adminEmail);
	
	public void deleteAllUsers(String adminEmail);
	
}
