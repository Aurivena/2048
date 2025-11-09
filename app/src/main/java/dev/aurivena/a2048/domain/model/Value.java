package dev.aurivena.a2048.domain.model;

import java.util.Random;

public class Value {
    private final Random random;

    public Value(){
        this.random = new Random();
    }
    public int generate(){
        return this.random.nextInt(10) < 9 ? 2 : 4;
    }
}
