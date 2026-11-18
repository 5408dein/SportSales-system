package com.salesystem.controller;

import com.salesystem.entity.Admin;
import com.salesystem.entity.Customer;
import com.salesystem.service.AdminService;
import com.salesystem.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LoginController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> form) {

        Map<String, Object> res = new HashMap<>();
        String username = form.get("username");
        String password = form.get("password");
        String role = form.get("role");

        if ("ADMIN".equals(role)) {
            // 管理员登录
            Admin result = adminService.login(username, password);
            if (result != null) {
                res.put("code", 200);
                res.put("role", "ADMIN");
                res.put("user", result);
            } else {
                res.put("code", 500);
                res.put("msg", "账号或密码错误");
            }
        } else {
            // 顾客登录
            Customer customer = customerService.login(username, password);
            if (customer != null) {
                res.put("code", 200);
                res.put("role", "CUSTOMER");
                res.put("user", customer);
            } else {
                res.put("code", 500);
                res.put("msg", "顾客代码或密码错误");
            }
        }
        return res;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Customer customer) {
        Map<String, Object> res = new HashMap<>();
        Customer exist = customerService.findByCode(customer.getCustomerCode());
        if (exist != null) {
            res.put("code", 500);
            res.put("msg", "该顾客代码已存在");
            return res;
        }
        if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
            customer.setPassword("123456");
        }
        customerService.insert(customer);
        res.put("code", 200);
        res.put("role", "CUSTOMER");
        res.put("msg", "注册成功");
        res.put("user", customer);
        return res;
    }
}
