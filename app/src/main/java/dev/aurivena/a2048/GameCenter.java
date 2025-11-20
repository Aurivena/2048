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

public class GameCenter {

    private final GridLayout board;
    private final TextView scoreText;
    private final TextView bestText;
    private final CacheService cacheService;
    private final FieldService fieldService;
    private final MoveService moveService;
    private Field field;
    private int bestScore;

    private int[][] cells;

    public GameCenter(GridLayout board, TextView scoreText, TextView bestText){
        cacheService = new CacheService();

        this.board = board;
        this.scoreText = scoreText;
        this.bestText = bestText;

        moveService = new MoveService();
        fieldService = new FieldService();
    }

    public void startNewGame() {
        int size = 4;
        field = new Field(size);
        scoreText.setText("0");
        fieldService.spawnInitialTiles(this.field);
        cells = field.cells();
        cacheService.put(Cache.Cells, cacheService.copy(cells));

        if (cacheService.get(Cache.Best) != null){
            cacheService.put(Cache.Best,bestScore);
        }

        renderField();
    }

    public void rotateField( State state){
        cacheService.put(Cache.Cells, cacheService.copy(cells));
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

        if (cacheService.get(Cache.Best) != null){
            best = cacheService.get(Cache.Best);
        }

        if (cacheService.get(Cache.Score) != null){
            score = cacheService.get(Cache.Score);
        }

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
        int score = Integer.parseInt(scoreText.getText().toString());
        int best  = Integer.parseInt(bestText.getText().toString());
        if (score > best) {
            cacheService.put(Cache.Best, best);
            bestScore = score;
            bestText.setText(String.valueOf(score));
        }
    }

    private void updateScore(){
        int score = Integer.parseInt(scoreText.getText().toString());
        int old = score;
        cacheService.put(Cache.Score,old);

        int add = moveService.getLastScoreGain();
        score+=add;

        scoreText.setText(String.valueOf(score));
    }
}
