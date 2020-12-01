package acs.dal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LastIdValue {
	
	private Long lastId;
	
	public LastIdValue() {
	}

	
	@Id
	@GeneratedValue
	public Long getLastId() {
		return lastId;
	}
	public void setLastId(Long lastId) {
		this.lastId = lastId;
	}
	

	
}
