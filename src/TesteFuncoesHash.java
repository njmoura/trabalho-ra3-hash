// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

// Teste rapido das funcoes hash com os valores de referencia do enunciado (Tabela 5)
// Para a chave Ana o esperado e 65972 na Polinomial 31, 193451509 na djb2 e 1991636261 na FNV1a
// Com m = 1000003 a posicao esperada e 630288 na FNV1a e 65972 na Polinomial 31
// A chave A1RR1W4ZYRAXCN na Polinomial 31 deve ir para a posicao 369858
public class TesteFuncoesHash {

    public static void main(String[] args) {
        char[] ana = {'A', 'n', 'a'};
        TabelaHash tabelaFnv = new TabelaHash(FuncoesHash.FNV1A);
        TabelaHash tabelaPolinomial = new TabelaHash(FuncoesHash.POLINOMIAL_31);

        Tela.texto("FNV1a(Ana) = ");
        Tela.inteiro(FuncoesHash.hashFnv1a(ana, 3));
        Tela.linha("   (esperado 1991636261)");

        Tela.texto("Polinomial31(Ana) = ");
        Tela.inteiro(FuncoesHash.hashPolinomial31(ana, 3));
        Tela.linha("   (esperado 65972)");

        Tela.texto("djb2(Ana) = ");
        Tela.inteiro(FuncoesHash.hashDjb2(ana, 3));
        Tela.linha("   (esperado 193451509)");

        Tela.texto("Posicao FNV1a(Ana) = ");
        Tela.inteiro(tabelaFnv.calcularPosicao(ana, 3));
        Tela.linha("   (esperado 630288)");

        Tela.texto("Posicao Polinomial31(Ana) = ");
        Tela.inteiro(tabelaPolinomial.calcularPosicao(ana, 3));
        Tela.linha("   (esperado 65972)");

        char[] usuario = {'A', '1', 'R', 'R', '1', 'W', '4', 'Z', 'Y', 'R', 'A', 'X', 'C', 'N'};
        Tela.texto("Posicao Polinomial31(A1RR1W4ZYRAXCN) = ");
        Tela.inteiro(tabelaPolinomial.calcularPosicao(usuario, 14));
        Tela.linha("   (esperado 369858)");

        // colisao do exemplo do enunciado, "Aa" e "BB" tem o mesmo valor na polinomial
        char[] aa = {'A', 'a'};
        char[] bb = {'B', 'B'};
        tabelaPolinomial.inserir(aa, 2, 0);
        tabelaPolinomial.inserir(bb, 2, 1);
        Tela.texto("Colisoes depois de inserir Aa e BB na polinomial: ");
        Tela.inteiro(tabelaPolinomial.colisoes);
        Tela.linha("   (esperado 1)");
        Tela.texto("Busca de BB devolve indice: ");
        Tela.inteiro(tabelaPolinomial.buscar(bb, 2));
        Tela.linha("   (esperado 1)");
    }
}
