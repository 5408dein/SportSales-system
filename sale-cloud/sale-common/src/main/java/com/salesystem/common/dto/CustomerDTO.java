package com.salesystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 顾客信息，跨服务传输用，不包含密码。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Integer customerId;
    private String customerCode;
    private String customerName;
    private String name;
    private String phone;
    private String address;
    private String postcode;
}
