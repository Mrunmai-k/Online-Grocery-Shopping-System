package com.example.grocerystore.service;

import com.example.grocerystore.domain.models.service.OfferServiceModel;

import java.util.List;

public interface OfferService {

    List<OfferServiceModel> findAllOffers();
}
