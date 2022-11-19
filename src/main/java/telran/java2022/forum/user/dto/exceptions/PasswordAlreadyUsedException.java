package telran.java2022.forum.user.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordAlreadyUsedException extends RuntimeException {
	private static final long serialVersionUID = -3052220880638296447L;

	public PasswordAlreadyUsedException() {
		super("Password does not differ from the old one.");
	}
}
