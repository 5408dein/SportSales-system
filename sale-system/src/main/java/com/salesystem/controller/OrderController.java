package com.salesystem.controller;

import com.salesystem.entity.Customer;
import com.salesystem.entity.Order;
import com.salesystem.service.CustomerService;
import com.salesystem.service.OrdersService;
import com.salesystem.service.OrderDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    public List<Order> list() {
        return ordersService.findAll();
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> customerOrders(@PathVariable Integer customerId) {
        return ordersService.findByCustomerId(customerId);
    }

    @GetMapping("/byCode")
    public List<Order> customerOrdersByCode(@RequestParam String code) {
        Customer customer = customerService.findByCode(code);
        if (customer == null) {
            return List.of();
        }
        return ordersService.findByCustomerId(customer.getCustomerId());
    }

    @PostMapping("/create")
    public Map<String, Object> create(@RequestBody Order order) {
        Map<String, Object> res = new HashMap<>();
        ordersService.create(order);
        res.put("code", 200);
        res.put("msg", "下单成功");
        return res;
    }

    @PutMapping("/status")
    public Map<String, Object> updateStatus(@RequestParam Integer orderId,
                                            @RequestParam String status) {
        Map<String, Object> res = new HashMap<>();
        ordersService.updateStatus(orderId, status);
        res.put("code", 200);
        res.put("msg", "状态更新成功");
        return res;
    }

    @PutMapping("/ship")
    public Map<String, Object> ship(@RequestParam Integer orderId) {
        Map<String, Object> res = new HashMap<>();
        ordersService.ship(orderId);
        res.put("code", 200);
        res.put("msg", "发货成功");
        return res;
    }

    @PutMapping("/shortage")
    public Map<String, Object> markShortage(@RequestParam Integer orderId) {
        Map<String, Object> res = new HashMap<>();
        ordersService.markShortage(orderId);
        res.put("code", 200);
        res.put("msg", "已标记为缺货");
        return res;
    }

    @PutMapping("/restore")
    public Map<String, Object> restoreSupply(@RequestParam Integer orderId) {
        Map<String, Object> res = new HashMap<>();
        ordersService.restoreSupply(orderId);
        res.put("code", 200);
        res.put("msg", "已恢复为可供货");
        return res;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();
        ordersService.delete(id);
        res.put("code", 200);
        res.put("msg", "订单已删除");
        return res;
    }

    // ========== 报表 ==========

    @GetMapping("/report/paid")
    public List<Order> paidOrders() {
        return ordersService.paidOrders();
    }

    @GetMapping("/report/unpaid")
    public List<Order> unpaidOrders() {
        return ordersService.unpaidOrders();
    }

    @GetMapping("/report/unshipped")
    public List<Order> unshippedOrders() {
        return ordersService.unshippedOrders();
    }

    @GetMapping("/report/shortage")
    public List<Order> shortageOrders() {
        return ordersService.shortageOrders();
    }
}
