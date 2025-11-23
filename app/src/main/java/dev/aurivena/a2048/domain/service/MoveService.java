package dev.aurivena.a2048.domain.service;

import java.util.Arrays;
import java.util.HashMap;

import dev.aurivena.a2048.domain.model.MoveResult;
import dev.aurivena.a2048.domain.model.Side;

public class MoveService {

    public  MoveService() {
    }

    public boolean hasMoves(int[][]field) {
        for (int[] row : field){
            for (int v: row){
                if (v==0) return true;
            }
        }

        return (checkSide(Side.Horizontal,field) || checkSide(Side.Vertical, field));
    }

    private boolean checkSide(Side side, int[][] field){
        for (int i = 0; i< field.length;i++){
            for (int j = 0; j<field.length-1;j++){
                if (side == Side.Horizontal) {
                    if (field[i][j] == field[i][j+1]) return true;
                }
                if (side == Side.Vertical){
                    if (field[j][i] == field[j+1][i]) return true;
                }
            }
        }

        return false;
    }

    public MoveResult move(int[][]field){
        boolean changed = false;
        int lastScoreGain = 0;

        for (int[] line : field) {

            int[] before = Arrays.copyOf(line, line.length);

            compress(line);
            lastScoreGain += merge(line);
            compress(line);

            if (!Arrays.equals(before, line)) {
                changed = true;
            }
        }
        return new MoveResult(changed, lastScoreGain);
    }

    public void rotate(int[][] cells){
        int n = cells.length;
        int[][] rotated = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n-1-i] = cells[i][j];
            }
        }

        for (int i = 0; i < n;i++){
            System.arraycopy(rotated[i],0,cells[i],0,n);
        }
    };

    private int merge(int[] row){
        int gained = 0;

        for (int i=0;i<row.length-1;i++) {
            if (row[i]!=0 && row[i] == row[i+1]){
                row[i] *=2;
                gained+=row[i];
                row[i+1] = 0;
            }
        }
        return gained;
    }


    private void compress(int[] row){
        int index = 0;

        for (int r : row) {
            if (r != 0) {
               row[index++] = r;
            }
        }

        while (index<row.length) {
            row[index++] = 0;
        }
    }
}
