package dev.aurivena.a2048.domain.service;

public class SnapshotService {

    private  int[][] snapshot;

    public SnapshotService(){}

    public void copy(int[][] src) {
        int[][] dst = new int[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, src[i].length);
        }
        snapshot = dst;
    }

    public int[][] getSnapshot(){
        return snapshot;
    }
}
