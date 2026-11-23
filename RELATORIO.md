# Relatório – Trabalho RA3: Tabelas Hash

Integrantes: Gabriel Naumann Jerônimo de Moura e Gabriel Pacheco Benin

- Repositório: https://github.com/njmoura/trabalho-ra3-hash
- Vídeo: https://youtu.be/jEyI05FzMJs

## 1. Modelagem

- **Leitura:** o `LeitorCsv` lê o arquivo caractere por caractere (`BufferedReader.read()`) e
  monta cada registro à mão. Ele controla se está dentro de aspas, então vírgulas, `""` e quebras de
  linha dentro do review/text não quebram o registro. Só os campos Title, User_id, profileName e
  review/score são guardados.
- **Chaves:** cada chave é um `char[]` junto com o seu tamanho, guardado em uma variável separada.
- **Tabela hash (`TabelaHash`):** vetor de 1000003 posições; cada posição é o início de uma lista
  encadeada de `No`. Na inserção, a lista é percorrida comparando caractere a caractere e, se a chave
  não existe, ela entra no **início** da lista. Não tem rehash. A posição é `(h & 0x7FFFFFFF) % m`.
- **Cadastros:** a tabela guarda só a chave e um índice. Os dados ficam em vetores paralelos:
  - `CadastroLivros`: título, soma das notas e N;
  - `CadastroUsuarios`: User_id, profileName, melhor livro e nota do usuário.
- **Avaliações:** cada linha válida é guardada em `ListaAvaliacoes` como (índice do usuário,
  índice do livro, nota). Depois da leitura, uma única passada nessa lista calcula o melhor livro
  de cada usuário.
- **Ordenação:** merge sort em um vetor de índices de usuários.

## 2. Decisões

| Decisão | Escolha |
|---|---|
| Função hash das tabelas principais | FNV1a |
| Linha descartada | sem User_id, sem Title ou nota inválida (vazia, não numérica ou fora de 1.0–5.0); cada linha conta uma vez, pelo primeiro motivo nessa ordem |
| profileName | primeiro nome não vazio do User_id (linhas descartadas não contam) |
| Desempate do melhor livro | maior média → maior N → título em ordem alfabética |
| nota_usuario repetida | maior nota |
| Comparação de médias | `somaA * nB` contra `somaB * nA` (sem divisão) |
| Ordenação | merge sort (ver abaixo) |
| Arredondamento | média com 4 casas e nota com 1 casa, arredondando o meio para cima |

**Por que merge sort:** ele é sempre O(n log n), mesmo no pior caso. O quick sort pode chegar a
O(n²) dependendo do pivô, e a base tem muitos empates (99.014 usuários têm um melhor livro com média
5.0), o que é justamente um caso ruim para um quick sort simples. Com 1.008.961 usuários, o merge sort
faz cerca de 20 níveis de recursão e levou cerca de 0,4 s. O custo é um vetor auxiliar do mesmo
tamanho (cerca de 4 MB), que é pouco perto do resto do programa.

## 3. Resultados da leitura

| Medida | Valor |
|---|---|
| Registros lidos | 3.000.000 |
| Registros válidos | 2.438.018 |
| Descartados sem User_id | 561.787 |
| Descartados sem Title | 195 |
| Descartados com nota inválida | 0 |
| Usuários distintos | 1.008.961 |
| Títulos distintos | 206.711 |

Quase 19% das linhas não têm User_id. Nenhuma nota é inválida: todas estão entre 1.0 e 5.0.

### Tempos de cada etapa (RF8)

| Etapa | Tempo (ms) |
|---|---|
| Leitura e indexação | 60.335,4 |
| Cálculo do melhor livro | 37,3 |
| Ordenação (merge sort) | 410,2 |
| Gravação dos dois arquivos | 3.925,9 |
| Ranking top 10 | 12,1 |
| Consultas (user_id.txt) | 1,2 |

A leitura ocupa mais de 90% do tempo total: são 2,7 GB lidos um caractere por vez. O cálculo do
melhor livro é muito rápido (37 ms) porque é só uma passada nas 2,4 milhões de avaliações já
guardadas, usando índices, sem nenhuma busca na tabela hash.

### Top 10 livros com N ≥ 100 (RF7)

| # | Título | Média | N |
|---|---|---|---|
| 1 | Lilla Belle: The First Stages | 5.0000 | 106 |
| 2 | Why revival tarries | 4.9669 | 181 |
| 3 | The Ferret Calendar 2005, Ferret Music | 4.9430 | 158 |
| 4 | The Valley of Vision: a Collection of Puritan Prayers and Devotions | 4.9257 | 175 |
| 5 | Little Britches | 4.9118 | 136 |
| 6 | With the Old Breed: At Peleliu and Okinawa | 4.9113 | 654 |
| 7 | Azucar! The Story of Sugar | 4.9083 | 120 |
| 8 | Harrington on Hold 'em Expert Strategy for No Limit Tournaments, Vol. 2: Endgame | 4.9065 | 107 |
| 9 | Cooky book | 4.9058 | 223 |
| 10 | Betty Crocker's cooky book | 4.9054 | 222 |

As posições 9 e 10 são praticamente o mesmo livro com títulos diferentes (N e média quase iguais).
Isso é discutido na questão 4.

## 4. Parte experimental

Cada configuração usa uma tabela nova com m = 1000003. Os tempos são a média de 3 execuções.

### Usuários (1.008.961 chaves)

| Função | Colisões | Sondagens sucesso | Sondagens falha | Pior caso | Inserção (ms) | Busca (ms) |
|---|---|---|---|---|---|---|
| FNV1a | 373.603 | 1,5038 | 1,0076 | 8 | 130,7 | 186,4 |
| Polinomial 31 | 373.726 | 1,5049 | 1,0082 | 9 | 162,1 | 173,8 |
| djb2 | 373.726 | 1,5042 | 1,0106 | 8 | 138,6 | 150,4 |

### Títulos (206.711 chaves)

| Função | Colisões | Sondagens sucesso | Sondagens falha | Pior caso | Inserção (ms) | Busca (ms) |
|---|---|---|---|---|---|---|
| FNV1a | 20.130 | 1,1042 | 0,2064 | 5 | 33,0 | 28,4 |
| Polinomial 31 | 20.001 | 1,1035 | 0,2070 | 5 | 15,3 | 21,8 |
| djb2 | 19.895 | 1,1032 | 0,2082 | 5 | 15,4 | 21,6 |

### 4.1 Comparação com o hash ideal

Para saber se os números são bons ou ruins, comparamos com o que a teoria prevê para um hash
**ideal**, em que cada chave cai em uma posição totalmente aleatória. Com n chaves e m posições,
o fator de carga é α = n / m e:

- a quantidade esperada de posições ocupadas é m·(1 − e^(−α)), então as colisões esperadas são
  n − m·(1 − e^(−α));
- a busca com sucesso examina em média 1 + α/2 nós;
- a busca sem sucesso percorre a lista inteira, que tem em média α nós.

| Conjunto | α | Colisões esperadas | Medido (min – max) | Sucesso esperado | Medido | Falha esperada | Medido |
|---|---|---|---|---|---|---|---|
| Usuários | 1,0090 | 373.558 | 373.603 – 373.726 | 1,5045 | 1,5038 – 1,5049 | 1,0090 | 1,0076 – 1,0106 |
| Títulos | 0,2067 | 19.966 | 19.895 – 20.130 | 1,1034 | 1,1032 – 1,1042 | 0,2067 | 0,2064 – 0,2082 |

As três funções ficaram a menos de 1% do valor ideal em todas as medidas. Ou seja, **para estes
dados as três funções se comportam como um hash ideal**, e a diferença entre elas é do tamanho da
variação aleatória esperada, não uma diferença de qualidade.

**Pior caso.** Em um hash ideal, o tamanho de cada lista segue aproximadamente uma distribuição de
Poisson com média α. Para os usuários (α ≈ 1), a chance de uma lista ter 8 chaves é cerca de 9·10⁻⁶ e
de ter 9 é cerca de 1·10⁻⁶. Multiplicando pelo milhão de posições, o esperado é ter umas 9 listas com
8 chaves e cerca de 1 lista com 9. Por isso o pior caso medido (8 ou 9) é exatamente o esperado, e
a Polinomial 31 ter dado 9 não indica que ela é pior. Para os títulos (α ≈ 0,21), o esperado é ter
2 ou 3 listas com 5 chaves e quase nenhuma com 6, o que bate com o pior caso 5 das três funções.

### 4.2 Diferenças entre as funções

- **FNV1a:** o XOR seguido da multiplicação pelo primo espalha a mudança de cada caractere por todos os
  32 bits. Por isso ela é a mais robusta para chaves parecidas (como "Volume 1" e "Volume 2") e foi a
  escolhida para as tabelas principais. Nos usuários ela teve o menor número de colisões, mas a
  diferença (123 colisões em 373 mil) é pequena demais para ser significativa.
- **Polinomial 31:** é simples e boa quando a chave é longa e o m é primo. O ponto fraco é que a base
  31 é pequena: chaves diferentes podem dar exatamente o mesmo h, como "Aa" e "BB" (as duas valem
  2112). Também, chaves bem curtas não chegam a estourar o int: um título de 3 letras dá no máximo
  uns 120 mil, então os títulos curtos ficam concentrados no começo da tabela. Na base isso pesa
  pouco porque os User_ids têm 13–14 caracteres e a maioria dos títulos é longa.
- **djb2:** parecida com a polinomial, mas com base 33 e valor inicial 5381. O valor inicial alto faz
  até chaves curtas estourarem o int e se espalharem pela tabela. Nos títulos ela teve o menor número
  de colisões, de novo com diferença pequena.
- **O m primo ajuda as funções mais simples.** Como 1000003 é primo, o resto da divisão depende de
  todos os bits de h. Se m fosse uma potência de 2, a posição dependeria só dos bits mais baixos,
  e aí a Polinomial e a djb2 (que misturam pouco os bits altos com os baixos) teriam muito mais colisões
  que a FNV1a.
- A Polinomial 31 e a djb2 deram exatamente o mesmo número de colisões nos usuários (373.726). Isso é
  coincidência na contagem: as distribuições são diferentes, tanto que o pior caso de uma é 9 e o da
  outra é 8.

### 4.3 Tempos

- Os tempos ficam entre 0,07 e 0,19 µs por operação. Nessa escala, o custo de calcular o hash (uma
  multiplicação por caractere) é pequeno. O que domina é o acesso à memória: o vetor da tabela tem
  1 milhão de referências e os nós ficam espalhados na memória, então quase toda operação causa uma
  falta de cache.
- Os tempos variam entre execuções por causa do coletor de lixo e da compilação JIT do Java. Um sinal
  disso: nos títulos a FNV1a aparece mais lenta (33,0 ms contra 15,3 ms), mas ela é sempre a primeira
  função a rodar em cada conjunto, então pega parte do aquecimento da JVM. Uma medição mais justa
  descartaria uma execução de aquecimento ou alternaria a ordem das funções.
- Conclusão: os tempos **não** mostram uma função claramente mais rápida. A escolha entre elas, para
  estes dados, deve ser feita pela robustez (FNV1a), não pelo tempo.

### 4.4 Limitações da solução

- **Capacidade fixa:** com m = 1000003 e 1 milhão de usuários, α ≈ 1 ainda é ótimo. Mas sem rehash,
  se a base tivesse 10 vezes mais usuários (α ≈ 10), a busca com sucesso passaria a examinar cerca de
  6 nós e a sem sucesso cerca de 10. A tabela continuaria funcionando, só que mais lenta.
- **Memória:** todas as 2.438.018 avaliações válidas ficam na memória (usuário, livro e nota), mais as
  chaves de 1 milhão de usuários e 206 mil títulos, cada uma como objeto do Java. O programa precisa de
  algumas centenas de MB, por isso o README pede `-Xmx4g`. A alternativa seria ler o arquivo duas
  vezes (ver questão 1).
- **Tempo de leitura:** ler caractere por caractere deixa o código simples, mas leva cerca de 1
  minuto. Lendo o arquivo em blocos grandes, a mesma leitura levava cerca de 10 segundos.
- **Title como chave:** o mesmo livro com títulos diferentes ("Cooky book" e "Betty Crocker's cooky
  book") vira dois livros, e edições diferentes com o mesmo título viram um livro só, com avaliações
  repetidas (questão 4).
- **Média simples:** livros com poucas avaliações dominam o "melhor livro" (questão 3).
- **Ordem alfabética:** é pelo código dos caracteres, então letras maiúsculas vêm antes das minúsculas
  e letras com acento vêm depois de todas. Não é a ordem de um dicionário.
- **Desempate pelo título:** é determinístico, mas arbitrário: entre dois livros com mesma média e
  mesmo N, ganha o que vem antes no alfabeto, sem nenhum motivo de qualidade.

## 5. Questões

**1. Por que a média só é definitiva depois da leitura completa? O que precisa ser guardado?**

As avaliações de um mesmo livro estão espalhadas pelo arquivo inteiro, sem ordem. A última avaliação
de um livro pode estar na última linha, então, antes do fim do arquivo, a soma e o N ainda podem
mudar. Se o programa decidisse o melhor livro de um usuário no meio da leitura, essa decisão poderia
ficar errada depois. Por exemplo: o livro A tem média 5.0 com 1 nota e o livro B tem 4.8. O usuário
escolhe A, mas mais à frente A recebe dez notas 1.0 e cai para 1.4.

Por isso o programa guarda duas coisas durante a leitura:
- a soma e o N de cada título (tabela de livros);
- todas as avaliações válidas, como (índice do usuário, índice do livro, nota), em `ListaAvaliacoes`.

São 2.438.018 avaliações; com dois `int` e um `double` cada, isso dá cerca de 39 MB. Depois da
leitura, uma única passada nessa lista escolhe o melhor livro de cada usuário em cerca de 37 ms.
A outra opção seria ler o arquivo de 2,7 GB duas vezes: na primeira passada calcular as médias e na
segunda escolher os melhores livros. Gastaria menos memória, mas dobraria o tempo de leitura, que já
é a etapa mais lenta. Por isso preferimos guardar as avaliações.

**2. Comparar somaA/NA > somaB/NB com double pode dar errado?**

Pode. O double guarda os números em binário com 53 bits de precisão, e muitas frações (como 1/3 ou
1/10) não têm representação exata, então o resultado da divisão é arredondado. Duas médias iguais na
matemática podem virar doubles um pouco diferentes, e duas médias diferentes por muito pouco podem
virar o mesmo double. Isso muda o melhor livro e a ordem da lista ordenada, onde o empate é decidido
por N e por User_id.

Isso aconteceu de verdade no trabalho: 3044/640 é exatamente 4,75625, mas o double guarda
4,7562499999…, e o arredondamento para 4 casas dava 4,7562 em vez de 4,7563. Achamos isso conferindo
a saída, em 707 usuários, e corrigimos em `Utilitarios.decimalParaTexto`.

A solução para comparar é não dividir: como N é sempre positivo,
somaA/NA > somaB/NB é o mesmo que **somaA × NB > somaB × NA**. As notas são inteiras, então soma e
N são inteiros. O maior N da base é 18.237 (The Hobbit), então o maior produto possível é cerca de
5 × 18.237 × 18.237 ≈ 1,7 × 10⁹, muito abaixo de 2⁵³ ≈ 9 × 10¹⁵, o limite até onde o double guarda
inteiros sem erro. A comparação é exata. É assim que o programa faz em
`CadastroLivros.compararMedias`, usado tanto no melhor livro quanto na ordenação.

**3. Viés: um livro com uma única nota 5.0 fica à frente de um com média 4,9 em 20 mil avaliações.**

A média simples não leva em conta quantas avaliações existem. Com poucas notas, a média depende
muito do acaso (uma única pessoa decide tudo), enquanto 20 mil avaliações dão uma média muito mais
confiável. Na base o efeito é grande:

- 69.754 títulos (34% de todos) têm só 1 avaliação, e 41.911 deles têm nota 5.0;
- 99.014 usuários (9,8%) têm como melhor livro um livro de média 5.0;
- 144.293 usuários (14,3%) têm como melhor livro um livro com menos de 5 avaliações.

Ou seja, para muitos usuários o "melhor livro" é só o livro mais obscuro que eles avaliaram.

Correção proposta, a **média bayesiana**:
`média_ajustada = (soma + m × C) / (N + m)`,
onde C é a média geral da base (4,2283) e m é quantas avaliações "fictícias" com nota C somamos
(por exemplo m = 100). Com poucas avaliações a nota fica perto de C; com muitas, fica perto da média
real do livro. Com m = 100:

| Livro | Média simples | N | Média bayesiana |
|---|---|---|---|
| uma nota 5.0 | 5,0000 | 1 | 4,2359 |
| média 4,9 | 4,9000 | 20.000 | 4,8967 |
| Lilla Belle (1º do top 10) | 5,0000 | 106 | 4,6254 |

O livro de 20 mil avaliações passa à frente, como deveria. Isso continua podendo ser comparado sem
divisão, multiplicando cruzado os numeradores e denominadores. Outras opções seriam exigir um N
mínimo, como o top 10 faz (N ≥ 100), ou usar o limite inferior de um intervalo de confiança.

**4. O mesmo título em várias edições, com avaliações replicadas. Como isso afeta N e a média?**

Na base, a mesma avaliação aparece em edições diferentes (Ids diferentes) do mesmo livro. Como o
programa agrupa pelo Title, a mesma avaliação é somada uma vez por edição:

- o **N fica inflado**, contando a mesma pessoa várias vezes;
- a **média** só não muda se todas as avaliações forem repetidas o mesmo número de vezes. Como algumas
  edições têm cópias e outras não, as avaliações repetidas passam a pesar mais que as outras;
- o N inflado também faz o livro ganhar desempates e passar o filtro N ≥ 100 do top 10 sem ter de
  verdade 100 avaliações diferentes.

Contamos à parte: das 2.438.018 avaliações com User_id e Title, **302.790 (12,4%)** são cópias exatas
de outra (mesmo User_id, Title, review/time, nota e texto).

Existe também o problema contrário: o mesmo livro com títulos diferentes vira dois livros. No top 10,
"Cooky book" (média 4,9058, N = 223) e "Betty Crocker's cooky book" (média 4,9054, N = 222) são quase
certamente o mesmo livro, com as mesmas avaliações divididas entre dois títulos.

Cuidados para não contar a mesma avaliação mais de uma vez:
1. criar uma chave para cada avaliação, por exemplo User_id + Title + review/time + nota;
2. guardar essa chave em outra tabela hash durante a leitura;
3. se a chave já existir, ignorar a linha (não somar na média nem no N).

Isso custa mais uma tabela com cerca de 2,4 milhões de chaves. Para o segundo problema, seria preciso
normalizar os títulos (minúsculas, sem pontuação e sem o nome da editora). Isso é mais arriscado,
porque pode juntar livros que são diferentes de verdade.

## 6. Dificuldades

- Ler o CSV sem métodos prontos: o review/text tem vírgulas, aspas duplicadas e quebras de linha
  dentro do campo, então não dá para simplesmente separar por vírgula e por linha.
- Não poder usar o atributo de tamanho dos vetores, `String.equals` nem `parseDouble`: tivemos que
  guardar o tamanho de cada chave e escrever a comparação e a conversão de números à mão.
- O erro de precisão do double no arredondamento das médias (questão 2), que só apareceu conferindo
  a saída.
- Desempenho e memória: 3 milhões de linhas e 2,7 GB. A leitura caractere por caractere leva cerca
  de 1 minuto, e as avaliações precisam ficar todas na memória.
- Definir regras de desempate que fossem determinísticas e documentadas.
