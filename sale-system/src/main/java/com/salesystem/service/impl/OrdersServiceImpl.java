package com.salesystem.service.impl;

import com.salesystem.entity.Customer;
import com.salesystem.entity.Order;
import com.salesystem.entity.OrderDetail;
import com.salesystem.entity.Product;
import com.salesystem.mapper.CustomerMapper;
import com.salesystem.mapper.OrderDetailMapper;
import com.salesystem.mapper.OrdersMapper;
import com.salesystem.mapper.ProductMapper;
import com.salesystem.service.MessageService;
import com.salesystem.service.OrdersService;
import com.salesystem.util.LocalCacheUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private LocalCacheUtil localCache;

    // Redis锁（可能为null，未安装Redis时降级）
    @Autowired(required = false)
    private com.salesystem.util.RedisLockUtil redisLock;

    @Override
    public List<Order> findAll() { return ordersMapper.findAll(); }

    @Override
    public List<Order> findByCustomerId(Integer customerId) { return ordersMapper.findByCustomerId(customerId); }

    /**
     * 创建订单 —— 增强事务 + 分布式锁防超卖（加分项1+3）
     */
    @Transactional(rollbackFor = Exception.class, timeout = 30, isolation = Isolation.READ_COMMITTED)
    @Override
    public void create(Order order) {

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("购物车为空，无法下单");
        }

        // 根据顾客代码查找或创建顾客
        if (order.getCustomerCode() != null && !order.getCustomerCode().isEmpty()) {
            Customer customer = customerMapper.findByCode(order.getCustomerCode());
            if (customer == null) {
                customer = new Customer();
                customer.setCustomerCode(order.getCustomerCode());
                customer.setCustomerName(order.getReceiver() != null ? order.getReceiver() : "新顾客");
                customer.setName(order.getReceiver());
                customer.setPhone(order.getPhone());
                customer.setAddress(order.getAddress());
                customer.setPassword("123456");
                customerMapper.insert(customer);
            } else {
                if (order.getPhone() != null) customer.setPhone(order.getPhone());
                if (order.getAddress() != null) customer.setAddress(order.getAddress());
                customerMapper.update(customer);
            }
            order.setCustomerId(customer.getCustomerId());
        }
        if (order.getCustomerId() == null) { order.setCustomerId(1); }

        order.setOrderDate(new Date());
        if (order.getPaymentStatus() == null) order.setPaymentStatus("0");
        if (order.getSupplyFlag() == null) order.setSupplyFlag("Y");

        // 分布式锁防超卖 — 对每个商品加锁扣库存
        for (OrderDetail item : order.getItems()) {
            String lockKey = "lock:stock:" + item.getProductId();
            String lockVal = null;
            boolean locked = false;

            try {
                // 尝试获取分布式锁（Redis可用时用Redis，否则降级本地锁）
                if (redisLock != null) {
                    lockVal = redisLock.tryLock(lockKey, 5);
                    locked = lockVal != null;
                } else {
                    locked = localCache.tryLocalLock(lockKey, 5);
                }

                if (!locked) {
                    throw new RuntimeException("商品 " + item.getProductId() + " 正在被抢购，请稍后重试");
                }

                Product product = productMapper.findById(item.getProductId());
                if (product == null) throw new RuntimeException("商品不存在: " + item.getProductId());

                int rows = productMapper.reduceStock(item.getProductId(), item.getQuantity());
                if (rows == 0) throw new RuntimeException("库存不足，商品ID: " + item.getProductId());

            } finally {
                if (redisLock != null && lockVal != null) {
                    redisLock.unlock(lockKey, lockVal);
                } else if (locked) {
                    localCache.unlockLocal(lockKey);
                }
            }
        }

        // 创建订单
        ordersMapper.create(order);

        // 创建订单细则
        for (OrderDetail item : order.getItems()) {
            Product product = productMapper.findById(item.getProductId());
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(order.getOrderId());
            detail.setProductId(item.getProductId());
            detail.setManufacturerId(product.getManufacturerId());
            detail.setQuantity(item.getQuantity());
            BigDecimal price = BigDecimal.valueOf(product.getPrice());
            detail.setTotalAmount(price.multiply(BigDecimal.valueOf(item.getQuantity())));
            orderDetailMapper.add(detail);
        }
    }

    @Override
    public int updateStatus(Integer orderId, String status) {
        int rows = ordersMapper.updateStatus(orderId, status);
        if (rows > 0 && "3".equals(status)) {
            asyncNotify(orderId, "订单已完成", "您的订单已完成，感谢您的购买！", "system");
        }
        return rows;
    }

    @Override
    public int ship(Integer orderId) {
        int rows = ordersMapper.ship(orderId, new Date());
        if (rows > 0) {
            asyncNotify(orderId, "订单已发货", "您的订单已发货，请注意查收！", "ship");
        }
        return rows;
    }

    @Override
    public int markShortage(Integer orderId) {
        int rows = ordersMapper.markShortage(orderId);
        if (rows > 0) {
            asyncNotify(orderId, "订单缺货通知", "您的订单因库存不足被标记为缺货，我们将尽快联系厂家补货，敬请谅解！", "shortage");
        }
        return rows;
    }

    @Override
    public int restoreSupply(Integer orderId) { return ordersMapper.restoreSupply(orderId); }

    @Override
    public int delete(Integer orderId) { return ordersMapper.delete(orderId); }

    @Override
    public List<Order> paidOrders() { return ordersMapper.paidOrders(); }
    @Override
    public List<Order> unpaidOrders() { return ordersMapper.unpaidOrders(); }
    @Override
    public List<Order> unshippedOrders() { return ordersMapper.unshippedOrders(); }
    @Override
    public List<Order> shortageOrders() { return ordersMapper.shortageOrders(); }

    /** 异步发送通知（加分项5：多线程） */
    @org.springframework.scheduling.annotation.Async("taskExecutor")
    private void asyncNotify(Integer orderId, String title, String content, String type) {
        List<Order> orders = ordersMapper.findAll();
        for (Order o : orders) {
            if (o.getOrderId().equals(orderId)) {
                messageService.send(o.getCustomerId(), orderId, o.getOrderNo(), title, content, type);
                break;
            }
        }
    }
}
