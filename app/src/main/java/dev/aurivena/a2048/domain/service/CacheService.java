package dev.aurivena.a2048.domain.service;

import java.util.HashMap;

import dev.aurivena.a2048.domain.model.Cache;

public class CacheService {
    private final HashMap<Cache, Object> cache;

    public CacheService() {
        cache = new HashMap<>();
    }

    public <T> void put(Cache key, T value) {
        cache.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Cache key) {
        return (T) cache.get(key);
    }
}
