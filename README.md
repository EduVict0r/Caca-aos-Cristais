# Caça aos Cristais

Jogo 2D de arena fixa em Java, Swing e AWT, sem dependências externas.

## Executar no IntelliJ

1. Instale/configure o JDK 26 (o projeto já declara SDK 26 em .idea/misc.xml).
2. Abra esta pasta em File > Open.
3. Em File > Project Structure, selecione o JDK 26 e confira que src é Sources Root.
4. Use UTF-8 como codificação do projeto.
5. Abra src/com/unibra/game/Game.java e execute o main pelo triângulo verde.
6. Não são necessários Maven, Gradle, argumentos ou arquivos baixados.

A janela tem conteúdo fixo de 800 × 640. O placar fica acima da arena.

## Regras e controles

| Comando | Ação |
| --- | --- |
| Enter | Iniciar a partida |
| Setas ou WASD | Movimentar o personagem |
| P | Pausar ou retomar |
| R | Reiniciar após vitória ou derrota |

- Colete os dez cristais. Cada cristal vale dez pontos e só pode ser coletado uma vez.
- Evite os dois inimigos que patrulham a arena.
- Você começa com três vidas.
- Após receber dano, fica protegido por aproximadamente um segundo, com indicação visual.
- Coletar todos os cristais com vidas restantes resulta em vitória.
- Perder todas as vidas resulta em derrota, mesmo ao coletar o último cristal na mesma atualização.
- Trocar de janela pausa a partida e limpa os comandos de movimento.
- Reiniciar restaura os dados da partida.

## Arquivos

- src/com/unibra/game/Game.java: main e janela na EDT.
- Scenary.java: painel, Timer, controles, desenho e regras.
- Player.java: movimento, limites, vidas e proteção.
- Crystal.java: cristal e coleta.
- Enemy.java: patrulha.
- GameState.java: estados.
- src/com/unibra/utils/Music.java e Util.java: auxiliares originais preservados.
- res/andando.png e parado.png: imagens originais preservadas, não utilizadas.
- tests/GameChecks.java: verificações sem bibliotecas externas.
- docs/GUIA-DE-ESTUDO.md: explicações, perguntas, exercícios e plano de 14 dias.

## Base aproveitada

Foram preservados os pacotes, os nomes Game, Player e Scenary, a criação na EDT e o Timer Swing de 20 ms.
Game agora usa pack e janela fixa; Scenary usa paintComponent com super, associações de teclas e estados.
Player substitui sprites carregados por caminho relativo por formas, adicionando limites e normalização.
Music foi inspecionada: os WAV referidos não existem. Não é instanciada e soundFX fica false por compatibilidade.
Util não é necessária para formas. Ambas foram mantidas sem mudanças.

## Verificação

Compilação de todas as fontes com JDK 26 concluída. GameChecks executou com sucesso:
diagonal, limites superior/inferior, dano repetido, proteção aos 980/1000 ms, estado de coleta,
limites e inversão das duas patrulhas, abertura da janela e renderização do painel.
Também acionou Enter via ActionMap e a rotina de perda de foco, sem verificar automaticamente seus estados internos.
Após as verificações automatizadas, Edu validou manualmente vitória,
derrota, pontuação e reinício, confirmando o comportamento esperado.
Os cenários adicionais do roteiro abaixo permanecem para conferência.

No PowerShell, a partir desta pasta:
```powershell
$sources = @(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
javac -encoding UTF-8 -d out/verificacao $sources tests/GameChecks.java
java --class-path=out/verificacao GameChecks
java --class-path=out/verificacao com.unibra.game.Game
```

## Roteiro de testes manuais

- Conferir tela inicial e Enter, placar inicial 0 / 3 / 0.
- Mover com ambos os grupos de teclas; combinar e soltar W e seta para cima separadamente.
- Comparar diagonal com reta; tentar atravessar cada borda e entrar no placar.
- Coletar cada cristal, voltar ao local e confirmar que não pontua novamente.
- Observar as duas patrulhas até inverterem direção.
- Encostar em inimigo: uma vida perdida, piscada e proteção; manter contato para confirmar novo dano após a proteção.
- Pausar durante movimento/proteção; esperar dois segundos: nada deve avançar. Retomar.
- Segurar movimento, alternar para outra janela, soltar a tecla e voltar: jogo pausado, sem movimento preso.
- Coletar todos com vidas: vitória e 100 pontos. Perder três vidas: derrota.
- Para o empate coleta/dano fatal, usar temporariamente a última posição de cristal na rota do inimigo e chegar com uma vida; restaurar a configuração depois.
- Reiniciar após cada final por pelo menos cinco partidas: posições, direção, vidas, pontos, cristais e proteção restaurados; velocidade não aumenta.

## Limitações

Sem áudio, obstáculos ou fases adicionais. Colisões usam retângulos, inclusive nos losangos.
Movimento e proteção usam tempo simulado de 20 ms por atualização: sob carga extrema o jogo pode ficar mais lento.
O Timer continua solicitando desenho nas telas paradas, mas nenhuma regra ou proteção avança.
