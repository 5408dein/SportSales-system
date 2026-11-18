package com.salesystem.service.impl;

import com.salesystem.entity.OrderDetail;
import com.salesystem.mapper.OrderDetailMapper;
import com.salesystem.service.OrderDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl
        implements OrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public List<OrderDetail> findAll() {
        return orderDetailMapper.findAll();
    }

    @Override
    public List<?> findByOrderId(Integer orderId) {
        return orderDetailMapper.findByOrderId(orderId);
    }

    @Override
    public int add(OrderDetail detail) {
        return orderDetailMapper.add(detail);
    }

    @Override
    public int delete(Integer detailId) {
        return orderDetailMapper.delete(detailId);
    }
}