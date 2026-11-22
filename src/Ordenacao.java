// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

// Merge sort feito a mao para a lista ordenada de usuarios (RF9)
// Ordena um vetor com os indices dos usuarios, usando a regra de CadastroUsuarios.vemAntes
// Escolhemos o merge sort porque ele e sempre O(n log n), mesmo no pior caso,
// enquanto o quick sort pode cair para O(n^2)
// A base tem muitos empates (muitos usuarios com media 5.0) e o merge sort nao piora com isso
// O custo e um vetor auxiliar do mesmo tamanho
public class Ordenacao {

    static void ordenarUsuarios(int[] vetor, int quantidade, CadastroUsuarios usuarios, CadastroLivros livros) {
        int[] auxiliar = new int[quantidade];
        ordenarParte(vetor, auxiliar, 0, quantidade - 1, usuarios, livros);
    }

    // ordena o pedaco do vetor que vai de "inicio" ate "fim" (os dois inclusos)
    static void ordenarParte(int[] vetor, int[] auxiliar, int inicio, int fim,
                             CadastroUsuarios usuarios, CadastroLivros livros) {
        if (inicio >= fim) {
            return;
        }
        int meio = (inicio + fim) / 2;
        ordenarParte(vetor, auxiliar, inicio, meio, usuarios, livros);
        ordenarParte(vetor, auxiliar, meio + 1, fim, usuarios, livros);
        intercalar(vetor, auxiliar, inicio, meio, fim, usuarios, livros);
    }

    // junta as duas metades ja ordenadas, de inicio ate meio e de meio + 1 ate fim
    static void intercalar(int[] vetor, int[] auxiliar, int inicio, int meio, int fim,
                           CadastroUsuarios usuarios, CadastroLivros livros) {
        for (int i = inicio; i <= fim; i++) {
            auxiliar[i] = vetor[i];
        }

        int esquerda = inicio;
        int direita = meio + 1;
        int posicao = inicio;

        while (esquerda <= meio && direita <= fim) {
            if (usuarios.vemAntes(auxiliar[esquerda], auxiliar[direita], livros) == 1) {
                vetor[posicao] = auxiliar[esquerda];
                esquerda = esquerda + 1;
            } else {
                vetor[posicao] = auxiliar[direita];
                direita = direita + 1;
            }
            posicao = posicao + 1;
        }

        // copia o que sobrou da metade da esquerda
        while (esquerda <= meio) {
            vetor[posicao] = auxiliar[esquerda];
            esquerda = esquerda + 1;
            posicao = posicao + 1;
        }

        // copia o que sobrou da metade da direita
        while (direita <= fim) {
            vetor[posicao] = auxiliar[direita];
            direita = direita + 1;
            posicao = posicao + 1;
        }
    }
}
