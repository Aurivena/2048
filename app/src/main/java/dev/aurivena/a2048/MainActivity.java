package dev.aurivena.a2048;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import dev.aurivena.a2048.domain.model.Field;
import dev.aurivena.a2048.domain.service.FieldService;

public class MainActivity extends AppCompatActivity {

    private GridLayout board;
    private TextView scoreText, bestText;
    private Button newGameButton;
    private FieldService fieldService;
    private Field field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = findViewById(R.id.board);
        scoreText = findViewById(R.id.score);
        bestText = findViewById(R.id.best);
        newGameButton = findViewById(R.id.restartButton);

        newGameButton.setOnClickListener(v -> startNewGame());
        fieldService = new FieldService();

        startNewGame();
    }
    private void startNewGame() {
        field = new Field(4);
        scoreText.setText("0");
        fieldService.spawnInitialTiles(this.field);

        renderField();
    }

    private  void renderField(){
        int [][]cells = field.cells();
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
}
