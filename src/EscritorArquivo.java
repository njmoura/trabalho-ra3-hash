// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

// Escreve arquivos de texto
// Os numeros sao convertidos para texto a mao (Utilitarios)
public class EscritorArquivo {

    BufferedWriter escritor;
    char[] auxiliar; // vetor usado para montar os numeros antes de escrever

    EscritorArquivo(String caminho) throws IOException {
        escritor = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(caminho), "UTF-8"));
        auxiliar = new char[64];
    }

    void escreverCaractere(char c) throws IOException {
        escritor.write(c);
    }

    void escreverChave(char[] chave, int tamanhoChave) throws IOException {
        for (int i = 0; i < tamanhoChave; i++) {
            escreverCaractere(chave[i]);
        }
    }

    // texto fixo (por exemplo o cabecalho do CSV)
    void escreverTexto(String texto) throws IOException {
        escritor.write(texto);
    }

    void escreverInteiro(int valor) throws IOException {
        int quantidade = Utilitarios.inteiroParaTexto(valor, auxiliar);
        escreverChave(auxiliar, quantidade);
    }

    void escreverDecimal(double valor, int casas) throws IOException {
        int quantidade = Utilitarios.decimalParaTexto(valor, casas, auxiliar);
        escreverChave(auxiliar, quantidade);
    }

    // Escreve um campo de texto no formato CSV
    // Se o texto tiver virgula, aspas ou quebra de linha, ele vai entre aspas
    // e cada aspa do texto vira duas aspas ("")
    void escreverCampoCsv(char[] chave, int tamanhoChave) throws IOException {
        int precisaAspas = 0;
        for (int i = 0; i < tamanhoChave; i++) {
            char c = chave[i];
            if (c == ',' || c == '"' || c == '\n' || c == '\r') {
                precisaAspas = 1;
            }
        }

        if (precisaAspas == 0) {
            escreverChave(chave, tamanhoChave);
        } else {
            escreverCaractere('"');
            for (int i = 0; i < tamanhoChave; i++) {
                if (chave[i] == '"') {
                    escreverCaractere('"');
                }
                escreverCaractere(chave[i]);
            }
            escreverCaractere('"');
        }
    }

    void fechar() throws IOException {
        escritor.close();
    }
}
