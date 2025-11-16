package dev.aurivena.a2048.domain.service;

public class MoveService {
    public MoveService() {
    }

    public void move(int[][]field){
        for (int row=0;row<field.length;row++){
            for (int col = 0; col<field[row].length-1;col++){
               int startValue = field[row][col];
                int sum = field[row][col]+ field[row][col+1];
                if (sum!=startValue){
                    field[row][col] = sum;
                    field[row][col+1] = 0;
                }else{
                    field[row][col+1] = 0;
                }
            }
        }
    }
}
