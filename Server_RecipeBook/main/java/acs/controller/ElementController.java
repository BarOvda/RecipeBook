package acs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import acs.boundary.ChildIdWrapper;
import acs.boundary.ElementBoundary;
import acs.logic.EnchancedElementService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ElementController {

	private EnchancedElementService elementService;

	@Autowired
	public ElementController(EnchancedElementService elementService) {
		
		super();
		this.elementService = elementService;
		
	}
	
	//get specific element
	@RequestMapping(path = "/acs/elements/{userEmail}/{elementId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary retrieveSpecificElement(@PathVariable("elementId") String elementID,
			@PathVariable("userEmail") String userEmail) {

		return this.elementService
				.getSpecificElement(userEmail, elementID);
	}

	//get all elements
	@RequestMapping(
			path = "/acs/elements/{userEmail}", 
			method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getAllElements(
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page, 
			@RequestParam(name = "size", required = false, defaultValue = "10") int size) {
		
		return this.elementService.getAllElements(userEmail,size, page)
				.toArray(new ElementBoundary[0]);
}
	
	//create element and save
	@RequestMapping(path = "/acs/elements/{managerEmail}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary createaNewElement(@PathVariable("managerEmail") String managerEmail,
			@RequestBody ElementBoundary elementBoundary) {

		return this.elementService
				.create(managerEmail, elementBoundary);
	}

	//updating and saving an existing element
	@RequestMapping(path = "/acs/elements/{managerEmail}/{elementId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateAnElement(@PathVariable("elementId") String elementID,
			@PathVariable("managerEmail") String managerEmail, @RequestBody ElementBoundary elementBoundrary) {
		
		this.elementService
			.update(managerEmail, elementID, elementBoundrary);
	}

	//bind between element (a child-parent relationship)
	@RequestMapping(path = "/acs/elements/{managerEmail}/{parentElementId}/children",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void bindExistingElementToAnExstingChildElement(@PathVariable("managerEmail") String managerEmail,
			@PathVariable("parentElementId") String parentElementId, @RequestBody ChildIdWrapper childId) {
		
		this.elementService
		.addChildElementToElement(managerEmail, parentElementId, childId.getId());
	}

	//get all parent child's
	@RequestMapping(path = "acs/elements/{userEmail}/{parentElementId}/children",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getChildren (
			@PathVariable("userEmail") String userEmail,
			@PathVariable("parentElementId") String parentId,
			@RequestParam(name="size", required = false, defaultValue = "10") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page) { 
		
		return this.elementService
			.getChildren(userEmail, parentId, size, page)
			.toArray(new ElementBoundary[0]); 
	}
	
	//get all child parent's
	@RequestMapping(path = "acs/elements/{userEmail}/{childId}/parents",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getParents (
			@PathVariable("childId") String childId,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="size", required = false, defaultValue = "10") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page) { 
		
		return this.elementService
			.getParents(userEmail, childId, size, page)
			.toArray(new ElementBoundary[0]); 
	}
	
	//search elements by name
	@RequestMapping(path = "acs/elements/{userEmail}/search/byName/{name}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsByName (
			@PathVariable("userEmail") String userEmail,
			@PathVariable("name") String name,
			@RequestParam (name = "size", required=false, defaultValue = "10") int size,
			@RequestParam (name = "page", required=false, defaultValue = "0") int page) { 
		
		return this.elementService
			.getElementsByName(userEmail, name, size, page)
			.toArray(new ElementBoundary[0]); 
	}
	
	//search elements by type
	@RequestMapping(path = "acs/elements/{userEmail}/search/byType/{type}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElementsByType (
			@PathVariable("userEmail") String userEmail,
			@PathVariable("type") String type,
			@RequestParam (name = "size", required=false, defaultValue = "10") int size,
			@RequestParam (name = "page", required=false, defaultValue = "0") int page) { 
		
		return this.elementService
			.getElementsByType(userEmail,type, size, page)
			.toArray(new ElementBoundary[0]); 
	}

}
