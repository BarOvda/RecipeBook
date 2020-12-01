package acs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.boundary.ActionBoundary;
import acs.logic.ActionService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ActionController {
	
	private ActionService actionService;

	@Autowired
	public ActionController(ActionService actionService) {
		
		super();
		this.actionService = actionService;
		
	}
	
//Create action and save
	@RequestMapping(path = "/acs/actions",
			 method = RequestMethod.POST,
			 produces = MediaType.APPLICATION_JSON_VALUE,
			 consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object invokeAnAction(@RequestBody ActionBoundary input) {

		return this.actionService
				.invokeAction(input);

	}

}
