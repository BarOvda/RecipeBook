package acs.boundary.aidClass;

import java.util.Objects;

public class BoundaryEmail {
	
	private String email;

	public BoundaryEmail() {
	}

	public BoundaryEmail(String email) {
		
		super();
		this.email = email;
		
	}

	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	    BoundaryEmail email= (BoundaryEmail) o;
	    // field comparison
	    return Objects.equals(this.email, email.email);
	} 
	
	@Override
	public int hashCode() {
		if(this.email==null)
			return 0;
		int hash=0;
		 for(int i = 0; i < this.email.length(); i++){   // while counting characters if less than the length add one        
		        char character = this.email.charAt(i); // start on the first character
		        int ascii = (int) character; //convert the first character
		        hash+=ascii;
		    }
		return hash;

	}
}