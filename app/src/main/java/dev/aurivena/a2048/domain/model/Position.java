package dev.aurivena.a2048.domain.model;

public record Position (int x, int y){
    public Position move(int dx, int dy) {
        return new Position(x+ dx, y + dy);
    }
}
