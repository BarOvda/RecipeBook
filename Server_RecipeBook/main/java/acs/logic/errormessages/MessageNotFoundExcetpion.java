package acs.logic.errormessages;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MessageNotFoundExcetpion extends RuntimeException {
	
	private static final long serialVersionUID = -8868940317682165686L;

	public MessageNotFoundExcetpion() {
		super();
	}

	public MessageNotFoundExcetpion(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageNotFoundExcetpion(String message) {
		super(message);
	}

	public MessageNotFoundExcetpion(Throwable cause) {
		super(cause);
	}

}
