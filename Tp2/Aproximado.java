package Tp2;

import java.io.*;
import java.util.*;

public class Aproximado {
    public static void main(String[] args) {
        // Caminho onde os arquivos de teste estão localizados
        String folderPath = "Tp2\\testes\\"; 

        // Arquivo de saída
        String outputFilePath = "Tp2\\resultados\\aproximado.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Laço para processar os arquivos pmed1.txt até pmed40.txt
            for (int i = 1; i <= 40; i++) {
                String filePath = folderPath + "pmed" + i + ".txt";  // Caminho do arquivo de entrada
                System.out.println("Processando: " + filePath);
                
                // Processa o arquivo de entrada e captura o resultado
                String resultado = processarArquivo(filePath);
                
                // Escreve no arquivo de resultados
                writer.write("pmed" + i + ": " + resultado);
                writer.newLine();  // Nova linha para o próximo caso de teste
            }
            
            System.out.println("Resultados salvos no arquivo: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função para processar um arquivo de entrada e retornar o raio e tempo de execução
    public static String processarArquivo(String filePath) {
        // Lê o arquivo de entrada
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             Scanner sc = new Scanner(br)) {
            
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
            
            
            // Captura o tempo de início
            long tempoInicio = System.nanoTime();

            // Agora vamos aplicar o algoritmo Greedy para K-centros
            GreedyKCenter greedy = new GreedyKCenter(grafo);
            greedy.initialize(K); // Inicializa com o número de centros K
            greedy.computeCenters(); // Calcula os centros

            // Captura o tempo de fim
            long tempoFim = System.nanoTime();

            // Calcula e exibe o tempo de execução em milissegundos
            long tempoExecucao = (tempoFim - tempoInicio) / 1000000; // Converte para milissegundos
            
            // Retorna o resultado no formato esperado (raio e tempo de execução)
            return greedy.getRadius() + " " + tempoExecucao + " ms";
            
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao processar o arquivo";
        }
    }
}

// Classe para o algoritmo Greedy para K-centros
class GreedyKCenter {
    private Grafo grafo;
    private int K; // Número de centros
    private Set<Integer> centers; // Conjunto de centros
    private int[][] dist; // Matriz de distâncias entre os vértices
    private int radius; // Raio da solução
    private final int NOT_ASSIGN = Integer.MAX_VALUE;

    public GreedyKCenter(Grafo grafo) {
        this.grafo = grafo;
        this.dist = grafo.floydWarshall(); // Calcula todas as distâncias entre os vértices
        this.centers = new HashSet<>();
        this.radius = NOT_ASSIGN;
    }

    // Inicializa o número de centros K
    public void initialize(int k) {
        this.K = k;
    }

    // Função que encontra o índice do centro com maior distância mínima
    private int maxindex(int[] dist, int n) {
        int mi = 0;
        for (int i = 0; i < n; i++) {
            if (dist[i] > dist[mi]) {
                mi = i;
            }
        }
        return mi;
    }

    // Método que implementa a lógica de seleção dos centros
    public void computeCenters() {
        int[] dist = new int[grafo.getN()];
        Arrays.fill(dist, Integer.MAX_VALUE);

        // Index of the city having the maximum distance to its closest center
        int max = 0;
        List<Integer> centersList = new ArrayList<>();
        
        for (int i = 0; i < K; i++) {
            centersList.add(max);

            // Updating the distance of the cities to their closest centers
            for (int j = 0; j < grafo.getN(); j++) {
                dist[j] = Math.min(dist[j], this.dist[max][j]);
            }

            // Updating the index of the city with the maximum distance to its closest center
            max = maxindex(dist, grafo.getN());
        }

        // Printing the maximum distance of a city to a center (radius)
        this.radius = dist[max];
    }

    // Retorna o raio da solução
    public int getRadius() {
        return radius;
    }

    // Retorna os centros encontrados
    public Set<Integer> getCenters() {
        return centers;
    }
}
