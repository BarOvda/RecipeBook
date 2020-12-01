package acs.data;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import acs.boundary.ElementBoundary;
import acs.boundary.aidClass.BoundaryEmail;
import acs.data.aidClass.EntityEmail;

@Component
public class ElementEntityConverter {

	private ObjectMapper jackson;
	
	@PostConstruct
	public void setup() {
		
		this.jackson = new ObjectMapper();
		
	}
	
	//convert boundary to entity
	public ElementEntity converToEntity(ElementBoundary elementBoundary) {
		
		ElementEntity entity = new ElementEntity();
		
		if(elementBoundary.getElementId() != null) {
			entity.setElementId(Long.parseLong(elementBoundary.getElementId()));
		}else {
			entity.setElementId(null);
		}
		
		if(elementBoundary.getCreatedBy() != null) {
			entity.setCreatedBy(new EntityEmail(elementBoundary.getCreatedBy().getEmail()));
		}
		
		entity.setLocation(elementBoundary.getLocation());
		entity.setCreatedTimestamp(elementBoundary.getCreatedTimestamp());
		entity.setName(elementBoundary.getName());
		entity.setType(elementBoundary.getType());
		
		if(elementBoundary.getActive() != null) {
			entity.setActive(elementBoundary.getActive());
		}
		
		try {
			entity.setElementAttributes(
					this.jackson
						.writeValueAsString(
								elementBoundary.getElementAttributes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return entity;
	}

	//convert entity to boundary
	public ElementBoundary convertToBoundary(ElementEntity entity) {
		
		ElementBoundary boundery = new ElementBoundary();
		
		if(entity.getElementId() != null) {
			boundery.setElementId("" + entity.getElementId());
		}else {
			boundery.setElementId(null);
		}
		
		if(entity.getCreatedBy() != null) {
			boundery.setCreatedBy(new BoundaryEmail(entity.getCreatedBy().getEmail()));
		}else {
			boundery.setCreatedBy(null);
		}
	
		boundery.setLocation(entity.getLocation());
		boundery.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundery.setName(entity.getName());
		boundery.setType(entity.getType());
		boundery.setActive(entity.getActive());
		
		try {
			boundery.setElementAttributes(
					this.jackson.readValue(
							entity.getElementAttributes(), // JSON
							Map.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return boundery;
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
