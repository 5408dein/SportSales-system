package com.salesystem.product.service;

import com.salesystem.common.dto.StockDeductItem;
import com.salesystem.common.dto.StockDeductRequest;
import com.salesystem.common.dto.StockDeductResult;
import com.salesystem.common.dto.StockRestoreRequest;
import com.salesystem.common.exception.BusinessException;
import com.salesystem.product.entity.Manufacturer;
import com.salesystem.product.entity.Product;
import com.salesystem.product.mapper.ManufacturerMapper;
import com.salesystem.product.mapper.ProductMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 库存服务：批量扣减/恢复库存，供订单服务内部调用。
 * 防超卖依赖 SQL 的原子条件更新（update ... where stock >= quantity）。
 */
@Service
public class StockService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    /**
     * 批量扣库存：整批在一个本地事务里，任一商品失败则全部回滚。
     */
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public List<StockDeductResult> deductBatch(StockDeductRequest request) {
        List<StockDeductItem> items = new ArrayList<>(request.getItems());
        items.sort(Comparator.comparing(StockDeductItem::getProductId));

        List<StockDeductResult> results = new ArrayList<>();
        for (StockDeductItem item : items) {
            Product p = productMapper.findById(item.getProductId());
            if (p == null) {
                throw new BusinessException("商品不存在: " + item.getProductId());
            }
            int rows = productMapper.reduceStock(item.getProductId(), item.getQuantity());
            if (rows == 0) {
                throw new BusinessException("库存不足，商品ID: " + item.getProductId());
            }
            String manufacturerName = null;
            if (p.getManufacturerId() != null) {
                Manufacturer m = manufacturerMapper.findById(p.getManufacturerId());
                manufacturerName = m != null ? m.getManufacturerName() : null;
            }
            results.add(new StockDeductResult(
                    p.getProductId(), p.getProductName(), p.getDescription(),
                    p.getManufacturerId(), manufacturerName, p.getPrice(), item.getQuantity()));
        }
        return results;
    }

    /**
     * 批量恢复库存（下单失败补偿用）。
     */
    @Transactional(rollbackFor = Exception.class)
    public void restoreBatch(StockRestoreRequest request) {
        for (StockDeductItem item : request.getItems()) {
            productMapper.increaseStock(item.getProductId(), item.getQuantity());
        }
    }
}
