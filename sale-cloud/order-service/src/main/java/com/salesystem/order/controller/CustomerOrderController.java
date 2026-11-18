package com.salesystem.order.controller;

import com.salesystem.common.dto.CustomerDTO;
import com.salesystem.order.client.UserClient;
import com.salesystem.order.mapper.OrdersMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 提供「有订单的顾客」查询（原 CustomerMapper.fromOrders 的替代）。
 * 哪些顾客有订单取决于 order-service，因此放在这里，通过 Feign 批量取顾客信息。
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerOrderController {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserClient userClient;

    @GetMapping("/fromOrders")
    public List<CustomerDTO> fromOrders() {
        List<Integer> ids = ordersMapper.findDistinctCustomerIds();
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return userClient.findByIds(ids).unwrap();
    }
}
