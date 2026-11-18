package com.salesystem.product.service;

import com.salesystem.common.dto.ProductDTO;
import com.salesystem.product.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(Integer id);

    ProductDTO findDTOById(Integer id);

    List<ProductDTO> findDTOByIds(List<Integer> ids);

    void insert(Product product);

    void update(Product product);

    void delete(Integer id);

    void updateStatus(Product product);

    void updateStock(Product product);

    List<Product> lowStock();
}
