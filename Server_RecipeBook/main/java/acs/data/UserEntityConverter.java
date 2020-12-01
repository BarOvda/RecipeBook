package acs.data;

import org.springframework.stereotype.Component;

import acs.boundary.Role;
import acs.boundary.UserBoundary;
import acs.data.UserEntity;
import acs.data.UserRole;

@Component
public class UserEntityConverter {
	
	//convert boundary to entity
	public UserEntity converToEntity(UserBoundary userBoundary) {
		
		UserEntity entity = new UserEntity();
		
		entity.setEmail(userBoundary.getEmail());
		entity.setUsername(userBoundary.getUsername());
		entity.setAvatar(userBoundary.getAvatar());
		
		if(userBoundary.getRole() != null) {
			entity.setRole(UserRole.valueOf(userBoundary.getRole().name().toLowerCase()));
		}
		
		return entity;
	}

	//convert entity to boundary
	public UserBoundary convertToBoundary(UserEntity entity) {
		
		UserBoundary boundary = new UserBoundary();
		
		boundary.setEmail(entity.getEmail());
		boundary.setUsername(entity.getUsername());
		boundary.setAvatar(entity.getAvatar());
		
		
		if(entity.getRole() != null) {
			boundary.setRole(Role.valueOf(entity.getRole().name().toUpperCase()));
		}
		
		return boundary;
	}

}
