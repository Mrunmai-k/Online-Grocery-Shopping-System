package com.example.grocerystore.validation;

import com.example.grocerystore.domain.entities.Product;
import com.example.grocerystore.domain.models.service.ProductServiceModel;

public interface ProductValidationService {
    boolean isValid(Product product);

    boolean isValid(ProductServiceModel product);
}
