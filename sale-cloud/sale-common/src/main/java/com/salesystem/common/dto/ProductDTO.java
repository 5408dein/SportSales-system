package com.salesystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品信息，跨服务传输用（含厂家名，供订单明细拼装展示）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Integer productId;
    private String productCode;
    private String productName;
    private String description;
    private Double price;
    private Integer stock;
    private Integer manufacturerId;
    private String manufacturerName;
    private String status;
}
