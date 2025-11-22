package dev.aurivena.a2048.domain.model;

public class MoveResult {
    private final boolean changed;
    private final int score;
    public MoveResult(boolean changed, int score){
        this.changed = changed;
        this.score = score;
    }

    public boolean isChanged(){
        return changed;
    }

    public int getScore(){
        return score;
    }
}
