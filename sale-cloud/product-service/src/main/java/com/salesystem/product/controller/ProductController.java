package com.salesystem.product.controller;

import com.salesystem.product.entity.Product;
import com.salesystem.product.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public List<Product> list() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Integer id) {
        return productService.findById(id);
    }

    @PostMapping("/add")
    public String add(@RequestBody Product product) {
        productService.insert(product);
        return "success";
    }

    @PutMapping("/update")
    public String update(@RequestBody Product product) {
        productService.update(product);
        return "success";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        productService.delete(id);
        return "success";
    }

    @PutMapping("/status")
    public String updateStatus(@RequestBody Product product) {
        productService.updateStatus(product);
        return "success";
    }

    @PutMapping("/stock")
    public String updateStock(@RequestBody Product product) {
        productService.updateStock(product);
        return "success";
    }

    @GetMapping("/lowStock")
    public List<Product> lowStock() {
        return productService.lowStock();
    }
}
