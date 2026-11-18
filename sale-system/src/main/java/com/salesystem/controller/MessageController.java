package com.salesystem.controller;

import com.salesystem.entity.Message;
import com.salesystem.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
@CrossOrigin
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/list")
    public List<Message> list(@RequestParam Integer customerId) {
        return messageService.findByCustomerId(customerId);
    }

    @GetMapping("/unread")
    public Map<String, Object> unread(@RequestParam Integer customerId) {
        Map<String, Object> res = new HashMap<>();
        res.put("count", messageService.countUnread(customerId));
        return res;
    }

    @PutMapping("/read/{id}")
    public Map<String, Object> read(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();
        messageService.markRead(id);
        res.put("code", 200);
        return res;
    }

    @PutMapping("/readAll")
    public Map<String, Object> readAll(@RequestParam Integer customerId) {
        Map<String, Object> res = new HashMap<>();
        messageService.markAllRead(customerId);
        res.put("code", 200);
        return res;
    }
}
