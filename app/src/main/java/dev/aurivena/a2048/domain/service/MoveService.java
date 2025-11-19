package dev.aurivena.a2048.domain.service;

public class MoveService {
    public MoveService() {
    }

    public void move(int[][]field){
        for (int row=0;row<field.length;row++){
          int[] cmp = compress(field[row]);

          for (int i=0;i<cmp.length-1;i++) {
              if (cmp[i]!=0 && cmp[i]== cmp[i+1]){
                  cmp[i] *=2;
                  cmp[i+1] = 0;
              }

              field[row] = compress(cmp);
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
