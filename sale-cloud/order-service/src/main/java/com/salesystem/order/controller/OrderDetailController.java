package com.salesystem.order.controller;

import com.salesystem.common.dto.ProductDTO;
import com.salesystem.order.client.ProductClient;
import com.salesystem.order.entity.OrderDetail;
import com.salesystem.order.service.OrderDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductClient productClient;

    @GetMapping("/list")
    public List<OrderDetail> list() {
        return orderDetailService.findAll();
    }

    /**
     * 订单明细：先查 order_detail，再 Feign 批量取商品名/厂家名，在内存拼装。
     * 返回 key 与原单体保持一致（detail_id/productName/manufacturerName/quantity/total_amount）。
     */
    @GetMapping("/order/{orderId}")
    public List<Map<String, Object>> detail(@PathVariable Integer orderId) {
        List<OrderDetail> details = orderDetailService.findByOrderId(orderId);
        if (details.isEmpty()) {
            return List.of();
        }

        List<Integer> productIds = details.stream()
                .map(OrderDetail::getProductId).distinct().toList();
        Map<Integer, ProductDTO> productMap = productClient.findByIds(productIds).unwrap().stream()
                .collect(Collectors.toMap(ProductDTO::getProductId, p -> p));

        List<Map<String, Object>> result = new ArrayList<>();
        for (OrderDetail d : details) {
            ProductDTO p = productMap.get(d.getProductId());
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("detail_id", d.getDetailId());
            m.put("order_id", d.getOrderId());
            m.put("productName", p == null ? null : p.getDescription());
            m.put("manufacturerName", p == null ? null : p.getManufacturerName());
            m.put("quantity", d.getQuantity());
            m.put("total_amount", d.getTotalAmount());
            result.add(m);
        }
        return result;
    }
}
