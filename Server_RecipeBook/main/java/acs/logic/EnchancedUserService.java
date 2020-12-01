package acs.logic;

import java.util.List;

import acs.boundary.UserBoundary;


public interface EnchancedUserService extends UserService {
	
	public List<UserBoundary> getAllUser(String adminEmail, int size, int page);

}
