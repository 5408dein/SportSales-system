package com.salesystem.controller;

import com.salesystem.entity.Purchase;
import com.salesystem.service.PurchaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/purchase")
@CrossOrigin
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/list")
    public List<Purchase> list() {
        return purchaseService.findAll();
    }

    @GetMapping("/{id}")
    public Purchase getById(@PathVariable Integer id) {
        return purchaseService.findById(id);
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Purchase purchase) {
        Map<String, Object> res = new HashMap<>();
        purchaseService.insert(purchase);
        res.put("code", 200);
        res.put("msg", "进货单创建成功");
        return res;
    }

    @PutMapping("/arrive/{id}")
    public Map<String, Object> arrive(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();
        purchaseService.confirmArrival(id);
        res.put("code", 200);
        res.put("msg", "已确认到货，库存已更新");
        return res;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();
        purchaseService.delete(id);
        res.put("code", 200);
        res.put("msg", "进货单已删除");
        return res;
    }
}
