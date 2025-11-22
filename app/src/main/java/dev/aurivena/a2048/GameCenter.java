package dev.aurivena.a2048;

import android.widget.GridLayout;
import android.widget.TextView;
import dev.aurivena.a2048.domain.model.Cache;
import dev.aurivena.a2048.domain.model.Field;
import dev.aurivena.a2048.domain.model.MoveResult;
import dev.aurivena.a2048.domain.model.State;
import dev.aurivena.a2048.domain.service.CacheService;
import dev.aurivena.a2048.domain.service.FieldService;
import dev.aurivena.a2048.domain.service.MoveService;
import dev.aurivena.a2048.domain.service.SnapshotService;

public class GameCenter {

    private final CacheService cacheService;
    private final FieldService fieldService;
    private final MoveService moveService;
    private final SnapshotService snapshotService;
    private final GameUI gameUI;

    private Field field;
    private int[][] cells;
    private int bestScore;
    private int score;


    public GameCenter(GridLayout board, TextView scoreText, TextView bestText){
        cacheService = new CacheService();
        moveService = new MoveService();
        fieldService = new FieldService();
        snapshotService = new SnapshotService();
        gameUI = new GameUI(board, scoreText, bestText);
    }

    public void startNewGame() {
        final int size = 4;
        field = new Field(size);
        gameUI.setScore(0);
        fieldService.spawnInitialTiles(this.field);
        cells = field.cells();
        clearInterimData();

        Integer cachedBest = cacheService.get(Cache.Best);
        if (cachedBest != null && bestScore < cachedBest) {
                gameUI.setBestScore(bestScore);
        }

       gameUI.renderField(cells);
    }

    public void rotateField(State state){
        snapshotService.copy(cells);
        int normalized = 4;
        int coups = 0;
        while (coups<state.getValue()){
            cells =  moveService.rotate(cells);
            coups++;
        }

       MoveResult moveResult = moveService.move(cells);

        while (normalized>state.getValue() && state.getValue() != State.LEFT.getValue()) {
            cells = moveService.rotate(cells);
            normalized--;
        }

        if (!moveResult.isChanged()){
            return;
        }

        cacheService.put(Cache.Cells, snapshotService.getSnapshot());
        cacheService.put(Cache.Score, score);
        cacheService.put(Cache.Best, bestScore);

        updateScore(moveResult.getScore());
        updateBest();
        appendNewTile();

        if (!moveService.hasMoves(cells)){
            startNewGame();
            return;
        }
        gameUI.renderField(cells);
    }

    public void undo(){
        int[][] cache = cacheService.get(Cache.Cells);
        if (cache == null){
            return;
        }

        int best = 0, score = 0;

        Integer cacheBest = cacheService.get(Cache.Best);
        if (cacheBest != null){
            best = cacheBest;
        }

        Integer cachedScore = cacheService.get(Cache.Score);
        if (cachedScore != null){
            score = cachedScore;
        }

        this.bestScore = best;
        this.score = score;

        gameUI.setBestScore(best);
        gameUI.setScore(score);

        cells = cache;
        gameUI.renderField(cells);
    }


    private void appendNewTile(){
        field.set(cells);
        fieldService.spawnRandomTile(field);
        cells = field.cells();
    }

    private void updateBest() {
        if (score > bestScore) {
            bestScore = score;
            gameUI.setBestScore(bestScore);
        }
    }

    private void updateScore(int score){
        this.score += score;
        gameUI.setScore(this.score);
    }

    private void clearInterimData(){
        snapshotService.copy(cells);
        cacheService.put(Cache.Cells,snapshotService.getSnapshot());
        cacheService.put(Cache.Score,0);
        score = 0;
    }
}
