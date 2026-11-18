package com.salesystem.service;

import com.salesystem.entity.Message;
import java.util.List;

public interface MessageService {

    List<Message> findByCustomerId(Integer customerId);

    int countUnread(Integer customerId);

    void send(Integer customerId, Integer orderId, String orderNo, String title, String content, String msgType);

    int markRead(Integer messageId);

    int markAllRead(Integer customerId);
}
