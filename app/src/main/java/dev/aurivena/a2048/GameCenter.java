package dev.aurivena.a2048;

import android.content.Context;
import android.widget.GridLayout;
import android.widget.TextView;

import dev.aurivena.a2048.domain.model.Cache;
import dev.aurivena.a2048.domain.model.Field;
import dev.aurivena.a2048.domain.model.MoveResult;
import dev.aurivena.a2048.domain.model.State;
import dev.aurivena.a2048.domain.service.CacheService;
import dev.aurivena.a2048.domain.service.FieldService;
import dev.aurivena.a2048.domain.service.SnapshotService;

public class GameCenter {

    private final CacheService cacheService;
    private final FieldService fieldService;
    private final MoveCoordinator moveCoordinator;
    private final SnapshotService snapshotService;
    private final GameUI gameUI;

    private Field field;
    private int[][] cells;
    private int bestScore;
    private int score;


    public GameCenter(Context context, GridLayout board, TextView scoreText, TextView bestText) {
        cacheService = new CacheService(context);
        moveCoordinator = new MoveCoordinator();
        fieldService = new FieldService();
        snapshotService = new SnapshotService();
        gameUI = new GameUI(board, scoreText, bestText);
    }

    public void startNewGame() {
        final int size = 4;
        field = new Field(size);
        fieldService.spawnInitialTiles(this.field);
        cells = field.cells();
        clearInterimData();

        Integer savedBest = cacheService.get(Cache.HighScore);
        if (savedBest != null) {
            bestScore = savedBest;
        }
        gameUI.setBestScore(bestScore);

        gameUI.renderField(cells);
    }

    public void rotateField(State state) {
        snapshotService.copy(cells);

        MoveResult moveResult = moveCoordinator.move(state, cells);

        if (!moveResult.isValid()) {
            startNewGame();
            return;
        }

        if (!moveResult.isChanged()) {
            return;
        }

        cacheService.put(Cache.Cells, snapshotService.getSnapshot());
        cacheService.put(Cache.Score, score);
        cacheService.put(Cache.Best, bestScore); // Saves the previous best for Undo

        updateScore(moveResult.getScore());
        updateBest();
        appendNewTile();

        gameUI.renderField(cells);
    }

    public void undo() {
        int[][] cache = cacheService.get(Cache.Cells);
        if (cache == null) {
            return;
        }

        int best = 0, score = 0;

        Integer cacheBest = cacheService.get(Cache.Best);
        if (cacheBest != null) {
            best = cacheBest;
        }

        Integer cachedScore = cacheService.get(Cache.Score);
        if (cachedScore != null) {
            score = cachedScore;
        }

        this.bestScore = best;
        this.score = score;

        // When undoing, we also update the persistent high score to match the reverted state
        // This ensures if we undo a new record, the record is removed from disk too.
        cacheService.put(Cache.HighScore, this.bestScore);

        gameUI.setBestScore(best);
        gameUI.setScore(score);

        cells = cache;
        gameUI.renderField(cells);
    }


    private void appendNewTile() {
        field.set(cells);
        fieldService.spawnRandomTile(field);
        cells = field.cells();
    }

    private void updateBest() {
        if (score > bestScore) {
            bestScore = score;
            gameUI.setBestScore(bestScore);
            cacheService.put(Cache.HighScore, bestScore);
        }
    }

    private void updateScore(int score) {
        this.score += score;
        gameUI.setScore(this.score);
    }

    private void clearInterimData() {
        snapshotService.copy(cells);
        cacheService.put(Cache.Cells, snapshotService.getSnapshot());
        score = 0;
        cacheService.put(Cache.Score, score);
        gameUI.setScore(score);
    }
}
