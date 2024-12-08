package Tp2;

import java.util.Arrays;

public class Grafo {
    private int N;
    private int M;
    private int[][] matrizAdj;
    private static final int INFINITY = Integer.MAX_VALUE;

    public Grafo(int n) {
        this.N = n;
        this.matrizAdj = new int[N][N];
        for (int[] row : matrizAdj)
            Arrays.fill(row, INFINITY);
        for (int i = 0; i < N; i++)
            matrizAdj[i][i] = 0;
    }

    public void addAresta(int u, int v, int dist) {
        matrizAdj[u][v] = dist;
        matrizAdj[v][u] = dist;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int[] vect : matrizAdj)
            sb.append(Arrays.toString(vect) + "\n");
        sb.delete(sb.length() - 1, sb.length());
        return "[" + sb.toString() + "]";
    }

    public int[][] floydWarshall() {
        int[][] dist = new int[N][N];
        for (int i = 0; i < N; i++)
            dist[i] = matrizAdj[i].clone();

        for (int k = 0; k < N; k++) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE &&
                            dist[i][k] + dist[k][j] < dist[i][j])
                        dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }
        return dist;
    }

    public int getN() {
        return this.N;
    }

    public int getM() {
        return this.M;
    }

    public int[][] getmatrizAdj() {
        return this.matrizAdj;
    }
}