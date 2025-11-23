package dev.aurivena.a2048;

import dev.aurivena.a2048.domain.model.MoveResult;
import dev.aurivena.a2048.domain.model.State;
import dev.aurivena.a2048.domain.service.MoveService;

public class MoveCoordinator {
    private final MoveService moveService;

    public MoveCoordinator() {
        moveService = new MoveService();
    }

    public MoveResult move(State state, int[][] cells) {
        if (!moveService.hasMoves(cells)) {
            return new MoveResult(false, -1);
        }

        int normalized = 4;
        int coups = 0;
        while (coups < state.getValue()) {
            moveService.rotate(cells);
            coups++;
        }

        MoveResult moveResult = moveService.move(cells);

        while (normalized > state.getValue() && state.getValue() != State.LEFT.getValue()) {
            moveService.rotate(cells);
            normalized--;
        }

        return moveResult;
    }
}
