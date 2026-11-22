// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

// Impressao na tela
// Os numeros sao convertidos para texto a mao (Utilitarios) e impressos caractere a caractere,
// sem usar conversoes prontas do Java
public class Tela {

    static char[] auxiliar = new char[64];

    static void texto(String mensagem) {
        System.out.print(mensagem);
    }

    static void linha(String mensagem) {
        System.out.println(mensagem);
    }

    static void pularLinha() {
        System.out.println();
    }

    static void chave(char[] chave, int tamanho) {
        for (int i = 0; i < tamanho; i++) {
            System.out.print(chave[i]);
        }
    }

    static void inteiro(int valor) {
        int tamanho = Utilitarios.inteiroParaTexto(valor, auxiliar);
        chave(auxiliar, tamanho);
    }

    static void decimal(double valor, int casas) {
        int tamanho = Utilitarios.decimalParaTexto(valor, casas, auxiliar);
        chave(auxiliar, tamanho);
    }

    // imprime o tempo em milissegundos com 3 casas
    static void tempo(String nomeEtapa, double milissegundos) {
        texto(nomeEtapa);
        decimal(milissegundos, 3);
        linha(" ms");
    }
}
