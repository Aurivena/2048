package dev.aurivena.a2048.domain.model;

public class ObjectField {
    private int[][] field;

    public ObjectField(int length) {
        this.field = new int[length][length];
    }

    public int[][] getField() {
        return this.field;
    }

    public void changeField(int x, int y, int val) {
        this.field[x][y] = val;
    }
}
