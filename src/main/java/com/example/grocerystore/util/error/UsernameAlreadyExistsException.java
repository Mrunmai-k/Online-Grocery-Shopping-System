package com.example.grocerystore.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.grocerystore.util.constants.ExceptionMessages.*;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.CONFLICT, reason = USER_NAME_EXIST_EX_MSG)
public class UsernameAlreadyExistsException extends RuntimeException {

    private int statusCode;

    public UsernameAlreadyExistsException() {
        this.statusCode = 409;
    }

    public UsernameAlreadyExistsException(String message) {
        super(message);
        this.statusCode = 409;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
