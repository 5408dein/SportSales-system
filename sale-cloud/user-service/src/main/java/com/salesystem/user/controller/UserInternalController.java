package com.salesystem.user.controller;

import com.salesystem.common.dto.CustomerDTO;
import com.salesystem.common.dto.CustomerUpsertRequest;
import com.salesystem.common.dto.MessageSendRequest;
import com.salesystem.common.result.Result;
import com.salesystem.user.entity.Customer;
import com.salesystem.user.service.CustomerService;
import com.salesystem.user.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供其它服务内部调用的接口（不经过网关，不对前端暴露）。
 */
@RestController
@RequestMapping("/internal/user")
public class UserInternalController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/customer/by-code")
    public Result<CustomerDTO> findByCode(@RequestParam("code") String code) {
        Customer c = customerService.findByCode(code);
        return Result.ok(c == null ? null : toDTO(c));
    }

    @PostMapping("/customer/batch")
    public Result<List<CustomerDTO>> findByIds(@RequestBody List<Integer> ids) {
        List<CustomerDTO> list = customerService.findByIds(ids).stream().map(this::toDTO).toList();
        return Result.ok(list);
    }

    @PostMapping("/customer/upsert")
    public Result<CustomerDTO> upsert(@RequestBody CustomerUpsertRequest request) {
        Customer c = customerService.upsert(request);
        return Result.ok(toDTO(c));
    }

    @PostMapping("/message/send")
    public Result<Void> sendMessage(@RequestBody MessageSendRequest request) {
        messageService.send(request.getCustomerId(), request.getOrderId(), request.getOrderNo(),
                request.getTitle(), request.getContent(), request.getMsgType());
        return Result.ok(null);
    }

    private CustomerDTO toDTO(Customer c) {
        return new CustomerDTO(c.getCustomerId(), c.getCustomerCode(), c.getCustomerName(),
                c.getName(), c.getPhone(), c.getAddress(), c.getPostcode());
    }
}
