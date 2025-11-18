package dev.aurivena.a2048.domain.model;

public enum State {
    LEFT(0),
    TOP(1),
    RIGHT(2),
    DOWN(3);

    private final int value;

    State(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
