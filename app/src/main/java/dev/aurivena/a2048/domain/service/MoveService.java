package dev.aurivena.a2048.domain.service;

import java.util.Arrays;
import java.util.HashMap;

public class MoveService {
    private HashMap<Integer,int[]>arrayChanges = new HashMap<>();
    private int indexMap;
    public MoveService() {
    }

    public boolean move(int[][]field){
        boolean changed = false;
        for (int row=0;row<field.length;row++){

          int[] compressed = compress(field[row]);
          merge(compressed);
          int[] finalLine = compress(compressed);

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

    public HashMap<Integer, int[]> getArrayChanges(){
        return arrayChanges;
    }

    public void clearArrayChanges(){
        arrayChanges.clear();
        indexMap = 0;
    }

    private void merge(int[] row){
        int[] arrayChanges = new int[16];
        int index = 0;
        for (int i=0;i<row.length-1;i++) {
            if (row[i]!=0 && row[i] == row[i+1]){
                row[i] *=2;
                arrayChanges[index++] = row[i];
                row[i+1] = 0;
            }
        }
        this.arrayChanges.put(indexMap++,arrayChanges);
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
