package Tp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Exato {

    public static void main(String[] args) {
        // Caminho do arquivo de entrada (substitua com o caminho correto)
        String filePath = "Tp2\\testes\\pmed6.txt"; 

        try {
            // Abre o arquivo para leitura
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            Scanner sc = new Scanner(br);
            
            // Lê a primeira linha: número de vértices, arestas e K
            int N = sc.nextInt();  // Número de vértices
            int M = sc.nextInt();  // Número de arestas
            int K = sc.nextInt();  // Número de centros
            
            // Cria o grafo com o número de vértices
            Grafo grafo = new Grafo(N);
            
            // Lê as arestas e adiciona ao grafo
            for (int i = 0; i < M; i++) {
                int u = sc.nextInt(); // Vértice 1
                int v = sc.nextInt(); // Vértice 2
                int dist = sc.nextInt(); // Distância (peso) da aresta
                
                // Adiciona a aresta ao grafo
                grafo.addAresta(u - 1, v - 1, dist);  // Usando índices 0-based
            }
            
            // Calcula as distâncias entre todos os pares de vértices
            int[][] distancias = grafo.floydWarshall();
            
            // Captura o tempo de início
            long tempoInicio = System.nanoTime();

            // Agora vamos aplicar o algoritmo de força bruta para K-centros
            forçaBrutaKcentros(N, distancias, K); // Calcula a solução exata

            // Captura o tempo de fim
            long tempoFim = System.nanoTime();

            // Calcula e exibe o tempo de execução em milissegundos
            long tempoExecucao = (tempoFim - tempoInicio) / 1000000; // Converte para milissegundos
            System.out.println("Tempo de execução: " + tempoExecucao + " ms");
            
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função que implementa o algoritmo de força bruta para K-centros
    public static void forçaBrutaKcentros(int N, int[][] distancias, int K) {
        // Gera as combinações uma a uma, sem armazená-las todas
        int raioMinimo = Integer.MAX_VALUE;  // Inicializa com um valor muito alto
        List<Integer> melhoresCentros = new ArrayList<>();  // Lista para armazenar os melhores centros

        // Gerar combinações uma a uma e processá-las
        int[] combination = new int[K];
        for (int i = 0; i < K; i++) {
            combination[i] = i;
        }

        while (true) {
            // Calcula o raio para a combinação atual
            int raioAtual = calcularRaio(combination, distancias, N);

            // Atualiza o raio mínimo e os centros se encontramos uma solução melhor
            if (raioAtual < raioMinimo) {
                raioMinimo = raioAtual;
                melhoresCentros.clear();
                for (int i = 0; i < K; i++) {
                    melhoresCentros.add(combination[i]);
                }
            }

            // Encontra a próxima combinação de centros
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
                break; // Quando não há mais combinações possíveis, sai do loop
            }
        }

        // Exibe a solução exata
        System.out.println("Raio mínimo: " + raioMinimo);
        System.out.print("Centros escolhidos: ");
        for (int centro : melhoresCentros) {
            System.out.print((centro + 1) + " ");  // Exibe 1-based
        }
        System.out.println();
    }

    // Função para calcular o raio de uma combinação de centros
    public static int calcularRaio(int[] centros, int[][] distancias, int N) {
        int raio = 0;

        // Para cada vértice, encontra a menor distância para qualquer centro
        for (int i = 0; i < N; i++) {
            int distanciaMinima = Integer.MAX_VALUE;
            for (int centro : centros) {
                distanciaMinima = Math.min(distanciaMinima, distancias[i][centro]);
            }
            raio = Math.max(raio, distanciaMinima);  // O raio é a maior das menores distâncias
        }

        return raio;
    }
}
