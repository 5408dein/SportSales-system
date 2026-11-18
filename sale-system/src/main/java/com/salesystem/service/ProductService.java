package com.salesystem.service;

import com.salesystem.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(Integer id);

    void insert(Product product);

    void update(Product product);

    void delete(Integer id);

    void updateStatus(Product product);

    void updateStock(Product product);

    List<Product> lowStock();
}