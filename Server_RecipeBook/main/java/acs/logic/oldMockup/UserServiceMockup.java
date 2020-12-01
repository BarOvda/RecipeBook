package acs.logic.oldMockup;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acs.boundary.UserBoundary;
import acs.data.UserEntity;
import acs.data.UserEntityConverter;
import acs.data.UserRole;
import acs.logic.UserService;
import acs.logic.errormessages.MessageNotFoundExcetpion;


public class UserServiceMockup implements UserService {
	private Map<String, UserEntity> database;
	private UserEntityConverter userEntityConverter;

	public UserServiceMockup() {
		System.err.println("spring initialized me - UserMockup");
	}

	@Autowired
	public void setEntityConverter(UserEntityConverter entityConverter) {
		this.userEntityConverter = entityConverter;
	}

	@PostConstruct
	public void init() {
		this.database = Collections.synchronizedMap(new TreeMap<>());
	}

	@Override
	public UserBoundary createUser(UserBoundary input) {
		
		if(input.getEmail().isEmpty()) {
			throw new MessageNotFoundExcetpion("No email was entered there!");
		}

		UserEntity entity = this.userEntityConverter.converToEntity(input);

		database.put(entity.getEmail(), entity);

		return this.userEntityConverter.convertToBoundary(entity);
	}

	@Override
	public UserBoundary login(String userEmail) {

		UserEntity entity = this.database.get(userEmail);

		if (entity != null) {
			return this.userEntityConverter.convertToBoundary(entity);
		} else {
			throw new MessageNotFoundExcetpion("could not find this USER with this EMAIL: " + userEmail);
		}
	}

	@Override
	public UserBoundary updateUserDetails(String userEmail, UserBoundary update) {
		boolean dirty = false;
		UserBoundary existing = this.login(userEmail);

		if (update.getUsername() != null) {
			existing.setUsername(update.getUsername());
			dirty = true;
		}

		if (update.getAvatar() != null) {
			existing.setAvatar(update.getAvatar());
			dirty = true;
		}

		if (update.getRole() != null) {
			existing.setRole(update.getRole());
			dirty = true;
		}

		if (dirty) {
			this.database.put(userEmail, this.userEntityConverter.converToEntity(existing));
		}

		return existing;
	}

	@Override
	public List<UserBoundary> getAllUser(String adminEmail) {

		UserEntity entityAdmin = this.database.get(adminEmail);

		if (entityAdmin.getRole() == UserRole.admin) {
			
			return this.database.values().stream()
					.map(entity -> this.userEntityConverter.convertToBoundary(entity))
					.collect(Collectors.toList());
		}

		else {
			throw new MessageNotFoundExcetpion(adminEmail + " is NOT a admin");
		}

	}

	@Override
	public void deleteAllUsers(String adminEmail) {

		UserEntity entityAdmin = this.database.get(adminEmail);

		if (entityAdmin.getRole() == UserRole.admin) {
			database.values().removeIf(entity -> entity.getRole() == UserRole.player);
			}
		
		else {
			throw new MessageNotFoundExcetpion(adminEmail + "is NOT a admin");
		}
	}

}
