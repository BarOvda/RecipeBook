 package acs.boundary;

import java.util.Date;
import java.util.Map;
import acs.boundary.aidClass.BounderyElement;
import acs.boundary.aidClass.BoundaryEmail;


public class ActionBoundary {
	
	private String actionId;
	private String type;
	private BounderyElement element;
	private Date createdTimestamp;
	private BoundaryEmail invokedBy;
	private Map<String, Object> actionAttributes;
	
	public ActionBoundary() {
	}

	public ActionBoundary(String actionId, String type, BounderyElement element, Date createTimestamp,
			BoundaryEmail invokedBy, Map<String, Object> actionAttributes) {
		
		super();
		this.actionId = actionId;
		this.type = type;
		this.element = element;
		this.createdTimestamp = createTimestamp;
		this.invokedBy = invokedBy;
		this.actionAttributes = actionAttributes;
		
	}

	
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public BounderyElement getElement() {
		return element;
	}
	public void setElement(BounderyElement element) {
		this.element = element;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public BoundaryEmail getInvokedBy() {
		return invokedBy;
	}
	public void setInvokedBy(BoundaryEmail invokedBy) {
		this.invokedBy = invokedBy;
	}
	
	public Map<String, Object> getactionAttributes() {
		return actionAttributes;
	}
	public void setActionAttributes(Map<String, Object> actionAttributes) {
		this.actionAttributes = actionAttributes;
	}
}
