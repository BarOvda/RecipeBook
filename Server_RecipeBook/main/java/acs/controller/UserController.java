package acs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundary.UserBoundary;
import acs.logic.EnchancedUserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
	
	private EnchancedUserService userService;

	@Autowired
	public UserController(EnchancedUserService userService) {
		
		super();
		this.userService = userService;
		
	}

	//get specific user 
	@RequestMapping(path = "/acs/users/login/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary loginValidUserAndRetrieveUserDetails(@PathVariable("userEmail") String email) {

		return this.userService
				.login(email);

	}

	//create new user and save
	@RequestMapping(path = "/acs/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createaNewUser(@RequestBody UserBoundary input) {

		return this.userService
				.createUser(input);
	}

	//updating and saving an existing user
	@RequestMapping(path = "/acs/users/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserDetails(@PathVariable("userEmail") String userEmail, @RequestBody UserBoundary update) {

		this.userService
			.updateUserDetails(userEmail, update);
	}

}
