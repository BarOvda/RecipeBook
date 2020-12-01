package acs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.annotation.PostConstruct;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import acs.boundary.Role;
import acs.boundary.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class userTest {
	
	private RestTemplate restTemplate;
	private String url;
	private int port;
	private UserBoundary admin;

	@LocalServerPort
	public void setPort(int port) {
		
		this.port = port;
		
	}
	
	@PostConstruct
	public void init() {
		
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/acs/users";
		
	}
	
	@BeforeEach
	public void setup() {
		// insert admin user
		admin = this.restTemplate.postForObject(this.url,
				new UserBoundary(
						"lir@gmail.com",
						"lir",
						":)",
						Role.ADMIN),
				UserBoundary.class);
	}
	
	@AfterEach 
	public void teardown() {
		this.restTemplate.delete("http://localhost:" + port + "/acs/admin/users/" + admin.getEmail());
		
	}
	
	@Test
	public void testContext() {
		
	}
	
	@Test
	public void testPostUserCreatesAUserWithProperDetailsInTheDatabase() throws Exception{
		// GIVEN server is up
		// do nothing

		// WHEN I POST acs/user with a new user
		UserBoundary userToPost
			= new UserBoundary(
					"aviv@gmail.com", 
					"test", 
					":0", 
					Role.MANAGER);
				

		  UserBoundary userFromServer = 
		  this.restTemplate
			.postForObject(
					this.url, 
					userToPost, 
					UserBoundary.class);
		
		// THEN the database contains the new user details
		  UserBoundary userFromDatabase = 
		  this.restTemplate
			.getForObject(
					this.url +"/login" + "/{email}" , 			
					UserBoundary.class, 					
					userFromServer.getEmail());
		
		assertThat(userFromDatabase)
			.usingRecursiveComparison()
			.isEqualTo(userFromServer);
	}
	
	@Test
	public void testGetAllUsersWithDatabaseContainig3UsersAndAdminReturnsAllUsersInTheDatabase() throws Exception{
		// GIVEN server is up
		// AND the database contains 3 users
		List<UserBoundary> databaseContent = 
		   IntStream.range(0, 3) // Stream<Integer>
			.mapToObj(i->
					new UserBoundary(		
							i +"@walla.com", 
							"User #" + i + "Test",
							"Avatar #" + i + "Test",
							Role.MANAGER) 
			) // Stream<UserBoundary>
			.map(msg->this.restTemplate
					.postForObject(this.url, 
							msg, 
							UserBoundary.class)) // Stream<UserBoundary>
			.collect(Collectors.toList()); // List<UserBoundary>
			
		//add admin to the stream
		databaseContent.add(admin);
		
		// WHEN I GET acs/admin/users
		UserBoundary[] dataFromServer
			= this.restTemplate
				.getForObject("http://localhost:" + port + "/acs/admin/users/"+ admin.getEmail(), 
						UserBoundary[].class);
		
		
		// THEN the server returns status 2xx
		// AND the response includes all users in the database 
		assertThat(dataFromServer)
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactlyInAnyOrderElementsOf(databaseContent);
		
	}
	
	
	@Test
	public void testUpdateNameAttributeOfANewlyCreatedUserAndValidateDatabaseIsUpdated() throws Exception {
		// GIVEN the server is up
		// AND the server contains a user with name "aviv" and email defined
		UserBoundary newUser = 
		  this.restTemplate
			.postForObject(this.url, 
					new UserBoundary(
							"aviv@c.com", 
							"aviv", 
							":0", 
							Role.PLAYER), 					
					UserBoundary.class);
			
			
		// WHEN I PUT /users/{email} and {"name":"yevgeni"}
		Map<String, Object> update = Collections.singletonMap("username", "yevgeni");
		this.restTemplate
			.put(this.url + "/{email}", update, newUser.getEmail());
		
		
		// THEN the PUT operation is responded with status 2xx
		// AND the database is updated
		// AND the database was not updated with non modified fields
		assertThat(this.restTemplate
				.getForObject(
						this.url +"/login" + "/{email}", UserBoundary.class, newUser.getEmail()))
			.extracting(
					"email", 
					"username", 
					"avatar", 
					"role")
			.containsExactly(
					newUser.getEmail(), 
					"yevgeni",
					newUser.getAvatar(),
					newUser.getRole());
	}
	
	
	@Test
	public void testPostUserReturnsUserDetailsInResponse() throws Exception{
		// GIVEN server is up
		// do nothing

		// WHEN I POST acs/users with a new user
		UserBoundary userToPost
			= new UserBoundary(
					"aviv@gmail.com", 
					"test", 
					":0", 
					Role.PLAYER);
		
		UserBoundary userFromServer = 
		  this.restTemplate
			.postForObject(
					this.url, 
					userToPost, 
					UserBoundary.class);
		
		// THEN the server responds with the same user details
		assertThat(userFromServer)
		.usingRecursiveComparison()
		.isEqualTo(userToPost);

		
		// cleanup - delete all users from database
		// do nothing
	}
	
	@Test
	public void testGetAllUsersOnServerInitReturnsOnlyAdmin() throws Exception{
		// GIVEN server is up
		// do nothing
		
		// WHEN I GET acs/admin/users
		UserBoundary[] allUsers = 
		  this.restTemplate
			.getForObject(
					"http://localhost:" + port + "/acs/admin/users/"+ admin.getEmail(), 
					UserBoundary[].class);
		
		// THEN the server responds with status 2xx
		// AND the response body is only the admin
		assertThat(allUsers)
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactly(admin);
		
	}
	
}
