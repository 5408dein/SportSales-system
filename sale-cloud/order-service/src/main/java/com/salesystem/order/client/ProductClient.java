package com.salesystem.order.client;

import com.salesystem.common.dto.ProductDTO;
import com.salesystem.common.dto.StockDeductRequest;
import com.salesystem.common.dto.StockDeductResult;
import com.salesystem.common.dto.StockRestoreRequest;
import com.salesystem.common.result.Result;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调用 product-service 的 Feign 客户端。
 */
@FeignClient(name = "product-service", contextId = "productClient",
        path = "/internal/product", url = "${services.product.url:}")
public interface ProductClient {

    @GetMapping("/{id}")
    Result<ProductDTO> findById(@PathVariable("id") Integer id);

    @PostMapping("/batch")
    Result<List<ProductDTO>> findByIds(@RequestBody List<Integer> ids);

    @PostMapping("/stock/deduct-batch")
    Result<List<StockDeductResult>> deductBatch(@RequestBody StockDeductRequest request);

    @PostMapping("/stock/restore-batch")
    Result<Void> restoreBatch(@RequestBody StockRestoreRequest request);
}
