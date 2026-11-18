package com.salesystem.controller;

import com.salesystem.entity.Product;
import com.salesystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    // 商品列表
    @GetMapping("/list")
    public List<Product> list() {
        return productService.findAll();
    }

    // 商品详情
    @GetMapping("/{id}")
    public Product findById(@PathVariable Integer id) {
        return productService.findById(id);
    }

    // 新增
    @PostMapping("/add")
    public String add(@RequestBody Product product) {
        productService.insert(product);
        return "success";
    }

    // 更新
    @PutMapping("/update")
    public String update(@RequestBody Product product) {
        productService.update(product);
        return "success";
    }

    // 删除
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        productService.delete(id);
        return "success";
    }

    // 改状态（上架/下架）✔ 对应你 Vue
    @PutMapping("/status")
    public String updateStatus(@RequestBody Product product) {
        productService.updateStatus(product);
        return "success";
    }

    // 更新库存 ✔ 对应你 Vue
    @PutMapping("/stock")
    public String updateStock(@RequestBody Product product) {
        productService.updateStock(product);
        return "success";
    }

    // 低库存提醒 ✔ 对应你 Vue
    @GetMapping("/lowStock")
    public List<Product> lowStock() {
        return productService.lowStock();
    }
}