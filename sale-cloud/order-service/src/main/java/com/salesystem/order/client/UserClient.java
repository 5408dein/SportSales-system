package com.salesystem.order.client;

import com.salesystem.common.dto.CustomerDTO;
import com.salesystem.common.dto.CustomerUpsertRequest;
import com.salesystem.common.dto.MessageSendRequest;
import com.salesystem.common.result.Result;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调用 user-service 的 Feign 客户端。
 * url 配置了直连地址时走直连（联调阶段），否则走服务发现（接入 Nacos 后）。
 */
@FeignClient(name = "user-service", contextId = "userClient",
        path = "/internal/user", url = "${services.user.url:}")
public interface UserClient {

    @GetMapping("/customer/by-code")
    Result<CustomerDTO> findByCode(@RequestParam("code") String code);

    @PostMapping("/customer/batch")
    Result<List<CustomerDTO>> findByIds(@RequestBody List<Integer> ids);

    @PostMapping("/customer/upsert")
    Result<CustomerDTO> upsert(@RequestBody CustomerUpsertRequest request);

    @PostMapping("/message/send")
    Result<Void> sendMessage(@RequestBody MessageSendRequest request);
}
