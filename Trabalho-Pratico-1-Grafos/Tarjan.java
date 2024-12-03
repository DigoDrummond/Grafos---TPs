import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class Aresta {
    private int origem;
    private int destino;

    public Aresta(int a, int b) {
        this.origem = a;
        this.destino = b;
    }
    
    public int getOrigem() {
        return origem;
    }

    public int getDestino() {
        return destino;
    }

    @Override
    public String toString() {
        return "[" + origem + ", " + destino + "]";
    }
}

class Tarjan {
    private int V;
    private Adjacentes[] adj;
    private int td = 0;
    private int count = 0;

    Tarjan(int V) {
        this.V = V;
        adj = new Adjacentes[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new Adjacentes(i + 1);
        }
    }

    void addAresta(int u, int v) {
        Adjacentes aux = adj[u - 1];
        while (aux.getProximo() != null) {
            aux = aux.getProximo();
        }
        aux.setProximo(new Adjacentes(v));
        aux = adj[v - 1];
        while (aux.getProximo() != null) {
            aux = aux.getProximo();
        }
        aux.setProximo(new Adjacentes(u));
    }

    void BCCUtil(int u, int[] desc, int[] low, int[] pai, Stack<Aresta> st) {
        desc[u - 1] = low[u - 1] = ++td;
        int filho = 0;

        Adjacentes v = adj[u - 1].getProximo();
        while (v != null) {
            int verticeV = v.getVertice();

            if (desc[verticeV - 1] == -1) {
                filho++;
                pai[verticeV - 1] = u;
                st.push(new Aresta(u, verticeV));

                BCCUtil(verticeV, desc, low, pai, st);

                low[u - 1] = Math.min(low[u - 1], low[verticeV - 1]);

                if ((pai[u - 1] == -1 && filho > 1) || (pai[u - 1] != -1 && low[verticeV - 1] >= desc[u - 1])) {
                    List<Aresta> component = new ArrayList<>();
                    while (st.peek().getOrigem() != u || st.peek().getDestino() != verticeV) {
                        component.add(st.pop());
                    }
                    component.add(st.pop());

                    System.out.println("Componente Biconexo:");
                    for (Aresta e : component) {
                        System.out.println(e.getOrigem() + "--" + e.getDestino());
                    }
                    System.out.println();
                    count++;
                }
            } else if (verticeV != pai[u - 1]) {
                low[u - 1] = Math.min(low[u - 1], desc[verticeV - 1]);
                if (desc[verticeV - 1] < desc[u - 1]) {
                    st.push(new Aresta(u, verticeV));
                }
            }

            v = v.getProximo();
        }
    }

    void BCC() {
        int[] desc = new int[V];
        int[] low = new int[V];
        int[] pai = new int[V];
        Stack<Aresta> st = new Stack<>();

        for (int i = 0; i < V; i++) {
            desc[i] = -1;
            low[i] = -1;
            pai[i] = -1;
        }

        for (int i = 0; i < V; i++) {
            if (desc[i] == -1) {
                BCCUtil(i + 1, desc, low, pai, st);
            }

            if (!st.isEmpty()) {
                List<Aresta> component = new ArrayList<>();
                while (!st.isEmpty()) {
                    component.add(st.pop());
                }

                System.out.println("Componente Biconexo:");
                for (Aresta e : component) {
                    System.out.println(e.getOrigem() + "--" + e.getDestino());
                }
                System.out.println();
                count++;
            }
        }
        System.out.println("Número de componentes biconexos: " + count);
    }
}