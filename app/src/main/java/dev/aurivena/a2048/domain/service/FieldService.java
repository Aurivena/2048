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
        spawnRandomTile(field);
        spawnRandomTile(field);
    }

    public void spawnRandomTile(Field field) {
        int[][] cells = field.cells();

        Position pos = findFreeCell(cells);
        if (pos == null) return;

        cells[pos.x()][pos.y()] = randomTileValue();
    }

    private Position findFreeCell(int[][] cells) {
        List<Position> positions = new ArrayList<>();

        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                if (cells[x][y] == 0) positions.add(new Position(x, y));
            }
        }

        if (positions.isEmpty()) return null;

        return positions.get(random.nextInt(positions.size()));
    }

    private int randomTileValue() {
        return random.nextInt(10) < 9 ? 2 : 4;
    }
}
