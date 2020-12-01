package acs.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acs.boundary.LocationBoundery;
import acs.data.aidClass.EntityEmail;

@Entity
@Table(name="ELEMENT")
public class ElementEntity {

	private Long elementId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private EntityEmail createdBy;
	private UserEntity creator;
	private LocationBoundery location;
	private String elementAttributes;
	private Set<ElementEntity> parents;
	private Set<ElementEntity> children;

	public ElementEntity() {
		
		this.createdBy = new EntityEmail();
		this.location = new LocationBoundery();
		this.children = new HashSet<>();
		this.parents =  new HashSet<>();
		
	}
	
	
	@Id
	@Column(name = "ELEMENT_ID")
	public Long getElementId() {
		return elementId;
	}
	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@Embedded
	public EntityEmail getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(EntityEmail createdBy) {
		this.createdBy = createdBy;
	}

	@Embedded
	public LocationBoundery getLocation() {
		return location;
	}
	public void setLocation(LocationBoundery location) {
		this.location = location;
	}

	@Lob
	public String getElementAttributes() {
		return elementAttributes;
	}
	public void setElementAttributes(String elementAttributes) {
		this.elementAttributes = elementAttributes;
	}
	
	@ManyToMany()
	public Set<ElementEntity> getParents() {
		return parents;
	}
	public void setParents(Set<ElementEntity> parents) {
		this.parents = parents;
	}
	
	@ManyToMany(mappedBy = "parents")
	public Set<ElementEntity> getChildren() {
		return children;
	}
	public void setChildren(Set<ElementEntity> children) {
		this.children = children;
	}
	
	@ManyToOne
    @JoinColumn(name = "user_email")
	public UserEntity getCreator() {
		return creator;
	}
	public void setCreator(UserEntity creator) {
		this.creator = creator;
	}
	
	
	public void addParent(ElementEntity parent) {
		this.parents.add(parent);
	}
	
	public void addChildren (ElementEntity children) {
		this.children.add(children);
		children.addParent(this);
	}
	
}
