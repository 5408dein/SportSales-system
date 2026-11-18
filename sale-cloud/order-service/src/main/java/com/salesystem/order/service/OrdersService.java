package com.salesystem.order.service;

import com.salesystem.order.entity.Order;

import java.util.List;

public interface OrdersService {

    List<Order> findAll();

    List<Order> findByCustomerId(Integer customerId);

    void create(Order order);

    int updateStatus(Integer orderId, String status);

    int ship(Integer orderId);

    int markShortage(Integer orderId);

    int restoreSupply(Integer orderId);

    int delete(Integer orderId);

    List<Order> paidOrders();

    List<Order> unpaidOrders();

    List<Order> unshippedOrders();

    List<Order> shortageOrders();
}
