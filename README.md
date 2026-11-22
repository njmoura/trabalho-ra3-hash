# Trabalho RA3 – Tabelas Hash (Amazon Books Reviews)

Disciplina: Resolução de Problemas Estruturados em Computação – Prof. Andrey Cabral Meira

Integrantes: Gabriel Naumann Jerônimo de Moura e Gabriel Pacheco Benin

Implementação das três funções hash (FNV1a, Polinomial 31 e djb2) e da
tabela hash com encadeamento separado (capacidade fixa m = 1000003).

O programa lê o arquivo `Books_rating.csv` e monta a tabela de livros
(Title → soma das notas e N), descartando e contando as linhas sem User_id,
sem Title ou com nota inválida.

## Como compilar

```bash
javac -d bin src/*.java
```

## Como executar

Coloque o arquivo `Books_rating.csv` (Kaggle, "Amazon Books Reviews") na pasta do projeto e rode:

```bash
java -Xmx4g -cp bin Principal
```

Para conferir as funções hash com os valores do enunciado (chave "Ana"):

```bash
java -cp bin TesteFuncoesHash
```
