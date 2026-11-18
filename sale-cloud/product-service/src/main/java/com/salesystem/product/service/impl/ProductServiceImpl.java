package com.salesystem.product.service.impl;

import com.salesystem.common.dto.ProductDTO;
import com.salesystem.product.entity.Manufacturer;
import com.salesystem.product.entity.Product;
import com.salesystem.product.mapper.ManufacturerMapper;
import com.salesystem.product.mapper.ProductMapper;
import com.salesystem.product.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Override
    public List<Product> findAll() {
        return productMapper.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return productMapper.findById(id);
    }

    @Override
    public ProductDTO findDTOById(Integer id) {
        Product p = productMapper.findById(id);
        return p == null ? null : toDTO(p);
    }

    @Override
    public List<ProductDTO> findDTOByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return productMapper.findByIds(ids).stream().map(this::toDTO).toList();
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

    private ProductDTO toDTO(Product p) {
        String manufacturerName = null;
        if (p.getManufacturerId() != null) {
            Manufacturer m = manufacturerMapper.findById(p.getManufacturerId());
            manufacturerName = m != null ? m.getManufacturerName() : null;
        }
        return new ProductDTO(p.getProductId(), p.getProductCode(), p.getProductName(),
                p.getDescription(), p.getPrice(), p.getStock(), p.getManufacturerId(),
                manufacturerName, p.getStatus());
    }
}
