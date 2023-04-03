package com.example.grocerystore.validation;

import com.example.grocerystore.domain.models.service.UserServiceModel;

public interface UserValidationService {
    boolean isValid(UserServiceModel user);
}