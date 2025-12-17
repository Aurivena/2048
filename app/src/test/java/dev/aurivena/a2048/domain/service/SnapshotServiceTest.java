package dev.aurivena.a2048.domain.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;

public class SnapshotServiceTest {

    private SnapshotService snapshotService;

    @Before
    public void setUp() {
        snapshotService = new SnapshotService();
    }

    @Test
    public void copy_shouldCreateDeepCopy() {
        int[][] original = {
                {1, 2},
                {3, 4}
        };
        snapshotService.copy(original);
        int[][] snapshot = snapshotService.getSnapshot();

        assertArrayEquals(original, snapshot);
        assertNotSame(original, snapshot);
        assertNotSame(original[0], snapshot[0]);
    }
}
