import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class CycleFinder {
    private Adjacentes[] adjacentes;
    private List<List<Integer>> ciclosList;

    public CycleFinder(Adjacentes[] adjacentes) {
        this.adjacentes = adjacentes;
        this.ciclosList = new ArrayList<>();
    }

    public void encontrarCiclos() {
        int n = adjacentes.length;
        for (int i = 0; i < n; i++) {
            boolean[] visitados = new boolean[n];
            List<Integer> caminhoAtual = new ArrayList<>();
            dfs(i, i, visitados, caminhoAtual);
        }
        // Após coletar todos os ciclos, filtramos para manter apenas os maiores
        filtrarCiclos();
        // Imprimimos os ciclos filtrados
        for (List<Integer> ciclo : ciclosList) {
            System.out.println("Ciclo: " + ciclo);
        }
    }

    private void dfs(int verticeAtual, int verticeInicial, boolean[] visitados, List<Integer> caminhoAtual) {
        visitados[verticeAtual] = true;
        caminhoAtual.add(verticeAtual + 1); // +1 para ajustar o índice (0-based) para vértices (1-based)

        for (Adjacentes adj = adjacentes[verticeAtual].getProximo(); adj != null; adj = adj.getProximo()) {
            int vizinho = adj.getVertice() - 1; // Ajuste para 0-based
            if (vizinho == verticeInicial && caminhoAtual.size() > 2) {
                // Encontramos um ciclo
                List<Integer> ciclo = new ArrayList<>(caminhoAtual);
                normalizarEAdicionarCiclo(ciclo);
            } else if (!visitados[vizinho]) {
                dfs(vizinho, verticeInicial, visitados, caminhoAtual);
            }
        }

        // Backtracking
        visitados[verticeAtual] = false;
        caminhoAtual.remove(caminhoAtual.size() - 1);
    }

    private void normalizarEAdicionarCiclo(List<Integer> cicloOriginal) {
        // Gera a representação canônica do ciclo
        List<Integer> cicloNormalizado = obterCicloNormalizado(cicloOriginal);
        // Adiciona apenas se ainda não estiver na lista (evita duplicatas)
        if (!ciclosList.contains(cicloNormalizado)) {
            ciclosList.add(cicloNormalizado);
        }
    }

    private List<Integer> obterCicloNormalizado(List<Integer> ciclo) {
        // Rotacionar para que o ciclo comece com o menor vértice
        List<Integer> rotacionado = rotacionarParaMin(ciclo);

        // Decidir a direção com a sequência lexicograficamente menor
        List<Integer> reverso = new ArrayList<>(rotacionado);
        Collections.reverse(reverso);
        List<Integer> rotacionadoReverso = rotacionarParaMin(reverso);

        // Escolher a representação lexicograficamente menor
        List<Integer> representacao1 = rotacionado;
        List<Integer> representacao2 = rotacionadoReverso;

        // Compare elemento a elemento
        for (int i = 0; i < representacao1.size(); i++) {
            if (representacao1.get(i) < representacao2.get(i)) {
                return representacao1;
            } else if (representacao1.get(i) > representacao2.get(i)) {
                return representacao2;
            }
        }
        return representacao1; // Ambas as representações são iguais
    }

    private List<Integer> rotacionarParaMin(List<Integer> ciclo) {
        int minVertex = Collections.min(ciclo);
        int minIndex = ciclo.indexOf(minVertex);
        List<Integer> rotacionado = new ArrayList<>();
        for (int i = 0; i < ciclo.size(); i++) {
            rotacionado.add(ciclo.get((minIndex + i) % ciclo.size()));
        }
        return rotacionado;
    }

    private void filtrarCiclos() {
        List<List<Integer>> listaFiltrada = new ArrayList<>();
        for (List<Integer> ciclo : ciclosList) {
            Set<Integer> cicloSet = new HashSet<>(ciclo);
            boolean eSubciclo = false;
            for (List<Integer> outroCiclo : ciclosList) {
                if (outroCiclo.size() > ciclo.size()) {
                    Set<Integer> outroCicloSet = new HashSet<>(outroCiclo);
                    if (outroCicloSet.containsAll(cicloSet)) {
                        eSubciclo = true;
                        break;
                    }
                }
            }
            if (!eSubciclo) {
                listaFiltrada.add(ciclo);
            }
        }
        ciclosList = listaFiltrada;
    }
}