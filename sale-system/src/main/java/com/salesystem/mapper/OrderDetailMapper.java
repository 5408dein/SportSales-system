package com.salesystem.mapper;

import com.salesystem.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    List<OrderDetail> findAll();

    List<?> findByOrderId(@Param("orderId") Integer orderId);

    int add(OrderDetail detail);

    int delete(@Param("detailId") Integer detailId);
}