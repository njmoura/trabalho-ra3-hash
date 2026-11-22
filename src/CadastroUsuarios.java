// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

// Tabela que associa cada User_id ao profileName e ao melhor livro (RF3)
// A tabela hash guarda o User_id e o indice, e os dados ficam nos vetores abaixo, na posicao desse indice
public class CadastroUsuarios {

    TabelaHash tabela;

    char[][] ids;
    int[] tamanhoId;
    char[][] nomes;
    int[] tamanhoNome;
    int[] melhorLivro; // indice do melhor livro no CadastroLivros (-1 enquanto nao foi calculado)
    double[] notaUsuario; // maior nota que o usuario deu ao seu melhor livro
    int total;
    int capacidade;

    CadastroUsuarios(int tipoHash) {
        tabela = new TabelaHash(tipoHash);
        capacidade = 1024;
        ids = new char[capacidade][];
        tamanhoId = new int[capacidade];
        nomes = new char[capacidade][];
        tamanhoNome = new int[capacidade];
        melhorLivro = new int[capacidade];
        notaUsuario = new double[capacidade];
        total = 0;
    }

    void aumentarVetores() {
        int novaCapacidade = capacidade * 2;
        ids = Vetores.aumentar(ids, total, novaCapacidade);
        tamanhoId = Vetores.aumentar(tamanhoId, total, novaCapacidade);
        nomes = Vetores.aumentar(nomes, total, novaCapacidade);
        tamanhoNome = Vetores.aumentar(tamanhoNome, total, novaCapacidade);
        melhorLivro = Vetores.aumentar(melhorLivro, total, novaCapacidade);
        notaUsuario = Vetores.aumentar(notaUsuario, total, novaCapacidade);
        capacidade = novaCapacidade;
    }

    // Registra o usuario (se ainda nao existe) e devolve o indice dele
    // Para o profileName vale o primeiro nome nao vazio encontrado no arquivo
    int registrar(char[] id, int tamanho, char[] nome, int tamanhoDoNome) {
        int indice = tabela.buscar(id, tamanho);

        if (indice == -1) {
            if (total == capacidade) {
                aumentarVetores();
            }
            indice = total;
            char[] copia = Utilitarios.copiarChave(id, tamanho);
            ids[indice] = copia;
            tamanhoId[indice] = tamanho;
            nomes[indice] = Utilitarios.copiarChave(nome, tamanhoDoNome);
            tamanhoNome[indice] = tamanhoDoNome;
            melhorLivro[indice] = -1;
            notaUsuario[indice] = 0;
            tabela.inserir(copia, tamanho, indice);
            total = total + 1;
        } else {
            // usuario ja existia com nome vazio e agora apareceu um nome
            if (tamanhoNome[indice] == 0 && tamanhoDoNome > 0) {
                nomes[indice] = Utilitarios.copiarChave(nome, tamanhoDoNome);
                tamanhoNome[indice] = tamanhoDoNome;
            }
        }
        return indice;
    }

    // Calcula o melhor livro de cada usuario
    // So pode ser feito depois de ler o arquivo inteiro, porque a media de um livro
    // so fica definitiva quando todas as notas dele foram somadas
    // Percorre todas as avaliacoes validas guardadas durante a leitura
    // (melhorLivro comeca com -1 para todos, isso e feito no registrar)
    void calcularMelhoresLivros(ListaAvaliacoes avaliacoes, CadastroLivros livros) {
        for (int k = 0; k < avaliacoes.total; k++) {
            int usuario = avaliacoes.usuario[k];
            int livro = avaliacoes.livro[k];
            double nota = avaliacoes.nota[k];
            int atual = melhorLivro[usuario];

            if (atual == -1) {
                // primeiro livro visto desse usuario
                melhorLivro[usuario] = livro;
                notaUsuario[usuario] = nota;
            } else if (atual == livro) {
                // o usuario avaliou o mesmo livro de novo, entao vale a maior nota
                if (nota > notaUsuario[usuario]) {
                    notaUsuario[usuario] = nota;
                }
            } else if (livros.compararLivros(livro, atual) > 0) {
                // achou um livro melhor
                melhorLivro[usuario] = livro;
                notaUsuario[usuario] = nota;
            }
        }
    }

    // Ordem da lista ordenada (RF9), devolve 1 se o usuario "a" deve vir antes do "b"
    // Vem antes quem tem maior media_livro, se empatar quem tem maior N_livro
    // e se empatar de novo o User_id que vem antes em ordem alfabetica
    int vemAntes(int a, int b, CadastroLivros livros) {
        int livroA = melhorLivro[a];
        int livroB = melhorLivro[b];

        int resultado = livros.compararMedias(livroA, livroB);
        if (resultado > 0) {
            return 1;
        }
        if (resultado < 0) {
            return 0;
        }

        if (livros.quantidade[livroA] > livros.quantidade[livroB]) {
            return 1;
        }
        if (livros.quantidade[livroA] < livros.quantidade[livroB]) {
            return 0;
        }

        int ordem = Utilitarios.compararChaves(ids[a], tamanhoId[a], ids[b], tamanhoId[b]);
        if (ordem <= 0) {
            return 1;
        }
        return 0;
    }
}
