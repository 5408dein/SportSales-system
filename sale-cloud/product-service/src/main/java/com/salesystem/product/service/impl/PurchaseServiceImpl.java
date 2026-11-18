package com.salesystem.product.service.impl;

import com.salesystem.product.entity.Purchase;
import com.salesystem.product.mapper.ProductMapper;
import com.salesystem.product.mapper.PurchaseMapper;
import com.salesystem.product.service.PurchaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Purchase> findAll() {
        return purchaseMapper.findAll();
    }

    @Override
    public Purchase findById(Integer id) {
        return purchaseMapper.findById(id);
    }

    @Override
    public int insert(Purchase purchase) {
        if (purchase.getPurchaseDate() == null) {
            purchase.setPurchaseDate(new Date());
        }
        if (purchase.getStatus() == null) {
            purchase.setStatus("pending");
        }
        return purchaseMapper.insert(purchase);
    }

    /**
     * 确认到货：purchase 和 product 同属 product-service，本地事务继续有效。
     */
    @Transactional
    @Override
    public int confirmArrival(Integer purchaseId) {
        Purchase purchase = purchaseMapper.findById(purchaseId);
        if (purchase == null) {
            throw new RuntimeException("进货单不存在");
        }
        if ("arrived".equals(purchase.getStatus())) {
            throw new RuntimeException("该进货单已确认到货");
        }

        int rows = purchaseMapper.updateStatus(purchaseId, "arrived", new Date());
        if (rows == 0) {
            throw new RuntimeException("更新进货单状态失败");
        }

        productMapper.reduceStock(purchase.getProductId(), -purchase.getPurchaseQuantity());

        return rows;
    }

    @Override
    public int delete(Integer id) {
        return purchaseMapper.delete(id);
    }
}
