package com.salesystem.mapper;

import com.salesystem.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {

    List<Message> findByCustomerId(@Param("customerId") Integer customerId);

    int countUnread(@Param("customerId") Integer customerId);

    int insert(Message message);

    int markRead(@Param("messageId") Integer messageId);

    int markAllRead(@Param("customerId") Integer customerId);
}
