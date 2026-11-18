package com.salesystem.controller;

import com.salesystem.entity.OrderDetail;
import com.salesystem.service.OrderDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderDetail")
@CrossOrigin
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/list")
    public List<OrderDetail> list() {
        return orderDetailService.findAll();
    }

    @GetMapping("/order/{orderId}")
    public List<?> detail(@PathVariable Integer orderId) {
        return orderDetailService.findByOrderId(orderId);
    }

}