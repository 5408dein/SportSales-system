package com.salesystem.service.impl;

import com.salesystem.entity.Product;
import com.salesystem.mapper.ProductMapper;
import com.salesystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> findAll() {
        return productMapper.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return productMapper.findById(id);
    }

    @Override
    public void insert(Product product) {
        productMapper.insert(product);
    }

    @Override
    public void update(Product product) {
        productMapper.update(product);
    }

    @Override
    public void delete(Integer id) {
        productMapper.delete(id);
    }

    @Override
    public void updateStatus(Product product) {
        productMapper.updateStatus(product);
    }

    @Override
    public void updateStock(Product product) {
        productMapper.updateStock(product);
    }

    @Override
    public List<Product> lowStock() {
        return productMapper.lowStock();
    }
}