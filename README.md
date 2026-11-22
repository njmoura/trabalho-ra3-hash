# Trabalho RA3 – Tabelas Hash (Amazon Books Reviews)

Disciplina: Resolução de Problemas Estruturados em Computação – Prof. Andrey Cabral Meira

Integrantes: Gabriel Naumann Jerônimo de Moura e Gabriel Pacheco Benin

## O que o programa faz

Lê o arquivo `Books_rating.csv` (3 milhões de avaliações) e, para cada usuário, descobre o
**livro de maior média global** entre os livros que ele avaliou. Tudo é feito com uma
**tabela hash própria com encadeamento separado** (capacidade fixa m = 1000003), sem usar
nenhuma estrutura ou método pronto do Java.

| Requisito | Onde está |
|---|---|
| RF1 – 3 funções hash (FNV1a, Polinomial 31, djb2) | `FuncoesHash.java` |
| RF2 – tabela hash com encadeamento separado | `TabelaHash.java`, `No.java` |
| RF3 – Title → (soma, N) e User_id → (profileName, melhor livro) | `CadastroLivros.java`, `CadastroUsuarios.java` |
| RF4 – descarte de linhas inválidas | `Principal.lerBase` |
| RF5 – `usuario_melhor_livro.csv` | `Principal.gravarArquivo` |
| RF6 – consultas do `user_id.txt` | `Principal.fazerConsultas` |
| RF7 – top 10 livros com N ≥ 100 | `Principal.mostrarTop10` |
| RF8 – tempo de cada etapa (`System.nanoTime`) | `Principal.main` |
| RF9 – lista ordenada com merge sort | `Ordenacao.java` |

Arquivos auxiliares: `LeitorCsv.java` (leitura do CSV tratando aspas), `EscritorArquivo.java`
(gravação), `Utilitarios.java` (comparação de chaves e conversão de números à mão),
`Vetores.java` (aumentar vetores), `Tela.java` (impressão), `TesteFuncoesHash.java`
(confere os valores de referência do enunciado).

## Como compilar

É preciso ter um JDK instalado (Java 8 ou mais novo). Na pasta do projeto:

```bash
javac -d bin src/*.java
```

## Como executar

1. Baixe a base no Kaggle ("Amazon Books Reviews") e coloque o arquivo `Books_rating.csv`
   **na pasta do projeto** (a mesma pasta deste README). Ele não está no repositório porque tem 2,7 GB.
2. Coloque no arquivo `user_id.txt` os User_ids que deseja consultar (um por linha).
3. Execute:

```bash
java -Xmx4g -cp bin Principal
```

Para conferir as funções hash com os valores do enunciado (chave "Ana"):

```bash
java -cp bin TesteFuncoesHash
```

## Arquivos gerados

- `usuario_melhor_livro.csv` – um usuário por linha, na ordem em que apareceram no arquivo
- `usuario_melhor_livro_ordenado.csv` – mesma coisa, em ordem decrescente de média
  (desempate: maior N, depois User_id em ordem alfabética)

## Regras adotadas

- **Linha descartada:** sem User_id, sem Title ou com nota inválida (vazia, com letras ou fora de 1.0 a 5.0).
  Linha com profileName vazio **não** é descartada. Cada linha descartada é contada uma vez só,
  pelo primeiro motivo nesta ordem: sem User_id, sem Title, nota inválida.
- **Linha descartada não é usada para nada**, nem para o profileName.
- **profileName:** vale o primeiro nome não vazio encontrado para aquele User_id.
- **Melhor livro (desempate):** maior média → maior N → título em ordem alfabética.
- **nota_usuario:** se o usuário avaliou o mesmo livro mais de uma vez, vale a maior nota.
- **Comparação de médias sem divisão:** `somaA * nB > somaB * nA`.
- A tabela principal usa a função **FNV1a**.
