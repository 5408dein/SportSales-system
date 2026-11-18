package com.salesystem.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * Redis分布式锁（加分项3）
 * 用于防止并发下单导致库存超卖
 * redis.enabled=false 时自动禁用，改用数据库行锁
 */
@Component
@ConditionalOnProperty(name = "spring.redis.enabled", havingValue = "true")
public class RedisLockUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 尝试获取锁
     * @param key 锁的key
     * @param expireSeconds 过期时间（秒）
     * @return 锁的唯一标识（用于释放），获取失败返回null
     */
    public String tryLock(String key, long expireSeconds) {
        String lockValue = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue()
            .setIfAbsent(key, lockValue, Duration.ofSeconds(expireSeconds));
        return Boolean.TRUE.equals(success) ? lockValue : null;
    }

    /**
     * 释放锁（Lua脚本保证原子性）
     */
    public boolean unlock(String key, String lockValue) {
        String script =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "  return redis.call('del', KEYS[1]) " +
            "else " +
            "  return 0 " +
            "end";
        Long result = redisTemplate.execute(
            new DefaultRedisScript<>(script, Long.class),
            List.of(key), lockValue);
        return result != null && result == 1;
    }
}
