package com.salesystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单服务请求用户服务「幂等建/更顾客」的入参。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpsertRequest {

    private String customerCode;
    private String customerName;
    private String name;
    private String phone;
    private String address;
    private String postcode;
}
