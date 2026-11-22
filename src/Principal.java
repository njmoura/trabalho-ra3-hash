// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

import java.io.IOException;

// Programa principal do Trabalho RA3 (Tabelas Hash)
// Por enquanto le o Books_rating.csv, monta a tabela de livros (RF3) e conta as linhas descartadas (RF4)
public class Principal {

    static final String ARQUIVO_BASE = "Books_rating.csv";

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

        double inicio;
        double fim;

        // Etapa 1, leitura e indexacao
        Tela.linha("Lendo o arquivo " + ARQUIVO_BASE + " ...");
        inicio = System.nanoTime();
        lerBase(livros);
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
        Tela.texto("Titulos distintos: ");
        Tela.inteiro(livros.total);
        Tela.pularLinha();

        Tela.pularLinha();
        Tela.tempo("Leitura e indexacao: ", tempoLeitura);
    }

    // Le a base inteira e preenche a tabela de livros
    // Linhas sem User_id, sem Title ou com nota invalida sao descartadas e contadas (RF4)
    // Linha com profileName vazio NAO e descartada
    static void lerBase(CadastroLivros livros) throws IOException {
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
                    livros.registrar(leitor.titulo, leitor.tamanhoTitulo, nota);
                    registrosValidos = registrosValidos + 1;
                }
            }
        }
        leitor.fechar();
    }
}
