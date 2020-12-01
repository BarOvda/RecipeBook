package acs.logic;


import java.util.Collection;
import java.util.List;

import acs.boundary.ElementBoundary;

public interface EnchancedElementService extends ElementService {
	
	public void addChildElementToElement(String managerEmail, String parentElementId, String id);
	
	public List<ElementBoundary> getAllElements(String userEmail, int size, int page);
	
	public Collection<ElementBoundary> getChildren(String userEmail, String parentId, int size, int page);
	
	public Collection<ElementBoundary> getParents(String userEmail, String childId, int size, int page);
	
	public List<ElementBoundary> getElementsByName (String userEmail, String name, int size, int page);
	
	public List<ElementBoundary> getElementsByType (String userEmail, String type, int size, int page);
	
}
