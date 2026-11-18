package com.salesystem.order.service;

import com.salesystem.common.dto.CustomerDTO;
import com.salesystem.common.dto.CustomerUpsertRequest;
import com.salesystem.common.dto.MessageSendRequest;
import com.salesystem.common.dto.ProductDTO;
import com.salesystem.common.dto.StockDeductItem;
import com.salesystem.common.dto.StockDeductRequest;
import com.salesystem.common.dto.StockDeductResult;
import com.salesystem.common.dto.StockRestoreRequest;
import com.salesystem.common.exception.BusinessException;
import com.salesystem.order.client.ProductClient;
import com.salesystem.order.client.UserClient;
import com.salesystem.order.entity.Order;
import com.salesystem.order.entity.OrderDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单创建 Saga：
 * 1. 查商品快照（product-service）
 * 2. 幂等建/更顾客（user-service）
 * 3. 批量扣库存（product-service，整批一个事务）
 * 4. 本地事务写 orders + order_detail
 * 5. 第 4 步失败则补偿恢复库存
 * 6. 发站内消息（user-service，失败不影响订单）
 */
@Service
public class OrderCreateSaga {

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderLocalTxService orderLocalTxService;

    public void create(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new BusinessException("购物车为空，无法下单");
        }

        String orderNo = (order.getOrderNo() == null || order.getOrderNo().isBlank())
                ? "ORD" + System.currentTimeMillis()
                : order.getOrderNo();
        order.setOrderNo(orderNo);
        order.setOrderDate(new Date());
        if (order.getPaymentStatus() == null) order.setPaymentStatus("0");
        if (order.getSupplyFlag() == null) order.setSupplyFlag("Y");

        // 1. 商品快照
        List<Integer> productIds = order.getItems().stream()
                .map(OrderDetail::getProductId).distinct().toList();
        List<ProductDTO> products = productClient.findByIds(productIds).unwrap();
        if (products.size() != productIds.size()) {
            throw new BusinessException("部分商品不存在或已下架");
        }

        // 2. 顾客 upsert（幂等，失败无需补偿）
        CustomerDTO customer = null;
        if (order.getCustomerCode() != null && !order.getCustomerCode().isEmpty()) {
            CustomerUpsertRequest req = new CustomerUpsertRequest(
                    order.getCustomerCode(), order.getReceiver(), order.getReceiver(),
                    order.getPhone(), order.getAddress(), null);
            customer = userClient.upsert(req).unwrap();
            order.setCustomerId(customer.getCustomerId());
        }
        if (order.getCustomerId() == null) {
            order.setCustomerId(1);
        }

        // 3. 批量扣库存
        List<StockDeductItem> items = order.getItems().stream()
                .map(i -> new StockDeductItem(i.getProductId(), i.getQuantity()))
                .toList();
        List<StockDeductResult> deducted =
                productClient.deductBatch(new StockDeductRequest(orderNo, items)).unwrap();

        // 4. 本地事务建订单 + 明细
        try {
            orderLocalTxService.create(order, deducted);
        } catch (Exception e) {
            // 5. 补偿：恢复库存
            try {
                productClient.restoreBatch(new StockRestoreRequest(orderNo, items)).unwrap();
            } catch (Exception ex) {
                // 补偿失败仅记录，课程设计场景下人工介入
            }
            throw new BusinessException("下单失败：" + e.getMessage());
        }

        // 6. 发消息（失败不影响订单）
        if (customer != null) {
            try {
                userClient.sendMessage(new MessageSendRequest(customer.getCustomerId(),
                        order.getOrderId(), orderNo, "订单创建成功",
                        "您的订单 " + orderNo + " 已创建", "system")).unwrap();
            } catch (Exception e) {
                // 忽略
            }
        }
    }
}
