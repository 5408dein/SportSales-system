package com.salesystem.order.mapper;

import com.salesystem.order.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    List<OrderDetail> findAll();

    List<OrderDetail> findByOrderId(@Param("orderId") Integer orderId);

    int add(OrderDetail detail);

    int delete(@Param("detailId") Integer detailId);
}
