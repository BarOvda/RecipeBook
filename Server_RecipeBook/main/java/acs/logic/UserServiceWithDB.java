package acs.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundary.UserBoundary;
import acs.dal.ElementDao;
import acs.dal.UserDao;
import acs.data.ElementEntityConverter;
import acs.data.UserEntity;
import acs.data.UserEntityConverter;
import acs.data.UserRole;
import acs.logic.errormessages.BadPermissionExcetpion;
import acs.logic.errormessages.MessageNotFoundExcetpion;

@Service
public class UserServiceWithDB implements EnchancedUserService{
	
	private UserDao userDao;
	private UserEntityConverter userEntityConverter;

	
	@Autowired
	public UserServiceWithDB(UserDao userDao) {
		
		System.err.println("spring initialized me - UserMockup DB");
		this.userDao = userDao;

	}

	
	@Autowired
	public void setEntityConverter(UserEntityConverter entityConverter) {
		
		this.userEntityConverter = entityConverter;

	}

	
	@Override
	@Transactional
	public UserBoundary createUser(UserBoundary input) {

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);

		if (!pat.matcher(input.getEmail()).matches() || input.getEmail() == null) {
			throw new MessageNotFoundExcetpion("EMAIL is NOT valid!");
		}

		if (input.getUsername() == null || input.getUsername().isEmpty()) {
			throw new MessageNotFoundExcetpion("USERNAME does NOT exist!");
		}

		if (input.getAvatar() == null || input.getAvatar().isEmpty()) {
			throw new MessageNotFoundExcetpion("AVATAR is INVALID!");
		}

		UserEntity entity = this.userEntityConverter.converToEntity(input);

		entity = this.userDao.save(entity);

		return this.userEntityConverter.convertToBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userEmail) {

		Optional<UserEntity> entity = this.userDao.findById(userEmail);

		if (entity.isPresent()) {
			return this.userEntityConverter.convertToBoundary(entity.get());
		} else {
			throw new MessageNotFoundExcetpion("could not find this USER with this EMAIL: " + userEmail);
		}
	}

	@Override
	@Transactional
	public UserBoundary updateUserDetails(String userEmail, UserBoundary update) {
		
		UserBoundary existing = this.login(userEmail);

		if (update.getUsername() != null) {
			existing.setUsername(update.getUsername());
		}

		if (update.getAvatar() != null) {
			existing.setAvatar(update.getAvatar());
		}

		if (update.getRole() != null) {
			existing.setRole(update.getRole());
		}

		this.userDao.save(this.userEntityConverter.converToEntity(existing));

		return existing;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUser(String adminEmail) {

		UserEntity user = this.userDao.findById(adminEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + adminEmail));

		if (user.getRole() == UserRole.admin) {
			List<UserBoundary> userList = new ArrayList<UserBoundary>();

			Iterable<UserEntity> content = this.userDao.findAll();
			
			for (UserEntity users : content) {
				
				userList.add(this.userEntityConverter.convertToBoundary(users));
				
			}
			return userList;
		}
		throw new BadPermissionExcetpion("You do not permissions to access this function");
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUser(String adminEmail, int size, int page) {

		UserEntity user = this.userDao.findById(adminEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + adminEmail));

		if (user.getRole() != UserRole.admin) {
			throw new BadPermissionExcetpion("You do not permissions to access this function");
		}

		return this.userDao
				.findAll(PageRequest.of(page,
						size,
						Direction.ASC,
						"email"))
				.getContent()
				.stream()
				.map(this.userEntityConverter::convertToBoundary)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteAllUsers(String adminEmail) {

		UserEntity user = this.userDao.findById(adminEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + adminEmail));

		if (user.getRole() != UserRole.admin) {
			throw new BadPermissionExcetpion("You do not permissions to access this function");
		}

		this.userDao.deleteAll();
	}

}
