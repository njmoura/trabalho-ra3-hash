// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

// Tabela que associa cada Title a soma das notas e ao N (RF3)
// A tabela hash guarda o titulo e o indice, e os dados ficam nos vetores abaixo, na posicao desse indice
public class CadastroLivros {

    TabelaHash tabela;

    char[][] titulos;
    int[] tamanhoTitulo;
    double[] soma; // soma de todas as notas do livro
    int[] quantidade; // N, quantidade de avaliacoes do livro
    int total; // quantos livros (titulos distintos) existem
    int capacidade; // tamanho atual dos vetores

    CadastroLivros(int tipoHash) {
        tabela = new TabelaHash(tipoHash);
        capacidade = 1024;
        titulos = new char[capacidade][];
        tamanhoTitulo = new int[capacidade];
        soma = new double[capacidade];
        quantidade = new int[capacidade];
        total = 0;
    }

    // dobra o tamanho dos vetores quando eles enchem
    void aumentarVetores() {
        int novaCapacidade = capacidade * 2;
        titulos = Vetores.aumentar(titulos, total, novaCapacidade);
        tamanhoTitulo = Vetores.aumentar(tamanhoTitulo, total, novaCapacidade);
        soma = Vetores.aumentar(soma, total, novaCapacidade);
        quantidade = Vetores.aumentar(quantidade, total, novaCapacidade);
        capacidade = novaCapacidade;
    }

    // Soma a nota ao livro
    // Se o titulo ainda nao existe, ele e criado
    // Devolve o indice do livro
    int registrar(char[] titulo, int tamanho, double nota) {
        int indice = tabela.buscar(titulo, tamanho);

        if (indice == -1) {
            if (total == capacidade) {
                aumentarVetores();
            }
            indice = total;
            // copia o titulo, porque o vetor do leitor e reaproveitado na proxima linha
            char[] copia = Utilitarios.copiarChave(titulo, tamanho);
            titulos[indice] = copia;
            tamanhoTitulo[indice] = tamanho;
            soma[indice] = 0;
            quantidade[indice] = 0;
            tabela.inserir(copia, tamanho, indice);
            total = total + 1;
        }

        soma[indice] = soma[indice] + nota;
        quantidade[indice] = quantidade[indice] + 1;
        return indice;
    }

    double media(int livro) {
        return soma[livro] / quantidade[livro];
    }

    // Compara so as medias de dois livros, sem fazer divisao
    // somaA / nA > somaB / nB e o mesmo que somaA * nB > somaB * nA
    // Devolve 1 se a media de "a" e maior, -1 se e menor e 0 se sao iguais
    int compararMedias(int a, int b) {
        double esquerda = soma[a] * quantidade[b];
        double direita = soma[b] * quantidade[a];
        if (esquerda > direita) {
            return 1;
        }
        if (esquerda < direita) {
            return -1;
        }
        return 0;
    }

    // Decide qual livro e o melhor
    // Ganha o de maior media, se empatar ganha o de maior N e se empatar de novo
    // ganha o titulo que vem antes em ordem alfabetica
    // Devolve 1 se "a" e melhor que "b", -1 se "b" e melhor e 0 se for o mesmo livro
    int compararLivros(int a, int b) {
        int resultado = compararMedias(a, b);
        if (resultado != 0) {
            return resultado;
        }

        if (quantidade[a] > quantidade[b]) {
            return 1;
        }
        if (quantidade[a] < quantidade[b]) {
            return -1;
        }

        int ordem = Utilitarios.compararChaves(titulos[a], tamanhoTitulo[a], titulos[b], tamanhoTitulo[b]);
        if (ordem < 0) {
            return 1;
        }
        if (ordem > 0) {
            return -1;
        }
        return 0;
    }
}
