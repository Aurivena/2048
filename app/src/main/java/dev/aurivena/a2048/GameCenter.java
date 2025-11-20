package dev.aurivena.a2048;

import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import dev.aurivena.a2048.domain.model.Cache;
import dev.aurivena.a2048.domain.model.Field;
import dev.aurivena.a2048.domain.model.State;
import dev.aurivena.a2048.domain.service.CacheService;
import dev.aurivena.a2048.domain.service.FieldService;
import dev.aurivena.a2048.domain.service.MoveService;
import dev.aurivena.a2048.domain.service.SnapshotService;

public class GameCenter {

    private final GridLayout board;
    private final TextView scoreText;
    private final TextView bestText;
    private final CacheService cacheService;
    private final FieldService fieldService;
    private final MoveService moveService;
    private final SnapshotService snapshotService;
    private Field field;
    private int bestScore;
    private int score;

    private int[][] cells;

    public GameCenter(GridLayout board, TextView scoreText, TextView bestText){
        cacheService = new CacheService();

        this.board = board;
        this.scoreText = scoreText;
        this.bestText = bestText;

        moveService = new MoveService();
        fieldService = new FieldService();
        snapshotService = new SnapshotService();
    }

    public void startNewGame() {
        int size = 4;
        field = new Field(size);
        scoreText.setText("0");
        fieldService.spawnInitialTiles(this.field);
        cells = field.cells();
        clearInterimData();

        Integer cachedBest = cacheService.get(Cache.Best);
        if (cachedBest != null) {
            bestScore = cachedBest;
            bestText.setText(String.valueOf(bestScore));
        }

        renderField();
    }

    public void rotateField(State state){
        snapshotService.copy(cells);
        int normalized = 4;
        int coups = 0;
        while (coups<state.getValue()){
            cells =  moveService.rotate(cells);
            coups++;
        }

        boolean changed = moveService.move(cells);

        while (normalized>state.getValue() && state.getValue() != State.LEFT.getValue()) {
            cells = moveService.rotate(cells);
            normalized--;
        }

        if (changed){
            cacheService.put(Cache.Cells, snapshotService.getSnapshot());
            cacheService.put(Cache.Score, score);
            cacheService.put(Cache.Best, bestScore);

            updateScore();
            updateBest();
            appendNewTile();

            if (!moveService.hasMoves(cells)){
                startNewGame();
                return;
            }
        }
        renderField();
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

        bestText.setText(String.valueOf(best));
        scoreText.setText(String.valueOf(score));

        cells = cache;
        renderField();
    }

    private  void renderField(){
        int size = cells.length;

        for (int i = 0; i < board.getChildCount(); i++) {
            TextView cell = (TextView) board.getChildAt(i);

            int row = i/size;
            int col = i%size;

            int value = cells[row][col];

            if (value!=0){
                cell.setText(String.valueOf(value));
            }else{
                cell.setText("");
            }
        }
    }

    private void appendNewTile(){
        field.set(cells);
        fieldService.spawnRandomTile(field);
        cells = field.cells();
    }

    private void updateBest() {
        if (score > bestScore) {
            bestScore = score;
            bestText.setText(String.valueOf(bestScore));
        }
    }

    private void updateScore(){
        int add = moveService.getLastScoreGain();
        score += add;
        scoreText.setText(String.valueOf(score));
    }

    private void clearInterimData(){
        snapshotService.copy(cells);
        cacheService.put(Cache.Cells,snapshotService.getSnapshot());
        cacheService.put(Cache.Score,0);
        score = 0;
    }
}
