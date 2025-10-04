package ru.sapegin.aspect.cache;

import lombok.Getter;

@Getter
public class CacheEntry<V> {

    private final V value;

    private final Long timestamp;

    public CacheEntry(final V value, final Long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }
}
