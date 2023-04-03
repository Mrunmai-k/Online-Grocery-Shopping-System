package com.example.grocerystore.validation.implementations;

import com.example.grocerystore.domain.entities.Product;
import com.example.grocerystore.domain.entities.Receipt;
import com.example.grocerystore.domain.models.service.CategoryServiceModel;
import com.example.grocerystore.domain.models.service.ProductServiceModel;
import com.example.grocerystore.domain.models.service.ReceiptServiceModel;
import com.example.grocerystore.validation.ProductValidationService;
import com.example.grocerystore.validation.ReceiptValidationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReceiptValidationServiceImpl implements ReceiptValidationService {
    @Override
    public boolean isValid(Receipt receipt) {
        return receipt != null;
    }

    @Override
    public boolean isValid(ReceiptServiceModel receiptServiceModel) {
        return receiptServiceModel != null;
    }
}
