package acs.logic;

import java.util.List;

import acs.boundary.ActionBoundary;
import acs.boundary.ElementBoundary;
import acs.boundary.UserBoundary;

public interface EnchancedActionService extends ActionService {

	public List<ActionBoundary> getAllActions(String adminEmail, int size, int page);

	public List<UserBoundary> getAllFollowedByMe(String type, String myEmail, int size, int page);

	public List<ElementBoundary> getUsersRecipes(String type, String createdBy, int size, int page);

	public List<UserBoundary> getUsersSearch(String username, int size, int page);
	
	public List<ElementBoundary> getAllRecipeLike(String type, String name, int size, int page);

	public List<ElementBoundary> getFeed(String email, int size, int page);
	
}
