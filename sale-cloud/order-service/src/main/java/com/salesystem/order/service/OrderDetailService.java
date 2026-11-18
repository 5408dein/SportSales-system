package com.salesystem.order.service;

import com.salesystem.order.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetail> findAll();

    List<OrderDetail> findByOrderId(Integer orderId);

    int add(OrderDetail detail);

    int delete(Integer detailId);
}
