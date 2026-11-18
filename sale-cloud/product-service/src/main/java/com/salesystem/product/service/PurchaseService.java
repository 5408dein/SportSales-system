package com.salesystem.product.service;

import com.salesystem.product.entity.Purchase;

import java.util.List;

public interface PurchaseService {

    List<Purchase> findAll();

    Purchase findById(Integer id);

    int insert(Purchase purchase);

    int confirmArrival(Integer purchaseId);

    int delete(Integer id);
}
