package dev.aurivena.a2048.domain.service;

import java.util.Random;

import dev.aurivena.a2048.domain.model.Position;

public class PositionService {
    private final Random random;

    public PositionService() {
        this.random = new Random();
    }

    private Position generatePair(){
        int x = random.nextInt();
        int y = random.nextInt();

        return new Position(x,y);
    }

}
