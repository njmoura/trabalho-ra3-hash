// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

// Guarda todas as avaliacoes validas lidas do arquivo, com usuario, livro e nota
// Usuario e livro sao os indices nos cadastros
// Isso e necessario porque o melhor livro so pode ser decidido depois de ler o arquivo todo
public class ListaAvaliacoes {

    int[] usuario;
    int[] livro;
    double[] nota;
    int total;
    int capacidade;

    ListaAvaliacoes() {
        capacidade = 1024;
        usuario = new int[capacidade];
        livro = new int[capacidade];
        nota = new double[capacidade];
        total = 0;
    }

    void adicionar(int indiceUsuario, int indiceLivro, double valorNota) {
        if (total == capacidade) {
            int novaCapacidade = capacidade * 2;
            usuario = Vetores.aumentar(usuario, total, novaCapacidade);
            livro = Vetores.aumentar(livro, total, novaCapacidade);
            nota = Vetores.aumentar(nota, total, novaCapacidade);
            capacidade = novaCapacidade;
        }
        usuario[total] = indiceUsuario;
        livro[total] = indiceLivro;
        nota[total] = valorNota;
        total = total + 1;
    }
}
