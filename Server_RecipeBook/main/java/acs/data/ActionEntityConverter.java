package acs.data;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import acs.boundary.ActionBoundary;
import acs.boundary.aidClass.BoundaryEmail;
import acs.boundary.aidClass.BounderyElement;
import acs.data.ActionEntity;
import acs.data.aidClass.EntityElement;
import acs.data.aidClass.EntityEmail;

@Component
public class ActionEntityConverter {
	
	private ObjectMapper jackson;

	@PostConstruct
	public void setup() {
		
		this.jackson = new ObjectMapper();
		
	}

	//convert entity to boundary
	public ActionBoundary convertFromEntity(ActionEntity actionEntity) {
		
			ActionBoundary boundary = new ActionBoundary();
			
			boundary.setCreatedTimestamp(actionEntity.getCreatedTimestamp());
			
			if(actionEntity.getElement() != null) {
				boundary.setElement(new BounderyElement(actionEntity.getElement().getElementId()));
			}else {
				boundary.setElement(null);
			}
			
			if(actionEntity.getActionId() != null) {
				boundary.setActionId("" + actionEntity.getActionId());
			}else {
				boundary.setActionId(null);
			}
			
			if(actionEntity.getInvokedBy() != null) {
				boundary.setInvokedBy(new BoundaryEmail(actionEntity.getInvokedBy().getEmail()));
			}else {
				boundary.setInvokedBy(null);
			}
			
			boundary.setType(actionEntity.getType());
			
			try {
				boundary.setActionAttributes(this.jackson.readValue(actionEntity.getActionAttributes(), Map.class));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		
		return boundary;

	}

	//convert boundary to entity
	public ActionEntity convertToEntity(ActionBoundary action) {
		ActionEntity entity = new ActionEntity();
		
		if(action.getActionId() != null) {
			entity.setActionId(Long.parseLong(action.getActionId()));
		}
		else {
			entity.setActionId(null);
		}

		entity.setType(action.getType());
		
		if(action.getElement() != null) {
			entity.setElement(new EntityElement(action.getElement().getElementId()));
		}
		
		if(action.getInvokedBy() != null) {
			entity.setInvokedBy(new EntityEmail(action.getInvokedBy().getEmail()));
		}
		
		try {
			entity.setActionAttributes(this.jackson.writeValueAsString(action.getactionAttributes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return entity;
	}
	
	//convert to entity id
	public Long toEntityId(String id) {
		
		if (id != null) {
			return Long.parseLong(id);
		}else {
			return null;
		}
		
	}

	//convert from entity id
	public String fromEntityId(Long id) {
		
		if (id != null) {
			return id.toString();
		}else {
			return null;
		}
		
	}

}
