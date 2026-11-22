// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

import java.io.IOException;

// Parte experimental (secao 5.3 do enunciado)
// Para cada funcao hash e cada conjunto de chaves (usuarios e titulos), cria uma tabela nova e vazia
// com capacidade 1000003 e mede colisoes, sondagens (sucesso e falha), pior caso e tempos
// Os tempos sao a media de 3 execucoes e as outras medidas nao mudam entre execucoes
public class Experimento {

    static final int EXECUCOES = 3;

    static void executar(String nomeConjunto, char[][] chaves, int[] tamanhos, int total,
                         EscritorArquivo saida) throws IOException {

        // vetor para montar a chave da busca sem sucesso (chave + caractere de codigo 1)
        int maiorTamanho = 0;
        for (int i = 0; i < total; i++) {
            if (tamanhos[i] > maiorTamanho) {
                maiorTamanho = tamanhos[i];
            }
        }
        char[] chaveInexistente = new char[maiorTamanho + 1];

        Tela.pularLinha();
        Tela.texto("=== Experimento: ");
        Tela.texto(nomeConjunto);
        Tela.texto(" (");
        Tela.inteiro(total);
        Tela.linha(" chaves distintas) ===");
        Tela.linha("Funcao         | Colisoes | Sond. sucesso | Sond. falha | Pior caso | Insercao (ms) | Busca (ms)");

        for (int tipo = 1; tipo <= 3; tipo++) {
            double somaTempoInsercao = 0;
            double somaTempoBusca = 0;
            int colisoes = 0;
            int piorCaso = 0;
            double mediaSucesso = 0;
            double mediaFalha = 0;

            for (int execucao = 0; execucao < EXECUCOES; execucao++) {
                TabelaHash tabela = new TabelaHash(tipo);

                // insercao de todas as chaves distintas
                double inicio = System.nanoTime();
                for (int i = 0; i < total; i++) {
                    tabela.inserir(chaves[i], tamanhos[i], i);
                }
                double fim = System.nanoTime();
                somaTempoInsercao = somaTempoInsercao + (fim - inicio) / 1000000.0;

                // busca com sucesso, cada chave inserida e buscada uma vez
                int sondagensSucesso = 0;
                inicio = System.nanoTime();
                for (int i = 0; i < total; i++) {
                    tabela.buscar(chaves[i], tamanhos[i]);
                    sondagensSucesso = sondagensSucesso + tabela.ultimasSondagens;
                }
                fim = System.nanoTime();
                somaTempoBusca = somaTempoBusca + (fim - inicio) / 1000000.0;

                // busca sem sucesso, usando a chave com um caractere de codigo 1 no final
                int sondagensFalha = 0;
                for (int i = 0; i < total; i++) {
                    for (int j = 0; j < tamanhos[i]; j++) {
                        chaveInexistente[j] = chaves[i][j];
                    }
                    chaveInexistente[tamanhos[i]] = (char) 1;
                    tabela.buscar(chaveInexistente, tamanhos[i] + 1);
                    sondagensFalha = sondagensFalha + tabela.ultimasSondagens;
                }

                colisoes = tabela.colisoes;
                piorCaso = tabela.piorCaso;
                mediaSucesso = (double) sondagensSucesso / total;
                mediaFalha = (double) sondagensFalha / total;
            }

            double mediaInsercao = somaTempoInsercao / EXECUCOES;
            double mediaBusca = somaTempoBusca / EXECUCOES;

            // mostra na tela
            Tela.texto(FuncoesHash.nome(tipo));
            Tela.texto(" | ");
            Tela.inteiro(colisoes);
            Tela.texto(" | ");
            Tela.decimal(mediaSucesso, 4);
            Tela.texto(" | ");
            Tela.decimal(mediaFalha, 4);
            Tela.texto(" | ");
            Tela.inteiro(piorCaso);
            Tela.texto(" | ");
            Tela.decimal(mediaInsercao, 3);
            Tela.texto(" | ");
            Tela.decimal(mediaBusca, 3);
            Tela.pularLinha();

            // grava no arquivo de resultados
            saida.escreverTexto(nomeConjunto);
            saida.escreverCaractere(',');
            saida.escreverTexto(FuncoesHash.nome(tipo));
            saida.escreverCaractere(',');
            saida.escreverInteiro(colisoes);
            saida.escreverCaractere(',');
            saida.escreverDecimal(mediaSucesso, 4);
            saida.escreverCaractere(',');
            saida.escreverDecimal(mediaFalha, 4);
            saida.escreverCaractere(',');
            saida.escreverInteiro(piorCaso);
            saida.escreverCaractere(',');
            saida.escreverDecimal(mediaInsercao, 3);
            saida.escreverCaractere(',');
            saida.escreverDecimal(mediaBusca, 3);
            saida.escreverCaractere('\n');
        }
    }
}
