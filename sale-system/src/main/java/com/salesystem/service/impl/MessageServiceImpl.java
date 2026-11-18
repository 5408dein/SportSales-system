package com.salesystem.service.impl;

import com.salesystem.entity.Message;
import com.salesystem.mapper.MessageMapper;
import com.salesystem.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> findByCustomerId(Integer customerId) {
        return messageMapper.findByCustomerId(customerId);
    }

    @Override
    public int countUnread(Integer customerId) {
        return messageMapper.countUnread(customerId);
    }

    @Override
    public void send(Integer customerId, Integer orderId, String orderNo, String title, String content, String msgType) {
        Message msg = new Message();
        msg.setCustomerId(customerId);
        msg.setOrderId(orderId);
        msg.setOrderNo(orderNo);
        msg.setTitle(title);
        msg.setContent(content);
        msg.setMsgType(msgType);
        messageMapper.insert(msg);
    }

    @Override
    public int markRead(Integer messageId) {
        return messageMapper.markRead(messageId);
    }

    @Override
    public int markAllRead(Integer customerId) {
        return messageMapper.markAllRead(customerId);
    }
}
