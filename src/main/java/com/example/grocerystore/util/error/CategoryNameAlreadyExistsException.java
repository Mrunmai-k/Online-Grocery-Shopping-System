package com.example.grocerystore.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.grocerystore.util.constants.ExceptionMessages.*;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.CONFLICT, reason = CATEGORY_NAME_EXIST_EX_MSG)
public class CategoryNameAlreadyExistsException extends RuntimeException {

    private int statusCode;

    public CategoryNameAlreadyExistsException() {
        this.statusCode = 409;
    }

    public CategoryNameAlreadyExistsException(String message) {
        super(message);
        this.statusCode = 409;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
