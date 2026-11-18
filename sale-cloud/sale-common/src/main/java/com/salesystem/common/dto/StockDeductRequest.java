package com.salesystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量扣库存请求。requestNo 用于幂等，建议用订单号。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDeductRequest {

    private String requestNo;
    private List<StockDeductItem> items;
}
