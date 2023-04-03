package com.example.grocerystore.validation;

import com.example.grocerystore.domain.entities.Receipt;
import com.example.grocerystore.domain.models.service.ReceiptServiceModel;

public interface ReceiptValidationService {
    boolean isValid(Receipt receipt);

    boolean isValid(ReceiptServiceModel receiptServiceModel);
}
