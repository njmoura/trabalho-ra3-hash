// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

// Le o arquivo Books_rating.csv caractere por caractere e monta cada registro a mao
// Trata campos entre aspas, que podem ter virgulas, aspas duplicadas ("") e quebras de linha
// So guarda os campos que o trabalho usa, que sao Title (1), User_id (3), profileName (4) e review/score (6)
public class LeitorCsv {

    static final int TAMANHO_MAXIMO_CAMPO = 20000;

    static final int CAMPO_TITULO = 1;
    static final int CAMPO_USUARIO = 3;
    static final int CAMPO_NOME = 4;
    static final int CAMPO_NOTA = 6;

    BufferedReader leitor;

    // campos do registro atual
    char[] titulo = new char[TAMANHO_MAXIMO_CAMPO];
    int tamanhoTitulo;
    char[] usuario = new char[TAMANHO_MAXIMO_CAMPO];
    int tamanhoUsuario;
    char[] nome = new char[TAMANHO_MAXIMO_CAMPO];
    int tamanhoNome;
    char[] nota = new char[TAMANHO_MAXIMO_CAMPO];
    int tamanhoNota;

    LeitorCsv(String caminho) throws IOException {
        leitor = new BufferedReader(new InputStreamReader(new FileInputStream(caminho), "UTF-8"));
    }

    // Guarda o caractere no campo certo (os outros campos sao ignorados)
    void guardar(int campo, char c) {
        if (campo == CAMPO_TITULO) {
            if (tamanhoTitulo < TAMANHO_MAXIMO_CAMPO) {
                titulo[tamanhoTitulo] = c;
                tamanhoTitulo = tamanhoTitulo + 1;
            }
        } else if (campo == CAMPO_USUARIO) {
            if (tamanhoUsuario < TAMANHO_MAXIMO_CAMPO) {
                usuario[tamanhoUsuario] = c;
                tamanhoUsuario = tamanhoUsuario + 1;
            }
        } else if (campo == CAMPO_NOME) {
            if (tamanhoNome < TAMANHO_MAXIMO_CAMPO) {
                nome[tamanhoNome] = c;
                tamanhoNome = tamanhoNome + 1;
            }
        } else if (campo == CAMPO_NOTA) {
            if (tamanhoNota < TAMANHO_MAXIMO_CAMPO) {
                nota[tamanhoNota] = c;
                tamanhoNota = tamanhoNota + 1;
            }
        }
    }

    // Le um registro inteiro (uma avaliacao)
    // Devolve 1 se leu um registro e 0 se o arquivo acabou
    int lerRegistro() throws IOException {
        tamanhoTitulo = 0;
        tamanhoUsuario = 0;
        tamanhoNome = 0;
        tamanhoNota = 0;

        int campo = 0; // numero do campo atual (0 a 9)
        int dentroDeAspas = 0; // 1 quando estamos dentro de um campo entre aspas
        int ultimoFoiAspa = 0; // 1 quando o caractere anterior fechou as aspas
        int leuAlgo = 0;

        // read devolve o proximo caractere do arquivo, ou -1 quando o arquivo acaba
        int lido = leitor.read();
        while (lido != -1) {
            leuAlgo = 1;
            char c = (char) lido;

            if (dentroDeAspas == 1) {
                if (c == '"') {
                    // pode ser o fim das aspas ou o comeco de uma aspa dupla ("")
                    dentroDeAspas = 0;
                    ultimoFoiAspa = 1;
                } else {
                    // dentro das aspas, virgula e quebra de linha fazem parte do texto
                    guardar(campo, c);
                }
            } else {
                if (c == '"') {
                    if (ultimoFoiAspa == 1) {
                        // apareceu "" dentro do campo, entao e uma aspa de verdade
                        guardar(campo, '"');
                    }
                    dentroDeAspas = 1;
                } else if (c == ',') {
                    campo = campo + 1;
                } else if (c == '\n') {
                    // quebra de linha fora das aspas quer dizer que o registro terminou
                    return 1;
                } else if (c != '\r') {
                    guardar(campo, c);
                }
                ultimoFoiAspa = 0;
            }

            lido = leitor.read();
        }

        // chegou no fim do arquivo, se tinha algo no ultimo registro ele conta
        return leuAlgo;
    }

    void fechar() throws IOException {
        leitor.close();
    }
}
