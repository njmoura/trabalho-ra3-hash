// Trabalho RA3 de Tabelas Hash
// Gabriel Naumann Jeronimo de Moura e Gabriel Pacheco Benin

// Funcoes auxiliares feitas a mao, porque metodos prontos sao proibidos
// Aqui ficam a comparacao de chaves, copia de vetores, conversao de texto para numero e de numero para texto
public class Utilitarios {

    // Devolve 1 se as duas chaves sao iguais e 0 se sao diferentes
    // A comparacao e feita caractere a caractere (String.equals e proibido)
    static int chavesIguais(char[] a, int tamanhoA, char[] b, int tamanhoB) {
        if (tamanhoA != tamanhoB) {
            return 0;
        }
        for (int i = 0; i < tamanhoA; i++) {
            if (a[i] != b[i]) {
                return 0;
            }
        }
        return 1;
    }

    // Ordem alfabetica (pelo codigo dos caracteres)
    // Devolve um numero negativo se "a" vem antes de "b", positivo se vem depois e 0 se sao iguais
    static int compararChaves(char[] a, int tamanhoA, char[] b, int tamanhoB) {
        int menor = tamanhoA;
        if (tamanhoB < menor) {
            menor = tamanhoB;
        }
        for (int i = 0; i < menor; i++) {
            if (a[i] != b[i]) {
                return a[i] - b[i];
            }
        }
        // se uma chave e o comeco da outra, a menor vem antes
        return tamanhoA - tamanhoB;
    }

    // Cria um vetor novo, do tamanho exato, com os caracteres da chave
    static char[] copiarChave(char[] origem, int tamanho) {
        char[] copia = new char[tamanho];
        for (int i = 0; i < tamanho; i++) {
            copia[i] = origem[i];
        }
        return copia;
    }

    // Converte o texto da nota (exemplo "4.0") para double, sem Double.parseDouble
    // Devolve -1 se a nota for invalida (vazia, com letras ou fora do intervalo 1.0 a 5.0)
    static double converterNota(char[] texto, int tamanho) {
        if (tamanho == 0) {
            return -1;
        }

        double valor = 0;
        int i = 0;
        int quantidadeDigitos = 0;

        // parte inteira
        while (i < tamanho && texto[i] >= '0' && texto[i] <= '9') {
            valor = valor * 10 + (texto[i] - '0');
            quantidadeDigitos = quantidadeDigitos + 1;
            i = i + 1;
        }

        // parte decimal (opcional)
        if (i < tamanho && texto[i] == '.') {
            i = i + 1;
            double divisor = 10;
            while (i < tamanho && texto[i] >= '0' && texto[i] <= '9') {
                valor = valor + (texto[i] - '0') / divisor;
                divisor = divisor * 10;
                quantidadeDigitos = quantidadeDigitos + 1;
                i = i + 1;
            }
        }

        // sobrou algum caractere que nao e numero, ou nao tinha numero nenhum
        if (i != tamanho || quantidadeDigitos == 0) {
            return -1;
        }

        if (valor < 1.0 || valor > 5.0) {
            return -1;
        }
        return valor;
    }

    // Escreve o numero inteiro em "destino" e devolve quantos caracteres foram escritos
    // O programa so escreve numeros positivos ou zero (contagens, N, tempos)
    static int inteiroParaTexto(int valor, char[] destino) {
        int tamanho = 0;

        if (valor == 0) {
            destino[0] = '0';
            return 1;
        }

        // pega os digitos de tras para frente
        char[] invertido = new char[12];
        int quantidade = 0;
        while (valor > 0) {
            invertido[quantidade] = (char) ('0' + (valor % 10));
            quantidade = quantidade + 1;
            valor = valor / 10;
        }

        // coloca na ordem certa
        for (int i = quantidade - 1; i >= 0; i--) {
            destino[tamanho] = invertido[i];
            tamanho = tamanho + 1;
        }
        return tamanho;
    }

    // Escreve o numero decimal com "casas" casas depois do ponto (arredondado)
    // Devolve quantos caracteres foram escritos em "destino"
    static int decimalParaTexto(double valor, int casas, char[] destino) {
        int tamanho = 0;

        int fator = 1;
        for (int i = 0; i < casas; i++) {
            fator = fator * 10;
        }

        int parteInteira = (int) valor;
        double resto = valor - parteInteira;
        // Arredonda somando 0.5 e o valor 0.000001 a mais corrige o erro de precisao do double
        // Por exemplo, 3044 / 640 = 4.75625, mas o double guarda 4.7562499999
        // e sem essa correcao o resultado sairia 4.7562 em vez de 4.7563
        int parteDecimal = (int) (resto * fator + 0.5 + 0.000001);

        // o arredondamento pode virar mais uma unidade (exemplo 2.99996 com 4 casas vira 3.0000)
        if (parteDecimal >= fator) {
            parteDecimal = parteDecimal - fator;
            parteInteira = parteInteira + 1;
        }

        // parte inteira
        char[] auxiliar = new char[12];
        int quantidade = inteiroParaTexto(parteInteira, auxiliar);
        for (int i = 0; i < quantidade; i++) {
            destino[tamanho] = auxiliar[i];
            tamanho = tamanho + 1;
        }

        // parte decimal, completando com zeros a esquerda (exemplo 5 com 4 casas vira 0005)
        if (casas > 0) {
            destino[tamanho] = '.';
            tamanho = tamanho + 1;
            for (int i = casas - 1; i >= 0; i--) {
                destino[tamanho + i] = (char) ('0' + (parteDecimal % 10));
                parteDecimal = parteDecimal / 10;
            }
            tamanho = tamanho + casas;
        }
        return tamanho;
    }
}
