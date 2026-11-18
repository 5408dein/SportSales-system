package com.salesystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单服务请求用户服务「发站内消息」的入参。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendRequest {

    private Integer customerId;
    private Integer orderId;
    private String orderNo;
    private String title;
    private String content;
    private String msgType;
}
