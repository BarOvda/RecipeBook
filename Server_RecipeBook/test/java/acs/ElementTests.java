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
import acs.boundary.ElementBoundary;
import acs.boundary.LocationBoundery;
import acs.boundary.Role;
import acs.boundary.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ElementTests {
	private RestTemplate restTemplate;
	private String url;
	private int port;
	private UserBoundary manager;
	private UserBoundary admin;


	@LocalServerPort
	public void setPort(int port) {
		
		this.port = port;
		
	}

	@PostConstruct
	public void init() {
		
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/acs/elements/";

	}

	@BeforeEach
	public void setup() {
		// setup test environment before each test

		manager = this.restTemplate.postForObject("http://localhost:" + port + "/acs/users",
				new UserBoundary("lir@gmail.com", "lir", ":)", Role.MANAGER), UserBoundary.class);
		
		admin = this.restTemplate.postForObject("http://localhost:" + port + "/acs/users",
				new UserBoundary("bar@gmail.com", "bar", ":)", Role.ADMIN), UserBoundary.class);

	}

	@AfterEach
	public void teardown() {
		// cleanup test environment after each test
		this.restTemplate.delete("http://localhost:" + port + "/acs/admin/elements/" + this.admin.getEmail());
		this.restTemplate.delete("http://localhost:" + port + "/acs/admin/users/" + this.admin.getEmail());

	}

	@Test
	public void testContext() {

	}

	@Test
	public void testGetAllElementsWithDatabaseContainig2ElementsReturnsAllElementsInTheDatabase() throws Exception {
		// GIVEN server is up
		// do nothing

		// GIVEN server is up
		// AND the database contains 2 elements
		List<ElementBoundary> databaseContent = IntStream.range(0, 2)
				.mapToObj(i -> new ElementBoundary(
						null,
						"test",
						"test-element",
						null,
						null,
						null,
						new LocationBoundery(32.1155f, 34.1432f),
						Collections.singletonMap("first-attribute", "test-attribute")))
				.map(msg -> this.restTemplate.postForObject(this.url + this.manager.getEmail(),
						    msg,
						    ElementBoundary.class))
				.collect(Collectors.toList());

		// WHEN I GET /element/{userEmail}
		ElementBoundary[] dataFromServer = this.restTemplate
							.getForObject(this.url + this.manager.getEmail(),
									ElementBoundary[].class);

		// THEN the server returns status 2xx
		// AND the response includes all elements in the database in any order
		assertThat(dataFromServer).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(databaseContent);

		// cleanup - delete all elements from database
		// do nothing
	}


	@Test
	public void testPostelementReturnselementDetailsInResponse() throws Exception {

		// GIVEN server is up // do nothing

		// WHEN I POST /elements/{managerMail} with a new element
		ElementBoundary elementToPost = new ElementBoundary(
					null,
					"test",
					"test-element",
					true,
					null,
				    null,
				    new LocationBoundery(32.1155f, 34.1432f),
				Collections.singletonMap("first-attribute", "test-attribute"));

		ElementBoundary elementFromServer = this.restTemplate
					.postForObject(this.url + this.manager.getEmail(),
						elementToPost,
						ElementBoundary.class);

		// THEN the server responds with the same element details, except for the
//	  timestamp,id and the created by 
		assertThat(elementFromServer).isEqualToComparingOnlyGivenFields(
				elementToPost,
				"type",
				"name",
				"active",
				"location",
				"elementAttributes");

		// cleanup - delete all elements from database // do nothing
	}

	@Test
	public void testPostElementCreatesAElementWithProperDetailsInTheDatabase() throws Exception {
		// GIVEN server is up
		// do nothing
		// GIVEN server is up // do nothing

		// WHEN I POST /elements/{managerMail} with a new element
		ElementBoundary elementToPost = new ElementBoundary(
				null,
				"test",
				"test-element",
				true,
				null,
				null,
				new LocationBoundery(32.11f, 34.15f),
				Collections.singletonMap("first-attribute", "test-attribute"));

		ElementBoundary elementFromServer = this.restTemplate
					.postForObject(this.url + this.manager.getEmail(),
							elementToPost,
							ElementBoundary.class);

		// THEN the database contains the new element details

		ElementBoundary elementFromDatabase = this.restTemplate.getForObject(
				this.url + this.manager.getEmail() + "/" + elementFromServer.getElementId()
				, ElementBoundary.class);

		assertThat(elementFromDatabase)
			.usingRecursiveComparison()
				.isEqualTo(elementFromServer);
	}

	@Test
	public void testGetAllElementsOnServerInitReturnsEmptyArray() throws Exception {
		// GIVEN server is up
		// do nothing

		// WHEN I GET /elements
		ElementBoundary[] allElements = this.restTemplate
				.getForObject(this.url + this.manager.getEmail(),
						ElementBoundary[].class);

		// THEN the server responds with status 2xx
		// AND the response body is an empty array
		assertThat(allElements).isEmpty();

	}


	@Test
	public void testUpdateElementsAndAttributesOfANewlyCreatedElementsAndValidateDatabaseIsUpdated() throws Exception {
		// GIVEN the server is up
		// AND the server contains an element with moreAttributess {"first":"toUpdate"} and name "test"and valid is true
		//and Id defined as {x}
		ElementBoundary newElement = this.restTemplate
				.postForObject(
						this.url + this.manager.getEmail(),
						new ElementBoundary(
								null,
								"test",
								"test-element",
								true,
								null,
								null,
								new LocationBoundery(32.11f, 34.15f),
								Collections.singletonMap("1", "toUpdate")),
									ElementBoundary.class);

		// WHEN I PUT /elements/{adminEmail}/{x} and {"name":"update", "valid":"false","moreAttributes":{"2","new"}}
		Map<String, Object> update = new HashMap<>();
		
		update.put("name", "update");
		update.put("valid", false);
		update.put("elementAttributes", Collections.singletonMap("2", "new"));
		
		this.restTemplate.put(this.url + this.manager.getEmail() + "/{x}",
				update,
				newElement.getElementId());

		// THEN the PUT operation is responded with status 2xx
		// AND the database is updated
		// AND the database was not updated with non modified fields
		assertThat(this.restTemplate
				.getForObject(this.url + this.manager.getEmail() + "/{id}",
						ElementBoundary.class,
						newElement.getElementId()))
						.extracting("elementId",
								"type",
								"name",
								"active",
								"createdTimestamp",
								"createdBy",
								"location",
								"elementAttributes")
						.containsExactly(newElement.getElementId(),
								newElement.getType(),
								"update", // elementName
								true,
								newElement.getCreatedTimestamp(),
								newElement.getCreatedBy(),
								newElement.getLocation(),
								Collections.singletonMap("2", "new"));
	}

	
	  @Test public void
	  testUpdateNameAttributeOfANewlyCreatedelementAndValidateDatabaseIsUpdated()
	  throws Exception { 
		  // GIVEN the server is up // AND the server contains an element with Name "test" and Id defined as {x} 
		  ElementBoundary newElement=
				  this.restTemplate
				  .postForObject(
						  this.url+this.manager.getEmail()
						  , new ElementBoundary(
								  null,
								  "test",
								  "test-element",
								  true,
								  null,
								  null,
								  new LocationBoundery(32.11f, 34.15f),
								  Collections.singletonMap("1", "attribute")),
						  			ElementBoundary.class);
	  
	  
	  
	  // WHEN I PUT /elements/{adminEmail}/{x} and {"name":"updated"} 
	Map<String, Object> update =
	  Collections.
	  	singletonMap("name", "updated");
		this.restTemplate.put(this.url + this.manager.getEmail() +
				"/{x}",
				update,
				newElement.getElementId());
	  
	  
	  // THEN the PUT operation is responded with status 2xx // AND the database is
	  //updated AND the database was not updated with non modified fields
	  assertThat(this.restTemplate.getForObject( this.url +this.manager.getEmail()+ "/{id}",
	  ElementBoundary.class, newElement
	  	.getElementId()))
	  	.extracting(
	  			"elementId",
	  			"type",
	  			"name",
	  			"active",
			    "createdTimestamp",
			    "createdBy",
			    "location",
				"elementAttributes")
	  	.containsExactly(
	  			newElement.getElementId(),
	  			newElement.getType(),
				"updated", // elementName
				true,
				newElement.getCreatedTimestamp(),
				newElement.getCreatedBy(),
				newElement.getLocation(),
				Collections.singletonMap("1", "attribute"));
	  }
	  
	  
	  @Test public void
	  testCreateTwoNewElementsAndBindThemWithParentChildDepandencyAndParentContainsExactlyOneAndCorrectChild()
	  throws Exception { 
		  // GIVEN the server is up // AND the server contains two elements
		  List<ElementBoundary> databaseContent = IntStream.range(0, 2)//List of the two elements
					.mapToObj(i -> new ElementBoundary(
							null,
							"test",
							"test-element",
							null,
							null,
							null,
							new LocationBoundery(32.1155f, 34.1432f),
							Collections.singletonMap("first-attribute", "test-attribute")))
					.map(msg -> this.restTemplate.postForObject(
							this.url + this.manager.getEmail(),
							msg,
							ElementBoundary.class))
					.collect(Collectors.toList());
		  // WHEN I PUT /acs/elements/{managerEmail}/{parentElementId}/children and {"id":"{id}"} and id is 
		  //databaseContent[1]
		  
		  ElementBoundary child = databaseContent.get(1);
		  this.restTemplate.put(
							this.url + this.manager.getEmail()+"/"+databaseContent.get(0).getElementId()+"/children"
							,Collections.singletonMap("id", child.getElementId())
							);
		  
		// THEN the PUT operation is responded with status 2xx // AND the database is
		  //updated AND the parent element include the child element 
		  ElementBoundary[] childrenFromDB 
		   = this.restTemplate
		   		.getForObject
				  (this.url + this.manager.getEmail()+"/"+databaseContent.get(0).getElementId()+"/children",
				  ElementBoundary[].class);
		
		  if(childrenFromDB.length!=1) 
			  throw new RuntimeException("there is not exactly one child");
		  
		  assertThat(childrenFromDB[0]).extracting("elementId", "type", "name", "active"
						  , "createdTimestamp", "createdBy", "location",
							"elementAttributes").containsExactly(child.getElementId(), child.getType()
									, child.getName(),
									child.getActive(), child.getCreatedTimestamp(), child.getCreatedBy(),
									child.getLocation(),  child.getElementAttributes());
			  
		  
	  }
	
	  @Test public void
	  testCreateTwoNewElementsAndBindThemWithParentChildDepandencyAndChildContainsExactlyOneAndCorrectParent()
	  throws Exception { 
		  // GIVEN the server is up // AND the server contains two elements
		  List<ElementBoundary> databaseContent = IntStream.range(0, 2)//List of the two elements
					.mapToObj(i -> new ElementBoundary(
								null,
								"parent"+i,
								"test-element",
								null,
								null,
								null,
								new LocationBoundery(32.1155f, 34.1432f),
							    Collections.singletonMap("first-attribute", "test-attribute")))
					.map(msg -> this.restTemplate.postForObject(
							this.url + this.manager.getEmail(),
							msg,
							ElementBoundary.class))
					.collect(Collectors.toList());
		  
		  // WHEN I PUT /acs/elements/{managerEmail}/{parentElementId}/children and {"id":"{id}"} and id is 
		  //databaseContent[1]
		  
		  ElementBoundary child =databaseContent.get(1);
		  ElementBoundary parent =databaseContent.get(0);
		  
		  this.restTemplate.put(
							this.url + this.manager.getEmail()+"/"+parent.getElementId()+"/children"
							,Collections.singletonMap("id", child.getElementId()));
		  
		// THEN the PUT operation is responded with status 2xx // AND the database is
		  //updated AND the parent element include the child element 
		  
		  ElementBoundary[] parentsFromDB 
		   =this.restTemplate.getForObject
				  (this.url + this.manager.getEmail() +"/"+child.getElementId()+"/parents",
				  ElementBoundary[].class);
		
		  if(parentsFromDB.length!=1)
			  throw new RuntimeException("there is not exactly one parent");
		  
		  assertThat(parentsFromDB[0]).extracting(
				  "elementId",
				  "type",
				  "name",
				  "active",
				  "createdTimestamp",
				  "createdBy",
				  "location",
				  "elementAttributes").containsExactly(
						  	parent.getElementId(),
						  	parent.getType(),
							parent.getName(),
							parent.getActive(),
							parent.getCreatedTimestamp(),
							parent.getCreatedBy(),
							parent.getLocation(),
							parent.getElementAttributes());  
	  }
	  
	  @Test public void
	  testCreateThreeNewElementsAndBindThemWithParentChildDepandencyAndChildContainsExactlyTwoCorrectParents()
	  throws Exception {
		  // GIVEN the server is up // AND the server contains two elements
		  List<ElementBoundary> databaseContent = IntStream.range(0, 3)//List of the two elements
					.mapToObj(i -> new ElementBoundary(
								null,
								"parent"+i,
								"test-element",
								null,
								null,
								null,
								new LocationBoundery(32.1155f, 34.1432f),
								Collections.singletonMap("first-attribute", "test-attribute")))
					.map(msg -> this.restTemplate.postForObject(
							this.url + this.manager.getEmail(),
							msg,
							ElementBoundary.class))
					.collect(Collectors.toList());
		  
		  // WHEN I PUT /acs/elements/{managerEmail}/{parentElementId}/children and {"id":"{id}"} and id is 
		  //databaseContent[1]
		  
		  ElementBoundary child =databaseContent.get(0);
		  ElementBoundary parentTwo =databaseContent.get(1);
		  ElementBoundary parentOne =databaseContent.get(2);
		  
		  //Bind ParentOne to child
		  
		  this.restTemplate.put(
							this.url + this.manager.getEmail()+"/"+parentOne.getElementId()+"/children"
							,Collections.singletonMap("id", child.getElementId())
							);
		  
		  //Bind ParentTwo to child
		  
		  this.restTemplate.put(
					this.url + this.manager.getEmail()+"/"+parentTwo.getElementId()+"/children"
					,Collections.singletonMap("id", child.getElementId()));
		  
		// THEN the PUT operation is responded with status 2xx // AND the database is
		  //updated AND the parent element include the child element 
		  
		  ElementBoundary[] parentsFromDB 
		   =this.restTemplate.getForObject
				  (this.url + this.manager.getEmail()+"/"+child.getElementId()+"/parents",
				  ElementBoundary[].class);
		
		  if(parentsFromDB.length!=2) 
			  throw new RuntimeException("there is not exactly Two parent");
		  
		  assertThat(parentsFromDB[1]).extracting(
				  	"elementId",
				  	"type",
				  	"name",
				  	"active",
					"createdTimestamp",
					"createdBy",
					"location",
					"elementAttributes")
		  .containsExactly(
				  parentOne.getElementId(),
				  parentOne.getType(),
				  parentOne.getName(),
				  parentOne.getActive(), 
				  parentOne.getCreatedTimestamp(),
				  parentOne.getCreatedBy(),
				  parentOne.getLocation(),
				  parentOne.getElementAttributes());
			  
		  assertThat(parentsFromDB[0]).extracting(
				  "elementId",
				  "type",
				  "name",
				  "active",
				  "createdTimestamp",
				  "createdBy",
				  "location",
				  "elementAttributes")
		  .containsExactly(
				  parentTwo.getElementId(),
				  parentTwo.getType(),
				  parentTwo.getName(),
				  parentTwo.getActive(), 
				  parentTwo.getCreatedTimestamp(),
				  parentTwo.getCreatedBy(),
				  parentTwo.getLocation(),
				  parentTwo.getElementAttributes());
	  }
	  
}