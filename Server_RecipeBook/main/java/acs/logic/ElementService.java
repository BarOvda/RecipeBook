package acs.logic;

import java.util.List;

import acs.boundary.ElementBoundary;

public interface ElementService {

	public ElementBoundary create(String managerEmail, ElementBoundary element);

	public ElementBoundary update(String managerEmail, String elementID, ElementBoundary update);

	public ElementBoundary getSpecificElement(String userEmail, String elementID);

	public List<ElementBoundary> getAllElements(String userEmail);

	public void deleteAllElements(String adminEmail);

}
