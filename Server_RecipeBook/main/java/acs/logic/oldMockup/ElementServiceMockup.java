package acs.logic.oldMockup;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acs.boundary.ElementBoundary;
import acs.boundary.Role;
import acs.data.ElementEntity;
import acs.data.ElementEntityConverter;
import acs.logic.ElementService;
import acs.logic.UserService;
import acs.logic.errormessages.MessageNotFoundExcetpion;


public class ElementServiceMockup implements ElementService {
	private Map<Long, ElementEntity> database;
	private ElementEntityConverter elementEntityConverter;
	private AtomicLong nextId;
	private UserService userService;

	public ElementServiceMockup() {
		System.err.println("spring initialized me - Element Mockup");
	}

	@Autowired
	public void setEntityConverter(ElementEntityConverter entityConverter, UserService userService) {
		this.elementEntityConverter = entityConverter;
		this.userService = userService;
	}

	@PostConstruct
	public void init() {
		this.database = Collections.synchronizedMap(new TreeMap<>());
		this.nextId = new AtomicLong(0L);
	}

	@Override
	public ElementBoundary create(String managerEmail, ElementBoundary element) {

		if (this.userService.login(managerEmail).getRole() != Role.MANAGER)
			throw new MessageNotFoundExcetpion(managerEmail + " is NOT a manager ");

		Long newId = nextId.incrementAndGet();
		ElementEntity entity = this.elementEntityConverter.converToEntity(element);

		entity.setCreatedTimestamp(new Date());

		this.database.put(newId, entity);

		return this.elementEntityConverter.convertToBoundary(entity);
	}

	@Override
	public ElementBoundary update(String managerEmail, String elementID, ElementBoundary update) {
		if (this.userService.login(managerEmail).getRole() != Role.MANAGER
				&& this.userService.login(managerEmail).getRole() != Role.ADMIN)
			throw new MessageNotFoundExcetpion(managerEmail + " is NOT a Manager ");
		ElementBoundary existing = this.getSpecificElement(managerEmail, elementID);
		boolean dirty = false;

		if (update.getCreatedBy() != null || update.getElementId() != null || update.getCreatedTimestamp() != null) {
			throw new RuntimeException("Invalid update request");
		}

		if (update.getElementAttributes() != null) {
			existing.setElementAttributes(update.getElementAttributes());
			dirty = true;
		}

		if (update.getLocation() != null) {
			existing.setLocation(update.getLocation());
			dirty = true;
		}

		if (update.getType() != null) {
			existing.setType(update.getType());
			dirty = true;
		}

		if (update.getName() != null) {
			existing.setName(update.getName());
			dirty = true;
		}

		if (dirty) {
			this.database.put(this.elementEntityConverter.toEntityId(elementID),
					this.elementEntityConverter.converToEntity(existing));
		}

		return existing;
	}

	@Override
	public ElementBoundary getSpecificElement(String userEmail, String elementID) {
		if (this.userService.login(userEmail).getRole() == Role.PLAYER
				|| this.userService.login(userEmail).getRole() == Role.MANAGER
				|| this.userService.login(userEmail).getRole() == Role.ADMIN) {
			ElementEntity entity = this.database.get(this.elementEntityConverter.toEntityId(elementID));

			if (entity != null) {
				return this.elementEntityConverter.convertToBoundary(entity);
			} else {
				throw new MessageNotFoundExcetpion("could not find Element for id: " + elementID);
			}
		} else {
			throw new MessageNotFoundExcetpion(userEmail + " is NOT a user ");
		}
	}

	@Override
	public List<ElementBoundary> getAllElements(String userEmail) {
		if (this.userService.login(userEmail).getRole() == Role.PLAYER
				|| this.userService.login(userEmail).getRole() == Role.MANAGER
				|| this.userService.login(userEmail).getRole() == Role.ADMIN) {
			
		return this.database.values().stream().map(entity -> this.elementEntityConverter.convertToBoundary(entity))
				.collect(Collectors.toList());
		}else {
			throw new MessageNotFoundExcetpion(userEmail + " is NOT a user ");
		}
	}

	@Override
	public void deleteAllElements(String adminEmail) {
		if (this.userService.login(adminEmail).getRole() != Role.ADMIN)
			throw new MessageNotFoundExcetpion(adminEmail + " is NOT a admin ");
		this.database.clear();

	}

}
