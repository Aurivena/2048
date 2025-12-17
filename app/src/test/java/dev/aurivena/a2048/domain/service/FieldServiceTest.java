package dev.aurivena.a2048.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import dev.aurivena.a2048.domain.model.Field;

public class FieldServiceTest {

    private FieldService fieldService;

    @Before
    public void setUp() {
        fieldService = new FieldService();
    }

    @Test
    public void spawnInitialTiles_shouldSpawnTwoTiles() {
        Field field = new Field(4);
        fieldService.spawnInitialTiles(field);
        int[][] cells = field.cells();
        int count = 0;
        for (int[] row : cells) {
            for (int cell : row) {
                if (cell != 0) {
                    count++;
                }
            }
        }
        assertEquals(2, count);
    }

    @Test
    public void spawnRandomTile_shouldSpawnOneTile() {
        Field field = new Field(4);
        fieldService.spawnRandomTile(field);
        int[][] cells = field.cells();
        int count = 0;
        for (int[] row : cells) {
            for (int cell : row) {
                if (cell != 0) {
                    count++;
                }
            }
        }
        assertEquals(1, count);
    }

    @Test
    public void spawnRandomTile_shouldNotSpawnIfFull() {
        Field field = new Field(2);
        int[][] cells = field.cells();
        cells[0][0] = 2;
        cells[0][1] = 2;
        cells[1][0] = 2;
        cells[1][1] = 2;

        fieldService.spawnRandomTile(field);

        // Count should still be 4
        int count = 0;
        for (int[] row : cells) {
            for (int cell : row) {
                if (cell != 0) {
                    count++;
                }
            }
        }
        assertEquals(4, count);
    }

    @Test
    public void spawnRandomTile_shouldSpawn2or4() {
        Field field = new Field(4);
        Set<Integer> spawnedValues = new HashSet<>();

        // Try spawning multiple times to get both 2 and 4 potentially
        for (int i = 0; i < 20; i++) {
             field = new Field(4);
             fieldService.spawnRandomTile(field);
             for(int[] row: field.cells()) {
                 for(int cell: row) {
                     if (cell != 0) spawnedValues.add(cell);
                 }
             }
        }

        for (int value : spawnedValues) {
            assertTrue("Value should be 2 or 4", value == 2 || value == 4);
        }
    }
}
