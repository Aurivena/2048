package dev.aurivena.a2048.domain.service;

public class MoveService {
    public MoveService() {
    }

    public void move(int[][]field){
        final int defaultValue = 0;

        for (int row=0;row<field.length;row++){
            for (int col = 0; col<field[row].length-1;col++){
                int startValue = field[row][col];
                int sum = field[row][col]+ field[row][col+1];
                if (sum!=startValue){
                    field[row][col] = sum;
                }

                field[row][col+1] = defaultValue;
            }
        }
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
}
