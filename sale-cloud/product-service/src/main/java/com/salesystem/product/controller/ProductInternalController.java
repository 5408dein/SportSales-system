package com.salesystem.product.controller;

import com.salesystem.common.dto.ProductDTO;
import com.salesystem.common.dto.StockDeductRequest;
import com.salesystem.common.dto.StockDeductResult;
import com.salesystem.common.dto.StockRestoreRequest;
import com.salesystem.common.result.Result;
import com.salesystem.product.service.ProductService;
import com.salesystem.product.service.StockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供其它服务内部调用的接口（不经过网关）。
 */
@RestController
@RequestMapping("/internal/product")
public class ProductInternalController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @GetMapping("/{id}")
    public Result<ProductDTO> findById(@PathVariable Integer id) {
        return Result.ok(productService.findDTOById(id));
    }

    @PostMapping("/batch")
    public Result<List<ProductDTO>> findByIds(@RequestBody List<Integer> ids) {
        return Result.ok(productService.findDTOByIds(ids));
    }

    @PostMapping("/stock/deduct-batch")
    public Result<List<StockDeductResult>> deductBatch(@RequestBody StockDeductRequest request) {
        return Result.ok(stockService.deductBatch(request));
    }

    @PostMapping("/stock/restore-batch")
    public Result<Void> restoreBatch(@RequestBody StockRestoreRequest request) {
        stockService.restoreBatch(request);
        return Result.ok(null);
    }
}
