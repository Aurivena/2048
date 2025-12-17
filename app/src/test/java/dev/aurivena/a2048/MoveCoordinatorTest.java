package dev.aurivena.a2048;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dev.aurivena.a2048.domain.model.MoveResult;
import dev.aurivena.a2048.domain.model.State;

public class MoveCoordinatorTest {

    private MoveCoordinator moveCoordinator;

    @Before
    public void setUp() {
        moveCoordinator = new MoveCoordinator();
    }

    @Test
    public void move_Left_shouldNotRotate() {
        int[][] field = {
                {0, 2, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        // Expected: Move Left (standard move)
        // 2 goes to 0,0

        MoveResult result = moveCoordinator.move(State.LEFT, field);

        assertEquals(2, field[0][0]);
        assertTrue(result.isChanged());
    }

    @Test
    public void move_Right_shouldRotateAndMove() {
        /*
          Right Move.
          MoveService always moves "Left" relative to array.
          To move Right:
          1. Rotate so Right becomes Left (Rotate 180 degrees)
          2. Move
          3. Rotate back (Rotate 180 degrees)

          Or implementation might vary.
          State.RIGHT value = 2.

          Code:
          while (coups < state.getValue()) { rotate }
          move
          while (normalized > state.getValue() ...) { rotate }

          State.LEFT = 0. No rotation.
          State.RIGHT = 2. 2 Rotations.

          Rotation is CCW (Top-Right became First-Col-Top).
          Let's trace CCW 90.
          Original:
          1 2
          3 4

          Rot 1:
          2 4
          1 3
          (Right side became Top side).

          If I want to move Right.
          If I rotate 180 (2 rotations).
          1 2     4 3
          3 4  -> 2 1

          Move "Left":
          4 3 -> 4 3 (no change if simple)

          Suppose:
          0 2 0 0

          Rot 180:
          0 0 2 0

          Move Left:
          2 0 0 0

          Rot 180:
          0 0 0 2

          So moving "Left" on 180-rotated array simulates moving "Right".

          Let's test it.
        */

        int[][] field = {
                {2, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        // Move Right. Should go to 0,3.
        MoveResult result = moveCoordinator.move(State.RIGHT, field);

        assertEquals(0, field[0][0]);
        assertEquals(2, field[0][3]);
        assertTrue(result.isChanged());
    }

    @Test
    public void move_Up_shouldRotateAndMove() {
        /*
          State.UP = 3.
          3 Rotations (270 CCW) = 90 CW.

          Original:
          0
          2
          0
          0

          Rot 270 CCW:
          0 0 0 0
          0 0 2 0 (row 1 becomes col 2?)

          Let's just trust that the coordinator logic is sound if basic moves work.
          If I have a tile at (1,0) (Row 1, Col 0).
          Press Up.
          Should move to (0,0).
        */

        int[][] field = {
                {0, 0, 0, 0},
                {2, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        MoveResult result = moveCoordinator.move(State.UP, field);

        assertEquals(2, field[0][0]);
        assertEquals(0, field[1][0]);
        assertTrue(result.isChanged());
    }

    @Test
    public void move_Down_shouldRotateAndMove() {
        /*
          Tile at (0,0).
          Press Down.
          Should move to (3,0).
        */
        int[][] field = {
                {2, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        MoveResult result = moveCoordinator.move(State.DOWN, field);

        assertEquals(0, field[0][0]);
        assertEquals(2, field[3][0]);
        assertTrue(result.isChanged());
    }
}
