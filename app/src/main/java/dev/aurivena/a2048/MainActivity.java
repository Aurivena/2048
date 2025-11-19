package dev.aurivena.a2048;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import dev.aurivena.a2048.domain.model.Field;
import dev.aurivena.a2048.domain.model.State;
import dev.aurivena.a2048.domain.service.FieldService;
import dev.aurivena.a2048.domain.service.MoveService;

public class MainActivity extends AppCompatActivity {

    private GridLayout board;
    private TextView scoreText, bestText;
    private FieldService fieldService;
    private MoveService moveService;
    private Field field;
    GestureDetector gestureDetector;
    private int[][] cellsCache;

    private int[][] cells;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                int [][] cells = field.cells();
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
    private void startNewGame() {
        int size = 4;
        field = new Field(size);
        scoreText.setText("0");
        fieldService.spawnInitialTiles(this.field);
        cells = field.cells();
        cellsCache = cells;

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
        int[][] cache = cells;
        cellsCache = cache;
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
        }
        renderField();
    }

    private void undo(){
        cells = cellsCache;
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
            bestText.setText(String.valueOf(score));
        }
    }

    private void updateScore(){
        HashMap<Integer,int[]> changes = moveService.getArrayChanges();
        int score = Integer.parseInt(scoreText.getText().toString());

        for (int i = 0; i < changes.size(); i++) {
            int[] change = changes.get(i);
            for (int c : change) {
                score+=c;
            }
        }

        moveService.clearArrayChanges();

        scoreText.setText(String.valueOf(score));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}

