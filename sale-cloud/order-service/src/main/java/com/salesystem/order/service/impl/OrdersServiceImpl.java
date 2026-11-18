package com.salesystem.order.service.impl;

import com.salesystem.common.dto.MessageSendRequest;
import com.salesystem.order.client.UserClient;
import com.salesystem.order.entity.Order;
import com.salesystem.order.mapper.OrdersMapper;
import com.salesystem.order.service.OrderCreateSaga;
import com.salesystem.order.service.OrdersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderCreateSaga orderCreateSaga;

    @Autowired
    private UserClient userClient;

    @Override
    public List<Order> findAll() {
        return ordersMapper.findAll();
    }

    @Override
    public List<Order> findByCustomerId(Integer customerId) {
        return ordersMapper.findByCustomerId(customerId);
    }

    @Override
    public void create(Order order) {
        orderCreateSaga.create(order);
    }

    @Override
    public int updateStatus(Integer orderId, String status) {
        int rows = ordersMapper.updateStatus(orderId, status);
        if (rows > 0 && "3".equals(status)) {
            notify(orderId, "订单已完成", "您的订单已完成，感谢您的购买！", "system");
        }
        return rows;
    }

    @Override
    public int ship(Integer orderId) {
        int rows = ordersMapper.ship(orderId, new Date());
        if (rows > 0) {
            notify(orderId, "订单已发货", "您的订单已发货，请注意查收！", "ship");
        }
        return rows;
    }

    @Override
    public int markShortage(Integer orderId) {
        int rows = ordersMapper.markShortage(orderId);
        if (rows > 0) {
            notify(orderId, "订单缺货通知", "您的订单因库存不足被标记为缺货，我们将尽快联系厂家补货，敬请谅解！", "shortage");
        }
        return rows;
    }

    @Override
    public int restoreSupply(Integer orderId) {
        return ordersMapper.restoreSupply(orderId);
    }

    @Override
    public int delete(Integer orderId) {
        return ordersMapper.delete(orderId);
    }

    @Override
    public List<Order> paidOrders() {
        return ordersMapper.paidOrders();
    }

    @Override
    public List<Order> unpaidOrders() {
        return ordersMapper.unpaidOrders();
    }

    @Override
    public List<Order> unshippedOrders() {
        return ordersMapper.unshippedOrders();
    }

    @Override
    public List<Order> shortageOrders() {
        return ordersMapper.shortageOrders();
    }

    private void notify(Integer orderId, String title, String content, String type) {
        try {
            Order order = ordersMapper.findById(orderId);
            if (order != null) {
                userClient.sendMessage(new MessageSendRequest(order.getCustomerId(), orderId,
                        order.getOrderNo(), title, content, type)).unwrap();
            }
        } catch (Exception e) {
            // 消息发送失败不影响订单主流程
        }
    }
}
