// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

// Aumenta o tamanho dos vetores quando eles enchem
// Como Arrays.copyOf e System.arraycopy sao proibidos, a copia e feita com um for
public class Vetores {

    static int[] aumentar(int[] antigo, int quantidadeUsada, int novoTamanho) {
        int[] novo = new int[novoTamanho];
        for (int i = 0; i < quantidadeUsada; i++) {
            novo[i] = antigo[i];
        }
        return novo;
    }

    static double[] aumentar(double[] antigo, int quantidadeUsada, int novoTamanho) {
        double[] novo = new double[novoTamanho];
        for (int i = 0; i < quantidadeUsada; i++) {
            novo[i] = antigo[i];
        }
        return novo;
    }

    static char[][] aumentar(char[][] antigo, int quantidadeUsada, int novoTamanho) {
        char[][] novo = new char[novoTamanho][];
        for (int i = 0; i < quantidadeUsada; i++) {
            novo[i] = antigo[i];
        }
        return novo;
    }
}
