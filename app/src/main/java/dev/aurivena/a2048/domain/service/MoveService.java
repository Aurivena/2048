package dev.aurivena.a2048.domain.service;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MoveService {
    public MoveService() {
    }

    public boolean move(int[][]field){
        boolean changed = false;
        for (int row=0;row<field.length;row++){

          int[] compressed = compress(field[row]);
          int[] merged = merge(compressed);
          int[] finalLine = compress(merged);

          if (!Arrays.equals(field[row], finalLine)){
              field[row] = finalLine;
              changed = true;
          }
        }
        return changed;
    }

    public int[][] rotate(int[][] field){
        int n = field.length;
        int[][] result = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[j][n-1-i] = field[i][j];
            }
        }

        return result;
    };

    private int[] merge(int[] row){
        for (int i=0;i<row.length-1;i++) {
            if (row[i]!=0 && row[i] == row[i+1]){
                row[i] *=2;
                row[i+1] = 0;
            }
        }
        return row;
    }

    private int[]compress(int[] row){
        int[] compressed = new int[row.length];
        int index = 0;

        for (int r : row) {
            if (r != 0) {
                compressed[index++] = r;
            }
        }

        return compressed;
    }
}
