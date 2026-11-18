package com.salesystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 扣库存结果：返回商品的快照信息（价格、厂家等），供订单服务写明细。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDeductResult {

    private Integer productId;
    private String productName;
    private String description;
    private Integer manufacturerId;
    private String manufacturerName;
    private Double unitPrice;
    private Integer quantity;
}
