package com.example.grocerystore.validation.implementations;

import com.example.grocerystore.domain.models.service.UserServiceModel;
import com.example.grocerystore.validation.UserValidationService;
import org.springframework.stereotype.Component;

@Component
public class UserValidationServiceImpl implements UserValidationService {
    @Override
    public boolean isValid(UserServiceModel user) {
        return user != null;
    }
}