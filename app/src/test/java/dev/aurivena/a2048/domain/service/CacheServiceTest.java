package dev.aurivena.a2048.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import dev.aurivena.a2048.domain.model.Cache;

@RunWith(MockitoJUnitRunner.class)
public class CacheServiceTest {

    @Mock
    private Context context;
    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private SharedPreferences.Editor editor;

    private CacheService cacheService;

    @Before
    public void setUp() {
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putInt(anyString(), anyInt())).thenReturn(editor);

        cacheService = new CacheService(context);
    }

    @Test
    public void put_shouldCacheInMemory() {
        cacheService.put(Cache.Score, 100);
        Integer score = cacheService.get(Cache.Score);
        assertEquals(Integer.valueOf(100), score);
    }

    @Test
    public void put_HighScore_shouldSaveToSharedPreferences() {
        cacheService.put(Cache.HighScore, 500);

        verify(editor).putInt("HighScore", 500);
        verify(editor).apply();
    }

    @Test
    public void put_Best_shouldNotSaveToSharedPreferences() {
        cacheService.put(Cache.Best, 500);

        verify(editor, never()).putInt(anyString(), anyInt());
    }

    @Test
    public void get_HighScore_shouldRetrieveFromSharedPreferencesIfMissingInMemory() {
        when(sharedPreferences.getInt("HighScore", 0)).thenReturn(200);

        // Ensure memory cache is empty for HighScore
        Integer best = cacheService.get(Cache.HighScore);

        assertEquals(Integer.valueOf(200), best);
        verify(sharedPreferences).getInt("HighScore", 0);
    }

    @Test
    public void get_HighScore_shouldPrioritizeMemory() {
        cacheService.put(Cache.HighScore, 1000); // This also writes to SP, but memory is set

        // Reset mock to ensure we don't call getInt if memory hit?
        // Actually implementation calls getInt only if not in memory.

        Integer best = cacheService.get(Cache.HighScore);
        assertEquals(Integer.valueOf(1000), best);
    }
}
