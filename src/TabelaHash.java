// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

// Tabela hash com encadeamento separado (RF2)
// A capacidade e fixa (m = 1000003) e nunca muda, entao nao existe rehash
// Cada posicao do vetor listas guarda o primeiro no de uma lista encadeada (null quando esta vazia)
// Quando duas chaves caem na mesma posicao, a nova vai para o inicio da lista
public class TabelaHash {

    static final int CAPACIDADE = 1000003;

    No[] listas;
    int tipoHash; // qual funcao hash a tabela usa (ver FuncoesHash)
    int quantidade; // quantas chaves estao guardadas

    // contadores usados na parte experimental
    int colisoes; // insercoes que cairam em uma lista que nao estava vazia
    int ultimasSondagens; // sondagens da ultima operacao (insercao ou busca)
    int piorCaso; // maior numero de sondagens em uma unica operacao

    TabelaHash(int tipoHash) {
        this.tipoHash = tipoHash;
        this.listas = new No[CAPACIDADE];
        this.quantidade = 0;
        this.colisoes = 0;
        this.ultimasSondagens = 0;
        this.piorCaso = 0;
    }

    // posicao = (h & 0x7FFFFFFF) % m
    // a mascara zera o bit de sinal para a posicao nunca ser negativa
    int calcularPosicao(char[] chave, int tamanho) {
        int h = FuncoesHash.calcular(tipoHash, chave, tamanho);
        int posicao = (h & 0x7FFFFFFF) % CAPACIDADE;
        return posicao;
    }

    // guarda as sondagens da operacao e atualiza o pior caso
    void registrarSondagens(int sondagens) {
        ultimasSondagens = sondagens;
        if (sondagens > piorCaso) {
            piorCaso = sondagens;
        }
    }

    // Busca a chave
    // Devolve o indice guardado no no, ou -1 se a chave nao existe
    int buscar(char[] chave, int tamanho) {
        int posicao = calcularPosicao(chave, tamanho);
        No atual = listas[posicao];
        int sondagens = 0;

        while (atual != null) {
            sondagens = sondagens + 1;
            if (Utilitarios.chavesIguais(atual.chave, atual.tamanho, chave, tamanho) == 1) {
                registrarSondagens(sondagens);
                return atual.indice;
            }
            atual = atual.proximo;
        }

        registrarSondagens(sondagens);
        return -1;
    }

    // Insere a chave no inicio da lista da sua posicao
    // Antes percorre a lista para ver se a chave ja existe (comparando caractere a caractere)
    // Se ja existir, nao insere de novo
    // A chave nao e copiada, entao quem chama deve passar um vetor que nao sera reaproveitado
    void inserir(char[] chave, int tamanho, int indice) {
        int posicao = calcularPosicao(chave, tamanho);
        No atual = listas[posicao];
        int sondagens = 0;

        while (atual != null) {
            sondagens = sondagens + 1;
            if (Utilitarios.chavesIguais(atual.chave, atual.tamanho, chave, tamanho) == 1) {
                registrarSondagens(sondagens);
                return;
            }
            atual = atual.proximo;
        }

        // a chave e nova, se a lista ja tinha alguem e uma colisao
        if (listas[posicao] != null) {
            colisoes = colisoes + 1;
        }

        No novo = new No(chave, tamanho, indice, listas[posicao]);
        listas[posicao] = novo;
        quantidade = quantidade + 1;

        registrarSondagens(sondagens);
    }
}
