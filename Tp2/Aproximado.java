package Tp2;

import java.io.*;
import java.util.*;

public class Aproximado {
    public static void main(String[] args) {
        String folderPath = "Tp2\\testes\\"; 

        String outputFilePath = "Tp2\\resultados\\aproximado.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (int i = 1; i <= 40; i++) {
                String filePath = folderPath + "pmed" + i + ".txt"; 
                System.out.println("Processando: " + filePath);
                
                String resultado = processarArquivo(filePath);
                
                writer.write("pmed" + i + ": " + resultado);
                writer.newLine();  
            }
            
            System.out.println("Resultados salvos no arquivo: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String processarArquivo(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             Scanner sc = new Scanner(br)) {
            
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
            
            
            long tempoInicio = System.nanoTime();

            GreedyKCenter greedy = new GreedyKCenter(grafo);
            greedy.initialize(K); 
            greedy.computeCenters(); 

            long tempoFim = System.nanoTime();

            long tempoExecucao = (tempoFim - tempoInicio) / 1000000; 
            
            return greedy.raio + " " + tempoExecucao + " ms";
            
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao processar o arquivo";
        }
    }
}

class GreedyKCenter {
    private Grafo grafo;
    private int K; 
    public Set<Integer> centers; 
    private int[][] dist; 
    public int raio; 
    private final int NOT_ASSIGN = Integer.MAX_VALUE;

    public GreedyKCenter(Grafo grafo) {
        this.grafo = grafo;
        this.dist = grafo.floydWarshall(); 
        this.centers = new HashSet<>();
        this.raio = NOT_ASSIGN;
    }

    public void initialize(int k) {
        this.K = k;
    }

    private int maxindex(int[] dist, int n) {
        int mi = 0;
        for (int i = 0; i < n; i++) {
            if (dist[i] > dist[mi]) {
                mi = i;
            }
        }
        return mi;
    }

    public void computeCenters() {
        int[] dist = new int[grafo.getN()];
        Arrays.fill(dist, Integer.MAX_VALUE);

        int max = 0;
        List<Integer> centersList = new ArrayList<>();
        
        for (int i = 0; i < K; i++) {
            centersList.add(max);

            for (int j = 0; j < grafo.getN(); j++) {
                dist[j] = Math.min(dist[j], this.dist[max][j]);
            }

            max = maxindex(dist, grafo.getN());
        }

        this.raio = dist[max];
    }

}
