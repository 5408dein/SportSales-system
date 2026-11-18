package com.salesystem.service;

import com.salesystem.entity.Product;
import com.salesystem.mapper.ProductMapper;
import com.salesystem.util.LocalCacheUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品缓存服务（加分项2：Redis缓存）
 * - Redis启用时使用Redis缓存
 * - Redis未启用时使用本地ConcurrentHashMap缓存
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
    private static final long TTL = 600; // 10分钟

    @SuppressWarnings("unchecked")
    public List<Product> findAll() {
        // 优先Redis
        if (redisTemplate != null) {
            List<Product> cached = (List<Product>) redisTemplate.opsForValue().get(CACHE_KEY);
            if (cached != null) return cached;
            List<Product> products = productMapper.findAll();
            redisTemplate.opsForValue().set(CACHE_KEY, products);
            return products;
        }
        // 降级本地缓存
        List<Product> cached = localCache.get(CACHE_KEY, List.class);
        if (cached != null) return cached;
        List<Product> products = productMapper.findAll();
        localCache.put(CACHE_KEY, products, TTL);
        return products;
    }

    /** 清缓存（商品增删改时调用） */
    public void clearCache() {
        if (redisTemplate != null) {
            redisTemplate.delete(CACHE_KEY);
        }
        localCache.evict(CACHE_KEY);
    }
}
