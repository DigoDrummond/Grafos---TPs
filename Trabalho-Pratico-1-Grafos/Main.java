import java.io.RandomAccessFile;

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

    public void setVertice(int vertice) {
        this.vertice = vertice;
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

        //Selection Sort
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

public class Main {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        String nomeArq = null;
        System.out.print("\nDigite o nome do arquivo com sua extensão (.txt): ");
        nomeArq = System.console().readLine();
        System.out.print("\nQual algoritmo deseja utilizar? (1 - Tarjan, 2 - CycleFinder): ");
        int algoritmo = Integer.parseInt(System.console().readLine());
        Adjacentes[] adjacentes = null;
        adjacentes = constroiAdjacentes(adjacentes, nomeArq);

        if (algoritmo == 1) {
            int V = adjacentes.length;
            Tarjan g = new Tarjan(V);

            for (int i = 0; i < V; i++) {
                Adjacentes adj = adjacentes[i].getProximo();
                while (adj != null) {
                    g.addAresta(i + 1, adj.getVertice());
                    adj = adj.getProximo();
                }
            }

            g.BCC();

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000; 
            System.out.println("Tempo de execução: " + duration + " ms");
        } else if (algoritmo == 2) {
            CycleFinder cycleFinder = new CycleFinder(adjacentes);
            cycleFinder.encontrarCiclos();

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000; 
            System.out.println("Tempo de execução: " + duration + " ms");
        } else {
            System.out.println("Algoritmo inválido");
        }

        // // imprime a lista de adjacência
        // for (int i = 0; i < adjacentes.length; i++) {
        //     for (Adjacentes adj = adjacentes[i]; adj != null; adj = adj.getProximo()) {
        //         System.out.print(adj.getVertice() + " ");
        //     }
        //     System.out.println();
        // }
    }

    public static Adjacentes[] constroiAdjacentes(Adjacentes[] adjacentes, String nomeArq) {
        // Leitura do arquivo
        try {
            RandomAccessFile rf = new RandomAccessFile(nomeArq, "r");
            int nVertices = Integer.parseInt(rf.readLine());

            adjacentes = new Adjacentes[nVertices];
            for (int i = 0; i < nVertices; i++) {
                adjacentes[i] = new Adjacentes(i+1);
            }

            while(rf.getFilePointer() < rf.length()) {
                int[] valores = processLine(rf);

                Adjacentes aux = adjacentes[valores[0]-1];
                while (aux.getProximo() != null) {
                    aux = aux.getProximo();
                }
                aux.setProximo(new Adjacentes(valores[1]));
                aux = adjacentes[valores[1]-1];
                while (aux.getProximo() != null) {
                    aux = aux.getProximo();
                }
                aux.setProximo(new Adjacentes(valores[0]));
            }

            for (int i = 0; i < nVertices; i++) {
                adjacentes[i].setProximo(Adjacentes.ordenarAdjacentes(adjacentes[i].getProximo()));
            }            

            rf.close();

            return adjacentes;
        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo");
            return null;
        }
    }

    public static int[] processLine(RandomAccessFile rf) {
        int[] values = new int[2];
        try {
            String linha = rf.readLine();
            linha = linha.trim();
            String numeros[] = linha.split("\\s+");
            values[0] = Integer.parseInt(numeros[0]);
            values[1] = Integer.parseInt(numeros[1]);
        } catch (Exception e) {
            System.out.println("Erro ao ler a linha: " + e);
        }
        return values;
    }
}

