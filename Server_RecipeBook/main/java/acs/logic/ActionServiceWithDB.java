package acs.logic;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundary.ActionBoundary;
import acs.boundary.ElementBoundary;
import acs.boundary.UserBoundary;
import acs.dal.ActionDao;
import acs.dal.ElementDao;
import acs.dal.LastIdValue;
import acs.dal.LastValueDao;
import acs.dal.UserDao;
import acs.data.ActionEntity;
import acs.data.ActionEntityConverter;
import acs.data.ElementEntityConverter;
import acs.data.UserEntity;
import acs.data.UserEntityConverter;
import acs.data.UserRole;
import acs.logic.errormessages.BadPermissionExcetpion;
import acs.logic.errormessages.MessageNotFoundExcetpion;
import javassist.NotFoundException;

@Service
public class ActionServiceWithDB implements EnchancedActionService {
	
	private ActionEntityConverter actionEntityConverter;
	private ElementEntityConverter elementEntityConverter;
	private ActionDao actionDao;
	private UserDao userDao;
	private LastValueDao lastValueDao;
	private ElementDao elementDao;
	private UserEntityConverter userEntityConverter;

	
	@Autowired
	public ActionServiceWithDB(ElementDao elementDao, ActionDao actionDao, LastValueDao lastValueDao, UserDao userDao) {
		
		System.err.println("spring initialized me - Action Mockup DB");
		this.actionDao = actionDao;
		this.lastValueDao = lastValueDao;
		this.userDao = userDao;
		this.elementDao = elementDao;
		
	}

	
	@Autowired
	public void setEntityConverter(ActionEntityConverter entityConverter,UserEntityConverter userEntityConverter, UserService userService,
			ElementEntityConverter elementConverter) {
		
		this.actionEntityConverter = entityConverter;
		this.userEntityConverter = userEntityConverter;
		this.elementEntityConverter=elementConverter;
		
	}
	

	@Override
	@Transactional
	public Object invokeAction(ActionBoundary action) {
		
		Object pageNumObj = null;
		
		UserEntity user = this.userDao.findById(action.getInvokedBy().getEmail())
				.orElseThrow(() -> new MessageNotFoundExcetpion(
						"could not find user with email: " + action.getInvokedBy().getEmail()));

		if (user.getRole() != UserRole.player) {
			throw new BadPermissionExcetpion("You do not permissions to access this function");
		}

		this.elementDao.findByElementIdAndActive(Long.parseLong(action.getElement().getElementId()), true)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find element "
						+ action.getElement().getElementId() + " or could not find actvie valuable true"));

		if (action.getType() == null || action.getType().isEmpty()) {
			throw new MessageNotFoundExcetpion("The TYPE of action is INVALID !");
		}

		if (action.getInvokedBy().getEmail() == null || action.getInvokedBy().getEmail().isEmpty()
				|| action.getElement().getElementId() == null ||
				action.getElement().getElementId().isEmpty())
		 {
			throw new MessageNotFoundExcetpion("The EMAIL or ELEMENT is INVALID !");
		}
		
		if(action.getType().compareTo("follow")!=0)
			pageNumObj = checkPage(action);

		switch (action.getType()) {
			
		case "getAllUsersRecpies":
			
			
			return getUsersRecipes("recipe", action.getInvokedBy().getEmail(), 10, (Integer) pageNumObj);
			
		case "searchUser":
			
			
			return getUsersSearch((String)action.getactionAttributes().get("username"), 10, (Integer) pageNumObj);
			
		case "searchRecipe":
			
			
			return getAllRecipeLike("recipe", (String)action.getactionAttributes().get("name"), 10, (Integer) pageNumObj );
		
		case "getFeed":
			
			
			return getFeed(action.getInvokedBy().getEmail(),10,(Integer) pageNumObj);
	
		case "getAllFollowedByMe":


			return getAllFollowedByMe("follow", action.getInvokedBy().getEmail(), 10, (Integer) pageNumObj);
			
		case "follow":
			
			ActionEntity actionEntity = saveTheAction(action);
			return this.actionEntityConverter.convertFromEntity(actionEntity);
		
		default:
			
			throw new MessageNotFoundExcetpion("This action type dose not exist");
			
		}
		
	}
	
	private Object checkPage(ActionBoundary action) {
		
		Object pageNumObj = action.getactionAttributes().get("page");
		
		if(!(pageNumObj instanceof Integer)) {
			throw new MessageNotFoundExcetpion("illegal page number - not an integer");
		}
		
		return pageNumObj;
		
	}
	
	private ActionEntity saveTheAction(ActionBoundary action) {

		LastIdValue idValue = this.lastValueDao.save(new LastIdValue());
		ActionEntity actionEntity = this.actionEntityConverter.convertToEntity(action);

		actionEntity.setCreatedTimestamp(new Date());
		actionEntity.setActionId(idValue.getLastId());
		
		this.lastValueDao.delete(idValue);

		actionEntity = this.actionDao.save(actionEntity);
	
		return actionEntity;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminEmail) {

		List<ActionBoundary> allList = new ArrayList<>();
		Iterable<ActionEntity> content = this.actionDao.findAll();

		for (ActionEntity action : content) {
			allList.add(this.actionEntityConverter.convertFromEntity(action));
		}

		return allList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionBoundary> getAllActions(String adminEmail, int size, int page) {

		UserEntity user = this.userDao.findById(adminEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + adminEmail));

		if (user.getRole() != UserRole.admin) {
			throw new BadPermissionExcetpion("You do not permissions to access this function");
		}

		return this.actionDao
				.findAll(PageRequest
						.of(page, size, Direction.ASC, "actionId"))
				.getContent()
				.stream()
				.map(this.actionEntityConverter::convertFromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAllActions(String adminEmail) {

		UserEntity user = this.userDao.findById(adminEmail)
				.orElseThrow(() -> new MessageNotFoundExcetpion("could not find user with email: " + adminEmail));

		if (user.getRole() != UserRole.admin) {
			throw new BadPermissionExcetpion("You do not permissions to access this function");
		}

		this.actionDao
			.deleteAll();

	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllFollowedByMe(
			String type,String invokedBy, int size, int page) {
		
		List<UserBoundary> followed = new ArrayList<>();
		
		List<ActionBoundary> followActions = this.actionDao
				.findAllByTypeAndInvokedBy_email(
						type,
						invokedBy,
						PageRequest.of(page, size, Direction.ASC, "createdTimestamp"))
				.stream()
				.map(this.actionEntityConverter::convertFromEntity)
				.collect(Collectors.toList());
		
		for(ActionBoundary action : followActions){
			
			Optional<UserEntity> user = this.userDao.findById(action
					.getactionAttributes()
					.get("followed").toString());
			
			if (user.isPresent()) {
				followed.add(this.userEntityConverter.convertToBoundary(user.get()));
			} else {
				throw new MessageNotFoundExcetpion("could not find this USER with this EMAIL: ");
			}
		}
		
		return followed;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAllRecipeLike(
			String type, String name, int size, int page) {
		
		return this.elementDao
					.findAllByTypeAndNameLike(
							type,
							"%"+name+"%",
							PageRequest.of(page, size, Direction.DESC, "createdTimestamp"))
					.stream()
					.map(this.elementEntityConverter::convertToBoundary)
					.collect(Collectors.toList());		
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getUsersRecipes(
			String type,String createdBy, int size, int page) {
		
		return	this.elementDao
				.findAllByTypeAndCreatedBy_email(
						type,
						createdBy,
						PageRequest.of(page, size, Direction.DESC, "createdTimestamp"))
				.stream()
				.map(this.elementEntityConverter::convertToBoundary)
				.collect(Collectors.toList());
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getUsersSearch(
			String username, int size, int page) {
		
		return	this.userDao
				.findAllByUsernameLike("%"+username+"%",
						PageRequest.of(page, size, Direction.ASC, "email"))
				.stream()
				.map(this.userEntityConverter::convertToBoundary)
				.collect(Collectors.toList());
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getFeed(String email, int size, int page){

		List<UserBoundary> followedByMe = getAllFollowedByMe("follow", email, 10, page);
		List<ElementBoundary> feedsRecipes = new ArrayList<>();
		
		for(UserBoundary followed:followedByMe) {
			feedsRecipes.addAll(getUsersRecipes("recipe", followed.getEmail(), 2, 0));
		}
		return feedsRecipes;		
	}
				
	
}