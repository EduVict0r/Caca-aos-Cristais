# Guia de estudo — Caça aos Cristais

## Ordem e responsabilidades

Comece por Game: o main agenda a criação da janela na EDT.
Leia GameState, depois Player, Crystal, Enemy e por último Scenary.
Game configura a janela. Player guarda posição, vidas e proteção.
Crystal guarda posição e se já foi coletado. Enemy guarda posição, eixo e direção.
Scenary reúne os objetos, recebe comandos, aplica regras e desenha.
Os auxiliares Music e Util pertencem à base, mas não participam desta versão.

## Caminho de uma ação

1. main chama EventQueue.invokeLater.
2. Na EDT, Game cria Scenary, instala o painel, usa pack e exibe a janela.
3. O construtor de Scenary cria os objetos e associa teclas no InputMap/ActionMap.
4. Pressionar D registra seu código no conjunto keys; soltá-lo remove o código.
5. O Timer dispara aproximadamente a cada 20 ms. updateGame só avança em JOGANDO.
6. held calcula dx e dy. Player.update normaliza o vetor e limita a posição à arena.
7. Os inimigos avançam. Rectangle.intersects identifica contatos; takeDamage respeita a proteção.
8. O laço dos cristais ignora coletados e soma pontos nas novas coletas.
9. Zero vidas é verificado antes da vitória.
10. repaint solicita um desenho. Swing chama paintComponent, que desenha o estado atual.

## Conceitos com exemplos reais

**Classe e objeto:** Player é a definição; new Player(45,110) cria um jogador específico.
As duas chamadas new Enemy criam objetos separados com posições e eixos próprios.

**Construtor:** Player recebe as coordenadas iniciais. Os campos lives e immunity começam
com três e zero. Recriar Player em resetMatch também reinicia a proteção.

**Encapsulamento:** lives é privado. Scenary consulta getLives e pede takeDamage,
em vez de modificar vidas diretamente. getBounds devolve um retângulo novo.

**Enum:** GameState restringe os estados a cinco nomes válidos. A condição no início
de updateGame impede movimento, colisões e passagem da proteção durante pausa ou telas finais.

**Listas:** crystals e enemies são ArrayList. Um laço for visita cada objeto.
Cristais coletados permanecem na lista, marcados por um boolean. Isso mantém a quantidade total.
keys é um conjunto HashSet, não uma lista: cada código de tecla aparece no máximo uma vez.
Isso também evita alternar pausa várias vezes pelo autorrepetir do teclado.

**Eventos:** a tecla dispara uma Action; o Timer dispara outro evento.
A janela dispara windowLostFocus, que limpa keys e pausa a partida.

**Timer:** existe uma única instância, criada no construtor do painel.
resetMatch não cria outro Timer. Cada atualização ativa desconta 20 ms da proteção.
O intervalo é aproximado, não uma garantia de tempo real.

**EDT:** é a fila de trabalho da interface Swing. Teclas, Timer e desenho executam nela.
Assim, não precisamos coordenar threads próprias alterando o jogador simultaneamente.
Não coloque esperas, downloads ou trabalho pesado nesses métodos: travariam a interface.

**paintComponent:** super limpa o fundo conforme o contrato do Swing.
Uma cópia Graphics2D permite configurar cores e fontes, depois dispose libera essa cópia.
Desenhar não altera pontuação nem posições. repaint agenda o desenho, sem chamá-lo diretamente.

**Associações de teclas:** InputMap relaciona uma combinação à identificação da ação;
ActionMap relaciona essa identificação ao código executado.
WHEN_IN_FOCUSED_WINDOW permite controles enquanto a janela tem foco.
Há associações de pressionamento e soltura para cada tecla.
held usa OU: W e seta para cima juntos contam como uma direção, não velocidade dupla.

**Colisão:** Rectangle.intersects verifica sobreposição das áreas.
Não há colisão pixel a pixel. A borda do losango também pertence ao retângulo.
Math.hypot calcula o comprimento do vetor: (1,1) mede raiz de dois.
Dividir cada componente por esse comprimento mantém o módulo da velocidade em SPEED.

## Alterações na base e seus motivos

- Game preservou JFrame e EDT. pack substitui tamanho externo arbitrário para garantir área útil.
  A janela fixa evita regras de redimensionamento. O evento de foco evita comandos presos.
- Scenary preservou JPanel e Timer de 20 ms. paint virou paintComponent com super para
  respeitar o ciclo Swing. KeyListener foi substituído por mapas de teclas com soltura.
- O fundo que alternava de cor foi trocado por arena, grade, placar e sobreposições.
- Player deixou de carregar imagens por caminho relativo: formas funcionam em qualquer diretório.
  Posições double permitem diagonal normalizada; limites impedem invasão do placar.
- Estados explícitos tornam início, pausa e finais fáceis de explicar.
- Crystal e Enemy isolam os dados e desenhos das entidades, sem hierarquia adicional.
- Music procura voar.wav, ponto.wav e bateu.wav, ausentes em res. Foi preservada, sem instanciação.
  soundFX=false mantém compatibilidade. Util foi preservada, mas não há imagens para redimensionar.
- Não foram adicionados gerenciadores de dependências, recursos preview ou threads do jogo.

## Perguntas para ensaio

1. Qual é a primeira classe executada?
2. Por que criar a janela na EDT?
3. Qual é a diferença entre classe e objeto?
4. Por que vidas é privado?
5. Para que serve GameState?
6. Como uma tecla vira movimento?
7. Por que registrar a soltura?
8. Por que a diagonal não é mais rápida?
9. Como os limites protegem o placar?
10. Como impedir pontos duplicados?
11. Como o inimigo inverte a direção?
12. Como funciona a proteção contra dano?
13. Por que a pausa congela a proteção?
14. Quem vence se o último cristal coincidir com dano fatal?
15. Por que reiniciar não acelera o jogo?

## Respostas comentadas

1. Game, no main. Ele agenda a criação da interface.
2. Swing processa eventos e desenho nessa thread; manter alterações nela evita concorrência.
3. Enemy é uma classe; cada new Enemy é um objeto com estado independente.
4. Para controlar alterações através de takeDamage, respeitando as regras.
5. Nomear estados possíveis e permitir regras apenas no momento correto.
6. A Action registra o código; o Timer consulta keys e chama Player.update.
7. Sem remover a tecla, o próximo tick ainda entenderia que ela está pressionada.
8. O vetor é dividido pelo comprimento antes de multiplicar pela velocidade.
9. A coordenada mínima y é 100; o máximo considera o tamanho do personagem.
10. isCollected é verificado antes de collect e antes de somar pontos.
11. Ao atingir um extremo, corrige a posição e multiplica direction por -1.
12. O contador começa em 1000 após dano e bloqueia novos danos enquanto positivo.
13. updateGame retorna antes de Player.update quando não está em JOGANDO.
14. A derrota: a condição de zero vidas vem antes do else if da vitória.
15. resetMatch recria entidades e limpa dados, mas conserva a única instância de Timer.

## Cinco exercícios pequenos

1. Alterar Player.SPEED de 4 para 5 e comparar diagonal e reta.
2. Alterar Scenary.POINTS_PER_CRYSTAL para 20; conferir o total final de 200.
3. Alterar Player.INITIAL_LIVES para 5 e conferir placar e derrota.
4. Alterar Enemy.SPEED para 2 e observar a inversão nas quatro bordas relevantes.
5. Trocar o eixo do segundo inimigo em resetMatch; explicar como o boolean muda a patrulha.
Após cada exercício, testar e restaurar os valores para a entrega.

## Plano de quatorze dias

| Dia | Atividade e resultado esperado |
| --- | --- |
| 1 | Abrir no IntelliJ, executar Game e experimentar controles. |
| 2 | Ler Game e explicar main, JFrame, pack e EDT em voz alta. |
| 3 | Ler Player e desenhar no papel as coordenadas da arena. |
| 4 | Calcular movimento reto e diagonal, executar exercício 1. |
| 5 | Ler Crystal, explicar encapsulamento e coleta única. |
| 6 | Ler Enemy, acompanhar uma inversão, executar exercício 4. |
| 7 | Ler GameState e desenhar transições entre cinco estados. |
| 8 | Ler bindKey e held; testar teclas simultâneas e solturas. |
| 9 | Ler updateGame e simular colisões e prioridade de derrota no papel. |
| 10 | Estudar Timer, pausa e proteção; testar perda de foco. |
| 11 | Estudar paintComponent; identificar cada elemento na tela e no código. |
| 12 | Executar roteiro manual do README e corrigir qualquer falha observada. |
| 13 | Responder às quinze perguntas sem consultar respostas; ensaiar demonstração. |
| 14 | Compilar de novo, testar reinícios, ensaiar apresentação completa e guardar cópia. |

Na apresentação, iniciar uma partida, coletar, mostrar dano, pausar, trocar de janela
e explicar o reinício. Dividir as classes entre os estudantes, mas todos devem conseguir
narrar o caminho completo de uma tecla até o desenho.
