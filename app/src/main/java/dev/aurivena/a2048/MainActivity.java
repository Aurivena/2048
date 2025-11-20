package dev.aurivena.a2048;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.view.KeyEvent;


import androidx.appcompat.app.AppCompatActivity;

import dev.aurivena.a2048.domain.model.Cache;
import dev.aurivena.a2048.domain.model.Field;
import dev.aurivena.a2048.domain.model.State;
import dev.aurivena.a2048.domain.service.CacheService;
import dev.aurivena.a2048.domain.service.FieldService;
import dev.aurivena.a2048.domain.service.MoveService;

public class MainActivity extends AppCompatActivity {

    private GridLayout board;
    private TextView scoreText, bestText;
    private CacheService cacheService;
    private FieldService fieldService;
    private MoveService moveService;
    private Field field;
    private int bestScore;
    GestureDetector gestureDetector;

    private int[][] cells;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cacheService = new CacheService();

        board = findViewById(R.id.board);
        scoreText = findViewById(R.id.score);
        bestText = findViewById(R.id.best);

        Button newGameButton = findViewById(R.id.restartButton);
        Button undoButton = findViewById(R.id.undoButton);
        moveService = new MoveService();
        fieldService = new FieldService();

        newGameButton.setOnClickListener(v -> startNewGame());
        undoButton.setOnClickListener(v -> undo());

        startNewGame();


        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();

                float diffY = e2.getY() - e1.getY();

                if (Math.abs(diffX) > Math.abs(e2.getY() - e1.getY())) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX < 0) {
                            rotateField( State.LEFT);
                        } else {
                            rotateField( State.RIGHT);
                        }
                        return true;
                    }
                }else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY < 0) {
                            rotateField( State.DOWN);
                        } else {
                            rotateField( State.TOP);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT -> {
                rotateField(State.LEFT);
                yield true;
            }
            case KeyEvent.KEYCODE_DPAD_RIGHT -> {
                rotateField(State.RIGHT);
                yield true;
            }
            case KeyEvent.KEYCODE_DPAD_UP -> {
                rotateField(State.DOWN);
                yield true;
            }
            case KeyEvent.KEYCODE_DPAD_DOWN -> {
                rotateField(State.TOP);
                yield true;
            }
            default -> super.onKeyDown(keyCode, event);
        };
    }

    private void startNewGame() {
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

    private void rotateField( State state){
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

    private void undo(){
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
        cacheService.put(Cache.Score,score);

        int add = moveService.getLastScoreGain();

        score+=add;

        scoreText.setText(String.valueOf(score));
    }
}

