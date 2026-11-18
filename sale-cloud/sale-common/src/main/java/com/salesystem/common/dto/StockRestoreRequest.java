package com.salesystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量恢复库存请求（下单失败补偿用）。requestNo 与扣减时一致。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockRestoreRequest {

    private String requestNo;
    private List<StockDeductItem> items;
}
