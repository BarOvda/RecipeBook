package acs.data;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acs.data.aidClass.EntityElement;
import acs.data.aidClass.EntityEmail;

@Entity
@Table(name="ACTION")
public class ActionEntity {
	
	private Long actionId;
	private String type;
	private EntityElement element;
	private Date createdTimestamp;
	private EntityEmail invokedBy;
	private String actionAttributes;
	
	public ActionEntity() {
		
		this.element = new EntityElement();
		this.invokedBy = new EntityEmail();
		
	}
	
	
	@Id
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Embedded
	public EntityElement getElement() {
		return element;
	}
	public void setElement(EntityElement element) {
		this.element = element;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	
	@Embedded
	public EntityEmail getInvokedBy() {
		return invokedBy;
	}
	public void setInvokedBy(EntityEmail invokedBy) {
		this.invokedBy = invokedBy;
	}
	
	@Lob
	public String getActionAttributes() {
		return actionAttributes;
	}
	public void setActionAttributes(String actionAttributes) {
		this.actionAttributes = actionAttributes;
	}
}
