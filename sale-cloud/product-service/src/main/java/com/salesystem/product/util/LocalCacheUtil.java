package com.salesystem.product.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存/锁（Redis 未启用时的降级方案）。
 */
@Component
public class LocalCacheUtil {

    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public void put(String key, Object value, long ttlSeconds) {
        cache.put(key, new CacheEntry(value, System.currentTimeMillis() + ttlSeconds * 1000));
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        CacheEntry entry = cache.get(key);
        if (entry == null) return null;
        if (entry.expireTime < System.currentTimeMillis()) {
            cache.remove(key);
            return null;
        }
        return type.cast(entry.value);
    }

    public void evict(String key) {
        cache.remove(key);
    }

    public boolean tryLocalLock(String key, long timeoutSeconds) {
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000;
        while (System.currentTimeMillis() < deadline) {
            if (!cache.containsKey(key)) {
                cache.put(key, new CacheEntry(Thread.currentThread().getId(), deadline));
                return true;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    public void unlockLocal(String key) {
        cache.remove(key);
    }

    private static class CacheEntry {
        final Object value;
        final long expireTime;

        CacheEntry(Object value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }
    }
}
