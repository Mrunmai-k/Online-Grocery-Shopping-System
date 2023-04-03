package com.example.grocerystore.repository;

import com.example.grocerystore.domain.entities.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, String> {

    List <Receipt> findAllReceiptsByRecipient_UsernameOrderByIssuedOn(String customerName);
}
