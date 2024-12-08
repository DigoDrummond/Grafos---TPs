package Tp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Exato {

    public static void main(String[] args) {
        String filePath = "Tp2\\testes\\pmed6.txt"; 

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            Scanner sc = new Scanner(br);
            
            int N = sc.nextInt();  // Número de vértices
            int M = sc.nextInt();  // Número de arestas
            int K = sc.nextInt();  // Número de centros
            
            Grafo grafo = new Grafo(N);
            
            for (int i = 0; i < M; i++) {
                int u = sc.nextInt(); 
                int v = sc.nextInt(); 
                int dist = sc.nextInt(); 
                
                grafo.addAresta(u - 1, v - 1, dist);  
            }
            
            int[][] distancias = grafo.floydWarshall();
            
            long tempoInicio = System.nanoTime();

            forçaBrutaKcentros(N, distancias, K); 

            // Captura o tempo de fim
            long tempoFim = System.nanoTime();

            long tempoExecucao = (tempoFim - tempoInicio) / 1000000; 
            System.out.println("Tempo de execução: " + tempoExecucao + " ms");
            
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void forçaBrutaKcentros(int N, int[][] distancias, int K) {
        int raioMinimo = Integer.MAX_VALUE;  
        List<Integer> melhoresCentros = new ArrayList<>();  

        int[] combination = new int[K];
        for (int i = 0; i < K; i++) {
            combination[i] = i;
        }

        while (true) {
            int raioAtual = calcularRaio(combination, distancias, N);

            if (raioAtual < raioMinimo) {
                raioMinimo = raioAtual;
                melhoresCentros.clear();
                for (int i = 0; i < K; i++) {
                    melhoresCentros.add(combination[i]);
                }
            }

            int i = K - 1;
            while (i >= 0 && combination[i] == i + N - K) {
                i--;
            }

            if (i >= 0) {
                combination[i]++;
                for (int j = i + 1; j < K; j++) {
                    combination[j] = combination[j - 1] + 1;
                }
            } else {
                break; 
            }
        }

        System.out.println("Raio mínimo: " + raioMinimo);
        System.out.print("Centros escolhidos: ");
        for (int centro : melhoresCentros) {
            System.out.print((centro + 1) + " ");  
        }
        System.out.println();
    }

    public static int calcularRaio(int[] centros, int[][] distancias, int N) {
        int raio = 0;

        for (int i = 0; i < N; i++) {
            int distanciaMinima = Integer.MAX_VALUE;
            for (int centro : centros) {
                distanciaMinima = Math.min(distanciaMinima, distancias[i][centro]);
            }
            raio = Math.max(raio, distanciaMinima);
        }

        return raio;
    }
}
