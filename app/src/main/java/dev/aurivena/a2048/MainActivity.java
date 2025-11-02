package dev.aurivena.a2048;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GridLayout board;
    private TextView scoreText, bestText;
    private Button newGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = findViewById(R.id.board);
        scoreText = findViewById(R.id.score);
        bestText = findViewById(R.id.best);
        newGameButton = findViewById(R.id.restartButton);

        newGameButton.setOnClickListener(v -> startNewGame());
        startNewGame();
    }

    private void startNewGame() {
        scoreText.setText("0");
        bestText.setText("0");

        // Очистка всех ячеек
        for (int i = 0; i < board.getChildCount(); i++) {
            TextView cell = (TextView) board.getChildAt(i);
            cell.setText("");
        }

        // TODO: сюда добавишь логику генерации "2" и "4"
    }
}
