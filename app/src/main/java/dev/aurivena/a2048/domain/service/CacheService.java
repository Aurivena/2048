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

    public int[][] copy(int[][] src) {
        int[][] dst = new int[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, src[i].length);
        }
        return dst;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Cache key) {
        return (T) cache.get(key);
    }
}
