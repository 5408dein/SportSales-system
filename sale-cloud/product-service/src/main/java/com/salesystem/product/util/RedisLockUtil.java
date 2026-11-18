package com.salesystem.product.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * Redis 分布式锁（加分项）：spring.redis.enabled=false 时自动禁用。
 */
@Component
@ConditionalOnProperty(name = "spring.redis.enabled", havingValue = "true")
public class RedisLockUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String tryLock(String key, long expireSeconds) {
        String lockValue = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, lockValue, Duration.ofSeconds(expireSeconds));
        return Boolean.TRUE.equals(success) ? lockValue : null;
    }

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
