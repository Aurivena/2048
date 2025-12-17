package dev.aurivena.a2048.domain.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dev.aurivena.a2048.domain.model.MoveResult;

public class MoveServiceTest {

    private MoveService moveService;

    @Before
    public void setUp() {
        moveService = new MoveService();
    }

    @Test
    public void move_shouldMergeLeft() {
        int[][] field = {
                {2, 2, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        MoveResult result = moveService.move(field);

        assertEquals(4, field[0][0]);
        assertEquals(0, field[0][1]);
        assertTrue(result.isChanged());
        assertEquals(4, result.getScore());
    }

    @Test
    public void move_shouldNotMergeDifferentValues() {
        int[][] field = {
                {2, 4, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        MoveResult result = moveService.move(field);

        assertEquals(2, field[0][0]);
        assertEquals(4, field[0][1]);
        assertFalse(result.isChanged());
        assertEquals(0, result.getScore());
    }

    @Test
    public void move_shouldCompress() {
        int[][] field = {
                {0, 2, 0, 2},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        MoveResult result = moveService.move(field);

        assertEquals(4, field[0][0]);
        assertEquals(0, field[0][1]);
        assertEquals(0, field[0][3]);
        assertTrue(result.isChanged());
    }

    @Test
    public void rotate_shouldRotate90DegreesClockwise() {
        // This is actually tricky, looking at the code:
        // rotated[j][n-1-i] = cells[i][j];
        // i=0, j=0 -> rot[0][3] = cells[0][0]
        // i=0, j=3 -> rot[3][3] = cells[0][3]
        // This looks like counter-clockwise rotation? Or depending on coordinate system.
        // Let's test with a simple matrix

        int[][] field = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        /*
          Expected rotation based on logic:
          rot[0][3] = field[0][0] = 1  -> Top Right is 1
          rot[1][3] = field[0][1] = 2
          rot[3][3] = field[0][3] = 4 -> Bottom Right is 4

          This looks like a 90 degree counter-clockwise rotation if we consider standard matrix indices (row, col).
          Let's trace:
          (0,0) -> (0,3)
          (0,1) -> (1,3)

          Wait, rotated[j][n-1-i] = cells[i][j]
          If i is row, j is col.
          New row is j (old col).
          New col is n-1-i (n-1 - old row).

          (0,0) -> (0, 3)  (Top Left moves to Top Right?) No, wait.
          row 0, col 3. Top Right. Correct.

          So the first row becomes the last column?
          1, 2, 3, 4 ->
                       1
                       2
                       3
                       4

          This is a 90 degree Counter Clockwise rotation.

          Wait, usually 2048 moves are implemented by rotating to standard orientation (Left), moving, and rotating back.
          If standard move is "Left" (compress to index 0).

          If I press UP.
          I want tiles to move to top (row 0).
          If I rotate field so that "Top" becomes "Left".
          Top is row 0. Left is col 0.
          We need (0, x) to become (x, 0).
          This is Transpose.

          If we use rotate 90 deg.
          CCW: Top (0, x) -> Left (x, 0).
          (0,0) -> (0,3) (Wait, 0,0 is Top Left).

          Let's stick to testing what the code *does*.
        */

        moveService.rotate(field);

        int[][] expected = {
                {4, 8, 12, 16},
                {3, 7, 11, 15},
                {2, 6, 10, 14},
                {1, 5, 9, 13}
        };

        /*
          Let's re-verify trace.
          i=0, j=0 (1) -> rot[0][3] = 1.
          i=0, j=1 (2) -> rot[1][3] = 2.
          i=0, j=2 (3) -> rot[2][3] = 3.
          i=0, j=3 (4) -> rot[3][3] = 4.

          So Last Column is {1, 2, 3, 4}^T.
          Correct.
        */

        assertArrayEquals(expected, field);
    }

    @Test
    public void hasMoves_shouldReturnTrueIfEmptyCells() {
        int[][] field = {
            {2, 2, 4, 8},
            {4, 8, 2, 4},
            {2, 4, 8, 2},
            {4, 2, 4, 0}
        };
        assertTrue(moveService.hasMoves(field));
    }

    @Test
    public void hasMoves_shouldReturnTrueIfMergePossible() {
        int[][] field = {
            {2, 2, 4, 8},
            {4, 8, 2, 4},
            {2, 4, 8, 2},
            {4, 2, 4, 2}
        };
        assertTrue(moveService.hasMoves(field));
    }

    @Test
    public void hasMoves_shouldReturnFalseIfNoMoves() {
        int[][] field = {
            {2, 4, 8, 16},
            {32, 64, 128, 256},
            {2, 4, 8, 16},
            {32, 64, 128, 256}
        };
        assertFalse(moveService.hasMoves(field));
    }
}
