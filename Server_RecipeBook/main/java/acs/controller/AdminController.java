package acs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import acs.boundary.ActionBoundary;
import acs.boundary.UserBoundary;
import acs.logic.ElementService;
import acs.logic.EnchancedActionService;
import acs.logic.EnchancedUserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

	private EnchancedUserService userService;
	private EnchancedActionService actionService;
	private ElementService elementService;

	@Autowired
	public AdminController(EnchancedUserService userService, EnchancedActionService actionService, ElementService elementService) {
		
		super();
		this.userService = userService;
		this.actionService = actionService;
		this.elementService = elementService;
		
	}

	//get all users 
	@RequestMapping(path = "/acs/admin/users/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] exportAllUsers(
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam (name = "size", required = false, defaultValue = "10") int size,
			@RequestParam (name = "page", required = false, defaultValue = "0") int page) {

		return this.userService
				.getAllUser(adminEmail, size, page).toArray(new UserBoundary[0]);
	}

	//get all actions 
	@RequestMapping(path = "/acs/admin/actions/{adminEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] exportAllActions(
			@PathVariable("adminEmail") String adminEmail,
			@RequestParam (name = "size", required = false, defaultValue = "10") int size,
			@RequestParam (name = "page", required = false, defaultValue = "0") int page) {

		return this.actionService
				.getAllActions(adminEmail, size, page).toArray(new ActionBoundary[0]);
	}

	//delete all users in the system
	@RequestMapping(path = "/acs/admin/users/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllUsersInTheSystem(@PathVariable("adminEmail") String email) {

		this.userService
			.deleteAllUsers(email);
	}

	//delete all elements in the system
	@RequestMapping(path = "/acs/admin/elements/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllElementsInTheSystem(@PathVariable("adminEmail") String email) {
		
		this.elementService
			.deleteAllElements(email);
	}

	//delete all actions in the system
	@RequestMapping(path = "/acs/admin/actions/{adminEmail}",
			method = RequestMethod.DELETE)
	public void deleteAllActionInTheSystems(@PathVariable("adminEmail") String email) {

		this.actionService
			.deleteAllActions(email);
	}
	
}
