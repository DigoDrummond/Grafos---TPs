package Articulacoes;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class Aresta {
    public int origem;
    public int destino;

    public Aresta(int origem, int destino) {
        this.origem = origem;
        this.destino = destino;
    }

    @Override
    public String toString() {
        return origem + " - " + destino;
    }
}

class Grafo {
    private Adjacentes[] adjacentes;
    private int nVertices;
    private int[] tempoDescoberta;
    private int[] menorTempo;
    private int[] pais;
    private boolean[] visitado;
    private int tempo;
    private List<Integer> articulacoes;
    private Stack<Aresta> pilhaArestas;

    public Grafo(Adjacentes[] adjacentes) {
        this.adjacentes = adjacentes;
        this.nVertices = adjacentes.length;
        this.tempoDescoberta = new int[nVertices];
        this.menorTempo = new int[nVertices];
        this.pais = new int[nVertices];
        this.visitado = new boolean[nVertices];
        this.tempo = 0;
        this.articulacoes = new ArrayList<>();
        this.pilhaArestas = new Stack<>();
    }

    public void identificarArticulacoesEComponentesBiconexos() {
        for (int i = 0; i < nVertices; i++) {
            // Para cada vértice, remova-o e teste a conectividade
            boolean[] estadoOriginal = visitado.clone();
            visitado[i] = true;

            // Conta componentes conectados após a remoção do vértice i
            int componentes = 0;
            for (int j = 0; j < nVertices; j++) {
                if (j != i && !visitado[j]) {
                    componentes++;
                    dfsRemocao(j);
                }
            }

            // Se mais de um componente foi encontrado, o vértice é uma articulação
            if (componentes > 1) {
                articulacoes.add(i + 1);
            }

            visitado = estadoOriginal;
        }

        // agora executa o DFS para encontrar componentes biconexos
        for (int i = 0; i < nVertices; i++) {
            if (!visitado[i]) {
                encontrarComponentesDFS(i);
            }
        }

        System.out.println("Articulações encontradas: " + articulacoes);
    }

    private void dfsRemocao(int v) {
        visitado[v] = true;
        for (Adjacentes adj = adjacentes[v].getProximo(); adj != null; adj = adj.getProximo()) {
            if (!visitado[adj.getVertice() - 1]) {
                dfsRemocao(adj.getVertice() - 1);
            }
        }
    }

    // função DFS modificada para encontrar articulações e componentes biconexos
    private void encontrarComponentesDFS(int u) {
        visitado[u] = true;
        tempoDescoberta[u] = menorTempo[u] = ++tempo;
        int filhos = 0;

        for (Adjacentes adj = adjacentes[u].getProximo(); adj != null; adj = adj.getProximo()) {
            int v = adj.getVertice() - 1;

            if (!visitado[v]) {
                filhos++;
                pais[v] = u;
                pilhaArestas.push(new Aresta(u + 1, v + 1)); 
                encontrarComponentesDFS(v);

                menorTempo[u] = Math.min(menorTempo[u], menorTempo[v]);

                if ((pais[u] == -1 && filhos > 1) || (pais[u] != -1 && menorTempo[v] >= tempoDescoberta[u])) {
                    if (!articulacoes.contains(u + 1)) {
                        articulacoes.add(u + 1);
                    }

                    List<Integer> componente = new ArrayList<>();
                    Aresta aresta;
                    do {
                        aresta = pilhaArestas.pop();
                        if (!componente.contains(aresta.origem)) {
                            componente.add(aresta.origem);
                        }
                        if (!componente.contains(aresta.destino)) {
                            componente.add(aresta.destino);
                        }
                    } while (!(aresta.origem == u + 1 && aresta.destino == v + 1));

                    System.out.println("Componente biconexo: " + componente);
                }
            } else if (v != pais[u] && tempoDescoberta[v] < menorTempo[u]) {
                menorTempo[u] = Math.min(menorTempo[u], tempoDescoberta[v]);
                pilhaArestas.push(new Aresta(u + 1, v + 1));
            }
        }
    }
}

class Adjacentes {
    private Adjacentes proximo;
    private int vertice;

    public Adjacentes(int vertice) {
        this.proximo = null;
        this.vertice = vertice;
    }

    public int getVertice() {
        return this.vertice;
    }

    public Adjacentes getProximo() {
        return this.proximo;
    }

    public void setProximo(Adjacentes proximo) {
        this.proximo = proximo;
    }

    public static Adjacentes ordenarAdjacentes(Adjacentes cabeca) {
        if (cabeca == null || cabeca.proximo == null) {
            return cabeca;
        }

        Adjacentes atual = cabeca;
        while (atual != null) {
            Adjacentes menor = atual;
            Adjacentes proximo = atual.proximo;

            while (proximo != null) {
                if (proximo.vertice < menor.vertice) {
                    menor = proximo;
                }
                proximo = proximo.proximo;
            }

            int temp = atual.vertice;
            atual.vertice = menor.vertice;
            menor.vertice = temp;

            atual = atual.proximo;
        }

        return cabeca;
    }
}

public class Articulacoes {
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        String nomeArq = null;
        System.out.print("\nDigite o nome do arquivo com sua extensão (.txt): ");
        nomeArq = System.console().readLine();
        Adjacentes[] adjacentes = constroiAdjacentes(nomeArq);
    
        Grafo grafo = new Grafo(adjacentes);
        grafo.identificarArticulacoesEComponentesBiconexos();

        
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1_000_000; 
        System.out.println("Tempo de execução: " + duration + " ms");
    }

    public static Adjacentes[] constroiAdjacentes(String nomeArq) {
        Adjacentes[] adjacentes = null;

        try {
            RandomAccessFile rf = new RandomAccessFile(nomeArq, "r");
            int nVertices = Integer.parseInt(rf.readLine());

            adjacentes = new Adjacentes[nVertices];
            for (int i = 0; i < nVertices; i++) {
                adjacentes[i] = new Adjacentes(i + 1);
            }

            while (rf.getFilePointer() < rf.length()) {
                int[] valores = processLine(rf);

                Adjacentes aux = adjacentes[valores[0] - 1];
                while (aux.getProximo() != null) {
                    aux = aux.getProximo();
                }
                aux.setProximo(new Adjacentes(valores[1]));
                aux = adjacentes[valores[1] - 1];
                while (aux.getProximo() != null) {
                    aux = aux.getProximo();
                }
                aux.setProximo(new Adjacentes(valores[0]));
            }

            for (int i = 0; i < nVertices; i++) {
                adjacentes[i].setProximo(Adjacentes.ordenarAdjacentes(adjacentes[i].getProximo()));
            }

            rf.close();

        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
        }

        return adjacentes;
    }

    public static int[] processLine(RandomAccessFile rf) {
        int[] values = new int[2];
        try {
            String linha = rf.readLine().trim();
            String[] numeros = linha.split("\\s+");
            values[0] = Integer.parseInt(numeros[0]);
            values[1] = Integer.parseInt(numeros[1]);
        } catch (Exception e) {
            System.out.println("Erro ao ler a linha: " + e.getMessage());
        }
        return values;
    }
}