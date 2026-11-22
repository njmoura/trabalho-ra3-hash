// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

// As tres funcoes hash pedidas no trabalho (RF1)
// Todas recebem a chave como vetor de char e o tamanho (quantidade de caracteres validos),
// porque o trabalho nao deixa pegar o tamanho do vetor pronto
// As contas sao feitas com int de 32 bits e o estouro e esperado e faz parte das funcoes
public class FuncoesHash {

    // codigos usados para escolher a funcao hash
    static final int FNV1A = 1;
    static final int POLINOMIAL_31 = 2;
    static final int DJB2 = 3;

    // A FNV1a comeca em 2166136261 (0x811C9DC5), faz XOR com o caractere e multiplica pelo primo FNV
    static int hashFnv1a(char[] chave, int tamanho) {
        int h = 0x811C9DC5;
        for (int i = 0; i < tamanho; i++) {
            h = h ^ chave[i];
            h = h * 16777619;
        }
        return h;
    }

    // Polinomial de base 31 (regra de Horner), faz h = 31 * h + c
    static int hashPolinomial31(char[] chave, int tamanho) {
        int h = 0;
        for (int i = 0; i < tamanho; i++) {
            h = 31 * h + chave[i];
        }
        return h;
    }

    // A djb2 comeca em 5381 e faz h = 33 * h + c
    static int hashDjb2(char[] chave, int tamanho) {
        int h = 5381;
        for (int i = 0; i < tamanho; i++) {
            h = 33 * h + chave[i];
        }
        return h;
    }

    // chama a funcao hash escolhida pelo codigo
    static int calcular(int tipo, char[] chave, int tamanho) {
        if (tipo == FNV1A) {
            return hashFnv1a(chave, tamanho);
        } else if (tipo == POLINOMIAL_31) {
            return hashPolinomial31(chave, tamanho);
        } else {
            return hashDjb2(chave, tamanho);
        }
    }

    // nome da funcao, para mostrar na tela e nos arquivos
    static String nome(int tipo) {
        if (tipo == FNV1A) {
            return "FNV1a";
        } else if (tipo == POLINOMIAL_31) {
            return "Polinomial 31";
        } else {
            return "djb2";
        }
    }
}
