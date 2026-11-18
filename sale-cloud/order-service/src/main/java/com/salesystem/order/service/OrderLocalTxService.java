package com.salesystem.order.service;

import com.salesystem.common.dto.StockDeductResult;
import com.salesystem.order.entity.Order;
import com.salesystem.order.entity.OrderDetail;
import com.salesystem.order.mapper.OrderDetailMapper;
import com.salesystem.order.mapper.OrdersMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单本地事务：写 orders + order_detail 两张表。
 * 必须是独立 Bean，否则 @Transactional 自调用失效。
 */
@Service
public class OrderLocalTxService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Transactional(rollbackFor = Exception.class, timeout = 30, isolation = Isolation.READ_COMMITTED)
    public void create(Order order, List<StockDeductResult> deducted) {
        ordersMapper.create(order);

        for (StockDeductResult d : deducted) {
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(order.getOrderId());
            detail.setProductId(d.getProductId());
            detail.setManufacturerId(d.getManufacturerId());
            detail.setQuantity(d.getQuantity());
            detail.setTotalAmount(
                    BigDecimal.valueOf(d.getUnitPrice())
                            .multiply(BigDecimal.valueOf(d.getQuantity())));
            orderDetailMapper.add(detail);
        }
    }
}
