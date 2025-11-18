package dev.aurivena.a2048;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import dev.aurivena.a2048.domain.model.Field;
import dev.aurivena.a2048.domain.model.State;
import dev.aurivena.a2048.domain.service.FieldService;
import dev.aurivena.a2048.domain.service.MoveService;

public class MainActivity extends AppCompatActivity {

    private GridLayout board;
    private TextView scoreText, bestText;
    private Button newGameButton;
    private FieldService fieldService;
    private MoveService moveService;
    private final int size = 4;
    private Field field;
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = findViewById(R.id.board);
        scoreText = findViewById(R.id.score);
        bestText = findViewById(R.id.best);

        newGameButton = findViewById(R.id.restartButton);
        moveService = new MoveService();
        fieldService = new FieldService();

        newGameButton.setOnClickListener(v -> startNewGame());

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
                            rotateField(cells, State.LEFT);
                        } else {
                            rotateField(cells, State.RIGHT);
                        }
                        return true;
                    }
                }else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY < 0) {
                            rotateField(cells, State.DOWN);
                        } else {
                            rotateField(cells, State.TOP);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }
    private void startNewGame() {
        field = new Field(this.size);
        scoreText.setText("0");
        fieldService.spawnInitialTiles(this.field);
        int [][]cells = field.cells();

        renderField(cells);
    }

    private  void renderField(int [][]cells){
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

    private void rotateField(int [][]cells, State state){
        int normalized = 4;
        int[][] result = cells;
        int coups = 0;
        while (coups<state.getValue()){
            result =  moveService.rotate(result);
            coups++;
        }

        moveService.move(result);

        while (normalized>state.getValue() && state.getValue() != State.LEFT.getValue()) {
            result = moveService.rotate(result);
            normalized--;
        }

       renderField(result);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}

