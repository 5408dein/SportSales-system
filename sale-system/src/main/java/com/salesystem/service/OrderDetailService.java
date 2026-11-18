package com.salesystem.service;

import com.salesystem.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetail> findAll();

    List<?> findByOrderId(Integer orderId);

    int add(OrderDetail detail);

    int delete(Integer detailId);
}