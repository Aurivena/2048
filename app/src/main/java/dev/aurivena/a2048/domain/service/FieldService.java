package dev.aurivena.a2048.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.aurivena.a2048.domain.model.Field;
import dev.aurivena.a2048.domain.model.Position;


public class FieldService {

    private final Random random;

    public FieldService() {
        this.random = new Random();
    }

    public void spawnInitialTiles(Field field) {
        spawnRandomTile(field.getCells());
        spawnRandomTile(field.getCells());
    }

    public void spawnRandomTile(int[][] field) {
        Position pos = findFreeCell(field);
        if (pos == null) return;
        field[pos.x()][pos.y()] = randomTileValue();
    }

    private Position findFreeCell(int[][] field) {
        List<Position> positions = new ArrayList<>();
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field[x].length; y++) {
                if (field[x][y] == 0) positions.add(new Position(x, y));
            }
        }
        if (positions.isEmpty()) return null;
        return positions.get(random.nextInt(positions.size()));
    }

    private int randomTileValue() {
        return random.nextInt(10) < 9 ? 2 : 4;
    }
}
