import networkx as nx
import random

def gerar_grafo_erdos_renyi_conexo(num_vertices, probabilidade):
    while True:
        grafo = nx.erdos_renyi_graph(num_vertices, probabilidade)
        if nx.is_connected(grafo):
            break
    return grafo

def salvar_grafo_txt(nome_arquivo, grafo):
    num_vertices = grafo.number_of_nodes()
    arestas = grafo.edges()
    
    with open(nome_arquivo, 'w') as file:
        file.write(f"{num_vertices}\n")
        for aresta in arestas:
            file.write(f"{aresta[0]+1} {aresta[1]+1}\n")  # +1 para converter para 1-indexado, como no seu código original
    print(f"Grafo salvo no arquivo {nome_arquivo}")

def imprimir_componentes_biconexos(grafo):
    # Encontra os componentes biconexos
    biconexos = list(nx.biconnected_components(grafo))
    
    print(f"Número de componentes biconexos: {len(biconexos)}")
    for idx, componente in enumerate(biconexos, start=1):
        print(f"Componente Biconexo {idx}: {componente}")

# Parâmetros de geração do grafo
num_vertices = 100  # Número de vértices
probabilidade = 0.05  # Probabilidade de existência de uma aresta entre dois vértices

# Gera o grafo e salva em um arquivo
grafo_conexo = gerar_grafo_erdos_renyi_conexo(num_vertices, probabilidade)
salvar_grafo_txt('grafo.txt', grafo_conexo)

# Imprime os componentes biconexos
imprimir_componentes_biconexos(grafo_conexo)
