// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

// No da lista encadeada que fica em cada posicao da tabela hash
// Guarda a chave, o tamanho da chave (usado no lugar de chave.length),
// o indice onde estao os dados dessa chave no cadastro e a referencia para o proximo no
public class No {
    char[] chave; // chave guardada
    int tamanho; // quantidade de caracteres da chave
    int indice; // indice dos dados no cadastro (livros ou usuarios)
    No proximo; // proximo no da lista

    // Construtor do no
    No(char[] chave, int tamanho, int indice, No proximo) {
        this.chave = chave;
        this.tamanho = tamanho;
        this.indice = indice;
        this.proximo = proximo;
    }
}
