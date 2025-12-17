package dev.aurivena.a2048.domain.service;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import dev.aurivena.a2048.domain.model.Cache;

public class CacheService {
    private final HashMap<Cache, Object> cache;
    private final SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "game_prefs";

    public CacheService(Context context) {
        cache = new HashMap<>();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public <T> void put(Cache key, T value) {
        cache.put(key, value);
        if (key == Cache.HighScore && value instanceof Integer) {
            sharedPreferences.edit().putInt(key.name(), (Integer) value).apply();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Cache key) {
        if (key == Cache.HighScore) {
            if (cache.containsKey(key)) {
                return (T) cache.get(key);
            }
            // Fallback to SharedPreferences if not in memory cache
            int bestScore = sharedPreferences.getInt(key.name(), 0);
            cache.put(key, bestScore);
            return (T) Integer.valueOf(bestScore);
        }
        return (T) cache.get(key);
    }
}
