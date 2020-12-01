package acs.logic.errormessages;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class BadPermissionExcetpion extends RuntimeException {
	
	private static final long serialVersionUID = -8868940317682165686L;

	public BadPermissionExcetpion() {
		super();
	}

	public BadPermissionExcetpion(String message, Throwable cause) {
		super(message, cause);
	}

	public BadPermissionExcetpion(String message) {
		super(message);
	}

	public BadPermissionExcetpion(Throwable cause) {
		super(cause);
	}

}
