package acs.boundary.aidClass;

import java.util.Objects;


public class BounderyElement {
	
	private String elementId;

	public BounderyElement() {
	}

	public BounderyElement(String elementId) {
		
		super();
		this.elementId = elementId;
		
	}

	
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String element) {
		this.elementId = element;
	}
	
	@Override
	public boolean equals(Object o) {
		 // self check
	    if (this == o)
	        return true;
	    // null check
	    if (o == null)
	        return false;
	    // type check and cast
	    if (getClass() != o.getClass())
	        return false;
	    BounderyElement element= (BounderyElement) o;
	    // field comparison
	    return Objects.equals(this.elementId, element.elementId);
	} 
	
	@Override
	public int hashCode() {
		if(this.elementId==null)
			return -1;
	return Integer.parseInt(this.elementId);
	}
	
}