package ru.sapegin.aspect.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheStorage<K, V> {

    @Value("${limit.time.cache-storage}")
    private Long cacheTimeLimit;

    private final Map<K, CacheEntry<V>> storage = new ConcurrentHashMap<>();

    public V get(K key) {
        var cacheEntry = storage.get(key);
        if (cacheEntry == null) {
            return null;
        }

        var currentTime = System.currentTimeMillis();
        var entryTime = cacheEntry.getTimestamp();
        if(currentTime - entryTime > cacheTimeLimit) {
            storage.remove(key);
            return null;
        }

        return cacheEntry.getValue();
    }

    public void put(K key, CacheEntry<V> value) {
        storage.put(key, value);
    }
}
