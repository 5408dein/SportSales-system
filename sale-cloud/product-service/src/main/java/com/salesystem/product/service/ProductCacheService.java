package com.salesystem.product.service;

import com.salesystem.product.entity.Product;
import com.salesystem.product.mapper.ProductMapper;
import com.salesystem.product.util.LocalCacheUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品缓存服务：Redis 启用时用 Redis，未启用时降级本地缓存。
 */
@Service
public class ProductCacheService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private LocalCacheUtil localCache;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY = "cache:product:all";
    private static final long TTL = 600;

    @SuppressWarnings("unchecked")
    public List<Product> findAll() {
        if (redisTemplate != null) {
            List<Product> cached = (List<Product>) redisTemplate.opsForValue().get(CACHE_KEY);
            if (cached != null) return cached;
            List<Product> products = productMapper.findAll();
            redisTemplate.opsForValue().set(CACHE_KEY, products);
            return products;
        }
        List<Product> cached = localCache.get(CACHE_KEY, List.class);
        if (cached != null) return cached;
        List<Product> products = productMapper.findAll();
        localCache.put(CACHE_KEY, products, TTL);
        return products;
    }

    public void clearCache() {
        if (redisTemplate != null) {
            redisTemplate.delete(CACHE_KEY);
        }
        localCache.evict(CACHE_KEY);
    }
}
