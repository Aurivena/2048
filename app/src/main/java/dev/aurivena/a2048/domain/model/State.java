package dev.aurivena.a2048.domain.model;

public enum State {
    LEFT(0),
    UP(3),
    RIGHT(2),
    DOWN(1);

    private final int value;

    State(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
