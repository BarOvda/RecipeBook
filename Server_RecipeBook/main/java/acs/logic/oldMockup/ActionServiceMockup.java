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

import acs.boundary.ActionBoundary;
import acs.boundary.Role;
import acs.data.ActionEntity;
import acs.data.ActionEntityConverter;
import acs.logic.ActionService;
import acs.logic.UserService;
import acs.logic.errormessages.MessageNotFoundExcetpion;


public class ActionServiceMockup implements ActionService {
	private Map<Long, ActionEntity> database;
	private ActionEntityConverter actionEntityConverter;
	private AtomicLong nextId;
	private UserService userService;

	public ActionServiceMockup() {
		System.err.println("spring initialized me - Action Mockup");
	}

	@Autowired
	public void setEntityConverter(ActionEntityConverter entityConverter, UserService userService) {
		this.actionEntityConverter = entityConverter;
		this.userService = userService;
	}

	@PostConstruct
	public void init() {
		this.database = Collections.synchronizedMap(new TreeMap<>());
		this.nextId = new AtomicLong(0L);
	}

	@Override
	public Object invokeAction(ActionBoundary action) {

		ActionEntity entity = this.actionEntityConverter.convertToEntity(action);

		Long newId = nextId.incrementAndGet();

		//entity.setId(newId);
		entity.setCreatedTimestamp(new Date());

		this.database.put(newId, entity);

		return this.actionEntityConverter.convertFromEntity(entity);
	}

	@Override
	public List<ActionBoundary> getAllActions(String adminEmail) {

		if (this.userService.login(adminEmail).getRole() == Role.ADMIN) {

			return this.database.values().stream().map(entity -> this.actionEntityConverter.convertFromEntity(entity))
					.collect(Collectors.toList());
		} else {
			throw new MessageNotFoundExcetpion(adminEmail + " is NOT a admin");
		}

	}

	@Override
	public void deleteAllActions(String adminEmail) {

		if (this.userService.login(adminEmail).getRole() == Role.ADMIN) {

			this.database.clear();

		} else {
			throw new MessageNotFoundExcetpion(adminEmail + " is NOT a admin");
		}
	}
}
