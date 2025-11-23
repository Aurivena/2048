package dev.aurivena.a2048;

import android.widget.GridLayout;
import android.widget.TextView;

public class GameUI {
    private final GridLayout board;
    private final TextView scoreText;
    private final TextView bestText;

    public GameUI(GridLayout board, TextView scoreText, TextView bestText) {
        this.board = board;
        this.scoreText = scoreText;
        this.bestText = bestText;
    }

    public void renderField(int[][] cells) {
        int size = cells.length;

        for (int i = 0; i < board.getChildCount(); i++) {
            TextView cell = (TextView) board.getChildAt(i);

            int row = i / size;
            int col = i % size;

            int value = cells[row][col];

            if (value != 0) {
                cell.setText(String.valueOf(value));
            } else {
                cell.setText("");
            }
        }
    }

    public void setBestScore(int score) {
        bestText.setText(String.valueOf(score));
    }

    public void setScore(int score) {
        scoreText.setText(String.valueOf(score));
    }
}
