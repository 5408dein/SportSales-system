package com.salesystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 扣减/恢复库存的单个商品项。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDeductItem {

    private Integer productId;
    private Integer quantity;
}
