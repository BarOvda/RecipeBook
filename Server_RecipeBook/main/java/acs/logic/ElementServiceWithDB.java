package acs.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import acs.boundary.ElementBoundary;
import acs.dal.ElementDao;
import acs.dal.LastIdValue;
import acs.dal.LastValueDao;
import acs.dal.UserDao;
import acs.data.ElementEntity;
import acs.data.ElementEntityConverter;
import acs.data.UserEntity;
import acs.data.UserRole;
import acs.data.aidClass.EntityEmail;
import acs.logic.errormessages.BadPermissionExcetpion;
import acs.logic.errormessages.MessageNotFoundExcetpion;

@Service
public class ElementServiceWithDB implements EnchancedElementService{
	
	private ElementEntityConverter elementEntityConverter;
	private ElementDao elementDao;
	private LastValueDao lastValueDao;
	private UserDao userDao;


	@Autowired
	public ElementServiceWithDB(ElementDao elementDao, LastValueDao lastValueDao, UserDao userDao) {
		
		System.err.println("spring initialized me - Element Mockup DB");
		this.elementDao = elementDao;
		this.lastValueDao = lastValueDao;
		this.userDao = userDao;

	}

	
	@Autowired
	public void setEntityConverter(ElementEntityConverter entityConverter, UserService userService) {
		
		this.elementEntityConverter = entityConverter;

	}

	
	@Override
	@Transactional
	public ElementBoundary create(String managerEmail, ElementBoundary element) {
		
		boolean managerBool = false;
		boolean adminBool = false;
		
		UserEntity user = this.userDao.findById(managerEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + managerEmail));
		
		if(user.getRole() == UserRole.admin) {
			adminBool = true;
			managerBool = true;
			
			user.setRole(UserRole.manager);
		}
		
		if(user.getRole() == UserRole.player) {
			managerBool = true;
			
			user.setRole(UserRole.manager);
		}
		
		if (user.getRole() != UserRole.manager) {
			throw new BadPermissionExcetpion("You do not permissions to access this function");
		}

		if (element.getType() == null || element.getType().isEmpty()) {
			throw new MessageNotFoundExcetpion("The TYPE of element is INVALID !");
		}

		if (element.getName() == null || element.getName().isEmpty()) {
			throw new MessageNotFoundExcetpion("The name of the ELEMENT does NOT exist!");
		}

		LastIdValue idValue = this.lastValueDao.save(new LastIdValue());
		ElementEntity entity = this.elementEntityConverter.converToEntity(element);

		entity.setCreatedTimestamp(new Date());
		entity.setElementId(idValue.getLastId());
		entity.setCreatedBy(new EntityEmail(managerEmail));

		this.lastValueDao.delete(idValue);

		entity = this.elementDao.save(entity);
		
		if(managerBool == true) {
			if(adminBool == true) {
				user.setRole(UserRole.admin);
				adminBool = false;
			}else {
				user.setRole(UserRole.player);
			}
		}
	
		return this.elementEntityConverter.convertToBoundary(entity);
	}

	@Override
	@Transactional
	public ElementBoundary update(String managerEmail, String elementID, ElementBoundary update) {
		
		boolean managerBool = false;
		boolean adminBool = false;
		ObjectMapper jackson = new ObjectMapper();
		
		UserEntity user = this.userDao.findById(managerEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + managerEmail));
		
		if(user.getRole() == UserRole.admin) {
			adminBool = true;
			managerBool = true;
			
			user.setRole(UserRole.manager);
		}
		
		if(user.getRole() == UserRole.player) {
			managerBool = true;
			
			user.setRole(UserRole.manager);
		}

		if (user.getRole() != UserRole.manager) {
			throw new BadPermissionExcetpion("You do not permissions to access this function");
		}
		
		ElementEntity exiElementEntity = this.elementDao.findById(Long.parseLong(elementID))
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not findelement with id: " + elementID));
		
		if (update.getElementAttributes() != null) {
			
			try {
				exiElementEntity.setElementAttributes
					(jackson.writeValueAsString
							(update.getElementAttributes()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		if (update.getLocation() != null) {
			exiElementEntity.setLocation(update.getLocation());
		}
		
		if (update.getType() != null) {
			exiElementEntity.setType(update.getType());
		}

		if (update.getName() != null) {
			exiElementEntity.setName(update.getName());
		}

		if (update.getActive() != null) {
			exiElementEntity.setActive(update.getActive());
		}

		this.elementDao.save(exiElementEntity);
		
		if(managerBool == true) {
			if(adminBool == true) {
				user.setRole(UserRole.admin);
				adminBool = false;
			}else {
				user.setRole(UserRole.player);
			}
		}
		
		return this.elementEntityConverter.convertToBoundary(exiElementEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary getSpecificElement(String userEmail, String elementID) {

		UserEntity user = this.userDao.findById(userEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find uer with email: " + userEmail));

		if (user.getRole() == UserRole.manager) {
			Optional<ElementEntity> entity = this.elementDao.findById(Long.parseLong(elementID));

			if (entity.isPresent()) {

				return this.elementEntityConverter.convertToBoundary(entity.get());
			} else {

				throw new MessageNotFoundExcetpion("could not find element for id: " + elementID);
			}
		}

		if (user.getRole() == UserRole.player) {

			ElementEntity entity = this.elementDao.findByElementIdAndActive(Long.parseLong(elementID), true)
					.orElseThrow(() -> new MessageNotFoundExcetpion("could not find element for id: " + elementID));

			return this.elementEntityConverter.convertToBoundary(entity);

		}
		throw new BadPermissionExcetpion("You do not permissions to access this function");
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAllElements(String userEmail) {

		List<ElementBoundary> allElement = new ArrayList<ElementBoundary>();

		Iterable<ElementEntity> content = this.elementDao.findAll();

		for (ElementEntity element : content) {
			
			allElement.add(this.elementEntityConverter.convertToBoundary(element));
		}

		return allElement;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAllElements(String userEmail, int size, int page) {

		UserEntity user = this.userDao.findById(userEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + userEmail));

		if (user.getRole() == UserRole.manager) {

			return this.elementDao
					.findAll(PageRequest.of(page,
							size,
							Direction.ASC,
							"elementId"))
					.getContent()
					.stream()
					.map(this.elementEntityConverter::convertToBoundary)
					.collect(Collectors.toList());

		}

		if (user.getRole() == UserRole.player) {

			return this.elementDao
					.findAllByActive(true,
							PageRequest.of(page,
									size,
									Direction.ASC,
									"elementId"))
					.stream()
					.map(this.elementEntityConverter::convertToBoundary)
					.collect(Collectors.toList());

		}
		throw new BadPermissionExcetpion("You do not permissions to access this function");
	}

	@Override
	@Transactional
	public void deleteAllElements(String adminEmail) {
		UserEntity user = this.userDao.findById(adminEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + adminEmail));

		if (user.getRole() != UserRole.admin) {
			throw new BadPermissionExcetpion("You do not permissions to access this function");
		}

		this.elementDao.deleteAll();
	}

	@Override
	@Transactional
	public void addChildElementToElement(String managerEmail, String parentElementId, String id) {
		
		boolean managerBool = false;
		boolean adminBool = false;
		
		UserEntity user = this.userDao.findById(managerEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + managerEmail));
		
		if(user.getRole() == UserRole.admin) {
			adminBool = true;
			managerBool = true;
			
			user.setRole(UserRole.manager);
		}
		
		if(user.getRole() == UserRole.player) {
			managerBool = true;
			user.setRole(UserRole.manager);
		}
		
		if (user.getRole() != UserRole.manager) {
			throw new BadPermissionExcetpion("You do not permissions to access this function");
		}

		if (parentElementId != null && parentElementId.equals(id)) {
			throw new RuntimeException("parent element CANNOT respond itself!");
		}

		ElementEntity parent = this.elementDao.findById(this.elementEntityConverter.toEntityId(parentElementId))
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find element for id: " + parentElementId));

		ElementEntity child = this.elementDao.findById(this.elementEntityConverter.toEntityId(id))
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find element for id: " + id));

		parent.addChildren(child);
		
		this.elementDao.save(parent);
		
		if(managerBool == true) {
			if(adminBool == true) {
				user.setRole(UserRole.admin);
				adminBool = false;
			}
			else {
				user.setRole(UserRole.player);
			}
			managerBool = false;
		}
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public Collection<ElementBoundary> getParents(String userEmail, String childId, int size, int page) {
	
		UserEntity user = this.userDao.findById(userEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + userEmail));

		this.elementDao.findById(this.elementEntityConverter.toEntityId(childId))
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find child for thid id: " + childId));

		if (user.getRole() == UserRole.manager) {
			
			return this.elementDao
					.findAllByChildren_elementId(this.elementEntityConverter
							.toEntityId(childId),
							PageRequest.of(page,
									size,
									Direction.ASC,
									"elementId"))
					.stream()
					.map(this.elementEntityConverter::convertToBoundary)
					.collect(Collectors.toList());
		}

		if (user.getRole() == UserRole.player) {
			
			return this.elementDao
					.findAllByChildren_elementIdAndActive(this.elementEntityConverter.toEntityId(childId),
							true,
							PageRequest.of(page,
									size,
									Direction.ASC,
									"elementId"))
					.stream()
					.map(this.elementEntityConverter::convertToBoundary)
					.collect(Collectors.toList());
		}

		throw new BadPermissionExcetpion("You do not permissions to access this function");
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<ElementBoundary> getChildren(String userEmail, String parentId, int size, int page) {

		UserEntity user = this.userDao.findById(userEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + userEmail));
		
		this.elementDao.findById(this.elementEntityConverter.toEntityId(parentId))
		.orElseThrow(()-> new MessageNotFoundExcetpion("could not find parents for this id: " + parentId));

		if (user.getRole() == UserRole.manager) {
			
			return this.elementDao
					.findAllByParents_elementId(this.elementEntityConverter.toEntityId(parentId),
							PageRequest.of(page,
									size,
									Direction.ASC,
									"elementId"))
					.stream()
					.map(this.elementEntityConverter::convertToBoundary)
					.collect(Collectors.toList());
		}

		if (user.getRole() == UserRole.player) {
			
			return this.elementDao
					.findAllByParents_elementIdAndActive(this.elementEntityConverter.toEntityId(parentId),
							true,
							PageRequest.of(page,
									size,
									Direction.ASC,
									"elementId"))
					.stream()
					.map(this.elementEntityConverter::convertToBoundary)
					.collect(Collectors.toList());
		}

		throw new BadPermissionExcetpion("You do not permissions to access this function");
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getElementsByName(String userEmail, String name, int size, int page) {

		UserEntity user = this.userDao.findById(userEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + userEmail));

		if (user.getRole() == UserRole.manager) {
			
			return this.elementDao
					.findAllByName(name, PageRequest.of(page,
							size,
							Direction.ASC,
							"elementId"))
					.stream()
					.map(this.elementEntityConverter::convertToBoundary)
					.collect(Collectors.toList());
		}

		if (user.getRole() == UserRole.player) {
			
			return this.elementDao
					.findAllByNameAndActive(name,
							true,
							PageRequest.of(page,
									size,
									Direction.ASC,
									"elementId"))
					.stream()
					.map(this.elementEntityConverter::convertToBoundary)
					.collect(Collectors.toList());
		}

		throw new BadPermissionExcetpion("You do not permissions to access this function");
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getElementsByType(String userEmail, String type, int size, int page) {

		UserEntity user = this.userDao.findById(userEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + userEmail));

		if (user.getRole() == UserRole.manager) {
			
			return this.elementDao
					.findAllByType(type,
							PageRequest.of(page,
									size,
									Direction.ASC,
									"elementId"))
					.stream()
					.map(this.elementEntityConverter::convertToBoundary)
					.collect(Collectors.toList());
		}

		if (user.getRole() == UserRole.player) {
			
			return this.elementDao
					.findAllByTypeAndActive(type,
							true,
							PageRequest.of(page,
									size,
									Direction.ASC,
									"elementId"))
					.stream()
					.map(this.elementEntityConverter::convertToBoundary)
					.collect(Collectors.toList());
		}

		throw new BadPermissionExcetpion("You do not permissions to access this function");
	}
}
