// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

// Programa principal do Trabalho RA3 (Tabelas Hash)
// Primeiro le o Books_rating.csv e monta as tabelas (RF3 e RF4), depois calcula o melhor livro
// de cada usuario e grava o usuario_melhor_livro.csv (RF5)
// Em seguida ordena os usuarios com merge sort e grava o usuario_melhor_livro_ordenado.csv (RF9),
// mostra o top 10 de livros (RF7), faz as consultas do user_id.txt (RF6) e mostra os tempos (RF8)
public class Principal {

    static final String ARQUIVO_BASE = "Books_rating.csv";
    static final String ARQUIVO_CONSULTA = "user_id.txt";
    static final String ARQUIVO_SAIDA = "usuario_melhor_livro.csv";
    static final String ARQUIVO_SAIDA_ORDENADO = "usuario_melhor_livro_ordenado.csv";

    // funcao hash usada nas tabelas principais
    static final int HASH_PRINCIPAL = FuncoesHash.FNV1A;

    // contadores da leitura (RF4)
    static int registrosLidos = 0;
    static int registrosValidos = 0;
    static int descartadosSemUsuario = 0;
    static int descartadosSemTitulo = 0;
    static int descartadosNotaInvalida = 0;

    public static void main(String[] args) throws IOException {
        CadastroLivros livros = new CadastroLivros(HASH_PRINCIPAL);
        CadastroUsuarios usuarios = new CadastroUsuarios(HASH_PRINCIPAL);
        ListaAvaliacoes avaliacoes = new ListaAvaliacoes();

        double inicio;
        double fim;

        // Etapa 1, leitura e indexacao
        Tela.linha("Lendo o arquivo " + ARQUIVO_BASE + " ...");
        inicio = System.nanoTime();
        lerBase(livros, usuarios, avaliacoes);
        fim = System.nanoTime();
        double tempoLeitura = (fim - inicio) / 1000000.0;

        Tela.texto("Registros lidos: ");
        Tela.inteiro(registrosLidos);
        Tela.pularLinha();
        Tela.texto("Registros validos: ");
        Tela.inteiro(registrosValidos);
        Tela.pularLinha();
        Tela.texto("Descartados sem User_id: ");
        Tela.inteiro(descartadosSemUsuario);
        Tela.pularLinha();
        Tela.texto("Descartados sem Title: ");
        Tela.inteiro(descartadosSemTitulo);
        Tela.pularLinha();
        Tela.texto("Descartados com nota invalida: ");
        Tela.inteiro(descartadosNotaInvalida);
        Tela.pularLinha();
        Tela.texto("Total descartado: ");
        Tela.inteiro(descartadosSemUsuario + descartadosSemTitulo + descartadosNotaInvalida);
        Tela.pularLinha();
        Tela.texto("Usuarios distintos: ");
        Tela.inteiro(usuarios.total);
        Tela.pularLinha();
        Tela.texto("Titulos distintos: ");
        Tela.inteiro(livros.total);
        Tela.pularLinha();

        // Etapa 2, calculo do melhor livro
        inicio = System.nanoTime();
        usuarios.calcularMelhoresLivros(avaliacoes, livros);
        fim = System.nanoTime();
        double tempoMelhorLivro = (fim - inicio) / 1000000.0;

        // Etapa 3, gravacao do arquivo na ordem em que os usuarios apareceram
        int[] ordemOriginal = new int[usuarios.total];
        for (int i = 0; i < usuarios.total; i++) {
            ordemOriginal[i] = i;
        }
        inicio = System.nanoTime();
        gravarArquivo(ARQUIVO_SAIDA, ordemOriginal, usuarios, livros);
        fim = System.nanoTime();
        double tempoGravacao = (fim - inicio) / 1000000.0;

        // Etapa 4, ordenacao com merge sort
        int[] ordenado = new int[usuarios.total];
        for (int i = 0; i < usuarios.total; i++) {
            ordenado[i] = i;
        }
        inicio = System.nanoTime();
        Ordenacao.ordenarUsuarios(ordenado, usuarios.total, usuarios, livros);
        fim = System.nanoTime();
        double tempoOrdenacao = (fim - inicio) / 1000000.0;

        inicio = System.nanoTime();
        gravarArquivo(ARQUIVO_SAIDA_ORDENADO, ordenado, usuarios, livros);
        fim = System.nanoTime();
        double tempoGravacaoOrdenado = (fim - inicio) / 1000000.0;

        // Etapa 5, top 10 livros com N >= 100
        inicio = System.nanoTime();
        mostrarTop10(livros);
        fim = System.nanoTime();
        double tempoTop10 = (fim - inicio) / 1000000.0;

        // Etapa 6, consultas
        inicio = System.nanoTime();
        fazerConsultas(usuarios, livros);
        fim = System.nanoTime();
        double tempoConsultas = (fim - inicio) / 1000000.0;

        // Etapa 7, tempos de cada etapa
        Tela.pularLinha();
        Tela.linha("=== Tempos de cada etapa ===");
        Tela.tempo("Leitura e indexacao: ", tempoLeitura);
        Tela.tempo("Calculo do melhor livro: ", tempoMelhorLivro);
        Tela.tempo("Ordenacao (merge sort): ", tempoOrdenacao);
        Tela.tempo("Gravacao de " + ARQUIVO_SAIDA + ": ", tempoGravacao);
        Tela.tempo("Gravacao de " + ARQUIVO_SAIDA_ORDENADO + ": ", tempoGravacaoOrdenado);
        Tela.tempo("Gravacao dos arquivos (total): ", tempoGravacao + tempoGravacaoOrdenado);
        Tela.tempo("Ranking top 10: ", tempoTop10);
        Tela.tempo("Consultas (" + ARQUIVO_CONSULTA + "): ", tempoConsultas);

        Tela.pularLinha();
        Tela.linha("Arquivos gerados: " + ARQUIVO_SAIDA + ", " + ARQUIVO_SAIDA_ORDENADO);
    }

    // Le a base inteira e preenche as duas tabelas e a lista de avaliacoes
    // Linhas sem User_id, sem Title ou com nota invalida sao descartadas e contadas (RF4)
    // Linha com profileName vazio NAO e descartada
    static void lerBase(CadastroLivros livros, CadastroUsuarios usuarios, ListaAvaliacoes avaliacoes) throws IOException {
        LeitorCsv leitor = new LeitorCsv(ARQUIVO_BASE);

        // a primeira linha e o cabecalho
        leitor.lerRegistro();

        while (leitor.lerRegistro() == 1) {
            registrosLidos = registrosLidos + 1;

            if (leitor.tamanhoUsuario == 0) {
                descartadosSemUsuario = descartadosSemUsuario + 1;
            } else if (leitor.tamanhoTitulo == 0) {
                descartadosSemTitulo = descartadosSemTitulo + 1;
            } else {
                double nota = Utilitarios.converterNota(leitor.nota, leitor.tamanhoNota);
                if (nota < 0) {
                    descartadosNotaInvalida = descartadosNotaInvalida + 1;
                } else {
                    int livro = livros.registrar(leitor.titulo, leitor.tamanhoTitulo, nota);
                    int usuario = usuarios.registrar(leitor.usuario, leitor.tamanhoUsuario,
                                                     leitor.nome, leitor.tamanhoNome);
                    avaliacoes.adicionar(usuario, livro, nota);
                    registrosValidos = registrosValidos + 1;
                }
            }
        }
        leitor.fechar();
    }

    // Grava um arquivo com as colunas do RF5, seguindo a ordem do vetor "ordem"
    static void gravarArquivo(String nomeArquivo, int[] ordem, CadastroUsuarios usuarios,
                              CadastroLivros livros) throws IOException {
        EscritorArquivo escritor = new EscritorArquivo(nomeArquivo);
        escritor.escreverTexto("User_id,profileName,Title,media_livro,N_livro,nota_usuario\n");

        for (int k = 0; k < usuarios.total; k++) {
            int u = ordem[k];
            int livro = usuarios.melhorLivro[u];

            escritor.escreverCampoCsv(usuarios.ids[u], usuarios.tamanhoId[u]);
            escritor.escreverCaractere(',');
            escritor.escreverCampoCsv(usuarios.nomes[u], usuarios.tamanhoNome[u]);
            escritor.escreverCaractere(',');
            escritor.escreverCampoCsv(livros.titulos[livro], livros.tamanhoTitulo[livro]);
            escritor.escreverCaractere(',');
            escritor.escreverDecimal(livros.media(livro), 4);
            escritor.escreverCaractere(',');
            escritor.escreverInteiro(livros.quantidade[livro]);
            escritor.escreverCaractere(',');
            escritor.escreverDecimal(usuarios.notaUsuario[u], 1);
            escritor.escreverCaractere('\n');
        }
        escritor.fechar();
    }

    // Ranking dos 10 livros de maior media com N >= 100 (RF7)
    // Faz 10 passadas e em cada uma escolhe o melhor livro que ainda nao foi escolhido
    // O desempate e igual ao do melhor livro, maior N e depois titulo em ordem alfabetica
    static void mostrarTop10(CadastroLivros livros) {
        int[] jaEscolhido = new int[livros.total];

        Tela.pularLinha();
        Tela.linha("=== Top 10 livros de maior media (N >= 100) ===");

        for (int posicao = 1; posicao <= 10; posicao++) {
            int melhor = -1;
            for (int i = 0; i < livros.total; i++) {
                if (livros.quantidade[i] >= 100 && jaEscolhido[i] == 0) {
                    if (melhor == -1) {
                        melhor = i;
                    } else if (livros.compararLivros(i, melhor) > 0) {
                        melhor = i;
                    }
                }
            }

            if (melhor == -1) {
                Tela.linha("(nao ha mais livros com N >= 100)");
                return;
            }

            jaEscolhido[melhor] = 1;
            Tela.inteiro(posicao);
            Tela.texto(". ");
            Tela.chave(livros.titulos[melhor], livros.tamanhoTitulo[melhor]);
            Tela.texto(" | media ");
            Tela.decimal(livros.media(melhor), 4);
            Tela.texto(" | N = ");
            Tela.inteiro(livros.quantidade[melhor]);
            Tela.pularLinha();
        }
    }

    // Consulta pontual (RF6), le o user_id.txt (um User_id por linha) e mostra
    // o profileName e o melhor livro de cada usuario
    static void fazerConsultas(CadastroUsuarios usuarios, CadastroLivros livros) {
        Tela.pularLinha();
        Tela.linha("=== Consultas do arquivo " + ARQUIVO_CONSULTA + " ===");

        try {
            BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(new FileInputStream(ARQUIVO_CONSULTA), "UTF-8"));
            char[] linha = new char[1000];
            int tamanho = 0;

            int lido = leitor.read();
            while (lido != -1) {
                char c = (char) lido;
                if (c == '\n') {
                    consultarUsuario(linha, tamanho, usuarios, livros);
                    tamanho = 0;
                } else if (c != '\r' && c != ' ' && c != '\t' && c != 65279) {
                    // ignora espacos, \r e o caractere 65279 (BOM), que o Bloco de Notas as vezes coloca no inicio
                    if (tamanho < 1000) {
                        linha[tamanho] = c;
                        tamanho = tamanho + 1;
                    }
                }
                lido = leitor.read();
            }
            // ultima linha sem \n no final
            consultarUsuario(linha, tamanho, usuarios, livros);
            leitor.close();
        } catch (IOException erro) {
            Tela.linha("Nao foi possivel ler o arquivo " + ARQUIVO_CONSULTA);
        }
    }

    static void consultarUsuario(char[] id, int tamanho, CadastroUsuarios usuarios, CadastroLivros livros) {
        if (tamanho == 0) {
            return; // linha em branco
        }

        Tela.texto("User_id: ");
        Tela.chave(id, tamanho);

        int u = usuarios.tabela.buscar(id, tamanho);
        if (u == -1) {
            Tela.linha(" -> usuario nao encontrado");
            return;
        }

        int livro = usuarios.melhorLivro[u];
        Tela.pularLinha();
        Tela.texto("   profileName: ");
        Tela.chave(usuarios.nomes[u], usuarios.tamanhoNome[u]);
        Tela.pularLinha();
        Tela.texto("   Melhor livro: ");
        Tela.chave(livros.titulos[livro], livros.tamanhoTitulo[livro]);
        Tela.pularLinha();
        Tela.texto("   Media do livro: ");
        Tela.decimal(livros.media(livro), 4);
        Tela.texto(" | N = ");
        Tela.inteiro(livros.quantidade[livro]);
        Tela.texto(" | nota do usuario: ");
        Tela.decimal(usuarios.notaUsuario[u], 1);
        Tela.pularLinha();
    }
}
