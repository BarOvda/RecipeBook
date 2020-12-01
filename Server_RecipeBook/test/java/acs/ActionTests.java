package acs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import acs.boundary.ActionBoundary;
import acs.boundary.ElementBoundary;
import acs.boundary.LocationBoundery;
import acs.boundary.Role;
import acs.boundary.UserBoundary;
import acs.boundary.aidClass.BounderyElement;
import acs.boundary.aidClass.BoundaryEmail;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActionTests {
	
	private RestTemplate restTemplate;
	private String url;
	private int port;
	private UserBoundary admin;
	private UserBoundary manager;
	private UserBoundary player;
	private ElementBoundary element;

	@LocalServerPort
	public void setPort(int port) {
		
		this.port = port;
		
	}

	@PostConstruct
	public void init() {
		
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/acs/actions";
		
	}

	@BeforeEach
	public void setup() {
		
		admin = this.restTemplate.postForObject("http://localhost:" + port + "/acs/users",
				new UserBoundary("bar.ovda@afeka.com", "bar", ":)", Role.ADMIN), UserBoundary.class);

		manager = this.restTemplate.postForObject("http://localhost:" + port + "/acs/users",
				new UserBoundary("lir@gmail.com", "lir", ":)", Role.MANAGER), UserBoundary.class);

		player = this.restTemplate.postForObject("http://localhost:" + port + "/acs/users",
				new UserBoundary("yevgeni@gmail.com", "yevgeni", ":)", Role.PLAYER), UserBoundary.class);

		this.element = this.restTemplate.postForObject(
				"http://localhost:" + port + "/acs/elements/" + manager.getEmail(),
				new ElementBoundary(null, "test", "test-element", true, null, null,
						new LocationBoundery(32.1155f, 34.1432f),
						Collections.singletonMap("first-attribute", "test-attribute")),
				ElementBoundary.class);
	}

	@AfterEach
	public void teardown() {
		this.restTemplate.delete("http://localhost:" + port + "/acs/admin/actions/" + admin.getEmail());
		this.restTemplate.delete("http://localhost:" + port + "/acs/admin/elements/" + admin.getEmail());
		this.restTemplate.delete("http://localhost:" + port + "/acs/admin/users/" + admin.getEmail());
		
	}

	@Test
	public void testContext() {

	}

	@Test
	public void testGetAllActionsWithDatabaseContainig3ActionsReturnsAllActionsInTheDatabase() throws Exception {
		// GIVEN server is up
		// AND the database contains 3 actions

		List<ActionBoundary> databaseContent = IntStream.range(0, 3) // Stream<Integer>
				.mapToObj(i -> new ActionBoundary(
						    null,
							"action #" + i + "Test",
						    new BounderyElement(element.getElementId()),
						    null,
						    new BoundaryEmail(this.player.getEmail()),
						    Collections.singletonMap("first-attribute", "test-attribute")))
				.map(msg -> this.restTemplate.postForObject(this.url,
							msg,
							ActionBoundary.class)) // Stream<UserBoundary>
				.collect(Collectors.toList()); // List<UserBoundary>

		// WHEN I GET acs/admin/actions

		ActionBoundary[] dataFromServer = this.restTemplate.getForObject(
				"http://localhost:" + port + "/acs/admin/actions/" + admin.getEmail()
				, ActionBoundary[].class);

		// THEN the server returns status 2xx
		// AND the response includes all actions in the database

		assertThat(dataFromServer)
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactlyInAnyOrderElementsOf(databaseContent);

	}

	@Test
	public void testPostActionReturnsActionDetailsInResponse() throws Exception {
		// GIVEN server is up
		// do nothing

		// WHEN I POST acs/actions with a new action
		
		ActionBoundary actionToPost = new ActionBoundary(
				"7",
				"action",
				new BounderyElement(element.getElementId()),
				null,
				new BoundaryEmail(this.player.getEmail()),
				Collections.singletonMap("first-attribute", "test-attribute"));

		ActionBoundary actionFromServer = this.restTemplate.postForObject(this.url, actionToPost, ActionBoundary.class);

		// THEN the server responds with the same user details
		assertThat(actionFromServer).
			isEqualToComparingOnlyGivenFields(actionToPost,
					"type",
					"element",
					"invokedBy",
				    "actionAttributes");

		// cleanup - delete all users from database
		// do nothing
	}

	@Test
	public void testGetAllActionsOnServerInitReturnsEmptyArray() throws Exception {
		// GIVEN server is up
		// do nothing

		// WHEN I GET /actions
		
		ActionBoundary[] allActions = this.restTemplate.getForObject(
				"http://localhost:" + port + "/acs/admin/actions/" + admin.getEmail(), ActionBoundary[].class);

		assertThat(allActions).isEmpty();
	}

	
	
	  @Test public void testFollowByAction() throws Exception {
	  
	  UserBoundary followed = this.restTemplate.postForObject("http://localhost:" + port + "/acs/users",
			  new UserBoundary("barovda@gmail.com", "bar", ":)",
			  Role.PLAYER),
			  UserBoundary.class);
	  
	  ActionBoundary followAction= new ActionBoundary(
			  null,
			  "follow",
			  new BounderyElement(element.getElementId()),
			  null,
			  new BoundaryEmail(this.player.getEmail()),
			  Collections.singletonMap("followed", followed.getEmail()));
	  
	  this.restTemplate.postForObject(
			  this.url ,
			  followAction,
			  ActionBoundary.class);
	  
	  
	 Map<String, Object> m = new HashMap<>();
	  
	  m.put("page", 0);
	  m.put("followed", followed.getEmail());
	  
	  ActionBoundary getFollowedByAction= new ActionBoundary(null,
	  "getAllFollowedByMe",
	  new BounderyElement(element.getElementId()),
	  null,
	  new BoundaryEmail(this.player.getEmail()),
	 m);
	  
	  //Retuen List->Map->UserBoundery
	 UserBoundary[] followedByMe =
	  this.restTemplate
	  	.postForObject(
	  		  this.url,
	  		  getFollowedByAction,
			  UserBoundary[].class);
	  
	  if(followedByMe.length!=1) {
		  throw new RuntimeException("there is not exactly one follower");
	  }
	  
	  assertThat(followedByMe)
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactly(followed);
	  }
	  
	  @Test
		public void createTwoElementOfTypeRecpieAndGetUsersRecpies() throws Exception {
		//create two elements of type recipe
		  List<ElementBoundary> databaseContent = IntStream.range(0, 2)
				  .mapToObj(i -> new ElementBoundary(null,
						  "recipe",
						  "recipe"+i,
						  true,
						  null,
						  null,
						  new LocationBoundery(32.1155f, 34.1432f),
						  Collections.singletonMap("first-attribute", "test-attribute")))
					.map(msg -> this.restTemplate.postForObject("http://localhost:" + port + "/acs/elements/"+ this.player.getEmail(),
						 msg,
					     ElementBoundary.class))
					.collect(Collectors.toList());
		  
		ElementBoundary recipe1 = databaseContent.get(0);
		ElementBoundary recipe2 = databaseContent.get(1);
		  
		  ActionBoundary getRecpiesAction= new ActionBoundary(
				  null,
				  "getAllUsersRecpies" ,
				  new BounderyElement(this.element.getElementId()),
				  null,
				  new BoundaryEmail(this.player.getEmail()),
				  Collections.singletonMap("page", 0));
		  
		  ElementBoundary[] usersRecpies =	  
				  this.restTemplate.postForObject(this.url ,
						  getRecpiesAction, ElementBoundary[].class);
		 
		  if(usersRecpies.length!=2) 
			  throw new RuntimeException("there is not exactly two recipes");	
		  

		  assertThat(usersRecpies[0]).extracting("elementId", "type", "name", "active"
				  , "createdTimestamp", "createdBy", "location",
					"elementAttributes").containsExactly(
							recipe2.getElementId(),
							recipe2.getType(),
							recipe2.getName(),
							recipe2.getActive(), 
							recipe2.getCreatedTimestamp(),
							recipe2.getCreatedBy(),
							recipe2.getLocation(),
							recipe2.getElementAttributes());
	  
		  assertThat(usersRecpies[1]).extracting("elementId", "type", "name", "active"
				  , "createdTimestamp", "createdBy", "location",
				  "elementAttributes").containsExactly(
						  recipe1.getElementId(),
						  recipe1.getType(),
						  recipe1.getName(),
						  recipe1.getActive(), 
						  recipe1.getCreatedTimestamp(),
						  recipe1.getCreatedBy(),
						  recipe1.getLocation(),
						  recipe1.getElementAttributes());

	  }
	  
	  @Test
		public void searchUserByName() throws Exception {
		  
		  Map<String, Object> m = new HashMap<>();
		  
		  m.put("page", 0);
		  m.put("username", this.player.getUsername());
		  
		  ActionBoundary searchAction= new ActionBoundary(null, "searchUser" ,
				  new BounderyElement(element.getElementId()), null,
				  new BoundaryEmail(this.player.getEmail()),
				  m);
		 
		  UserBoundary[] searchResult =	  
				  this.restTemplate.postForObject(this.url ,
						  searchAction, UserBoundary[].class);
		  
		  if(searchResult.length!=1) 
			  throw new RuntimeException("there is not exactly one result");	
		  
		  assertThat(searchResult)
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactly(this.player);
		 
	  }
	  
}