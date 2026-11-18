package com.salesystem.user.controller;

import com.salesystem.user.entity.Customer;
import com.salesystem.user.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    public List<Customer> list() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Integer id) {
        return customerService.findById(id);
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Customer customer) {
        Map<String, Object> res = new HashMap<>();
        customerService.insert(customer);
        res.put("code", 200);
        res.put("msg", "添加成功");
        return res;
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Customer customer) {
        Map<String, Object> res = new HashMap<>();
        customerService.update(customer);
        res.put("code", 200);
        res.put("msg", "更新成功");
        return res;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();
        customerService.delete(id);
        res.put("code", 200);
        res.put("msg", "删除成功");
        return res;
    }
}
