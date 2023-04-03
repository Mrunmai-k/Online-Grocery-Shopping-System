package com.example.grocerystore.validation.implementations;

import com.example.grocerystore.domain.entities.Product;
import com.example.grocerystore.domain.models.service.CategoryServiceModel;
import com.example.grocerystore.domain.models.service.ProductServiceModel;
import com.example.grocerystore.validation.ProductValidationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductionValidationServiceImpl implements ProductValidationService {
    @Override
    public boolean isValid(Product product) {
        return product != null;
    }

    @Override
    public boolean isValid(ProductServiceModel product) {
        return product != null
                && areCategoriesValid(product.getCategories());
    }

    private boolean areCategoriesValid(List<CategoryServiceModel> categories) {
        return categories != null && !categories.isEmpty();
    }
}
