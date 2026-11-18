package com.salesystem.order.mapper;

import com.salesystem.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrdersMapper {

    List<Order> findAll();

    Order findById(@Param("orderId") Integer orderId);

    List<Order> findByCustomerId(@Param("customerId") Integer customerId);

    List<Integer> findDistinctCustomerIds();

    List<Order> paidOrders();

    List<Order> unpaidOrders();

    List<Order> unshippedOrders();

    List<Order> shortageOrders();

    int create(Order order);

    int delete(@Param("orderId") Integer orderId);

    int updateStatus(@Param("orderId") Integer orderId,
                     @Param("paymentStatus") String paymentStatus);

    int ship(@Param("orderId") Integer orderId,
             @Param("shippingDate") Date shippingDate);

    int markShortage(@Param("orderId") Integer orderId);

    int restoreSupply(@Param("orderId") Integer orderId);
}
