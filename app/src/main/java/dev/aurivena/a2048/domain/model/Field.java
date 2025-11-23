package dev.aurivena.a2048.domain.model;

public class Field {
    private int[][] cells;

    public Field(int size) {
        this.cells = new int[size][size];
    }

    public int[][] cells() {
        return cells;
    }

    public void set(int[][] cells) {
        this.cells = cells;
    }
}
