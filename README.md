Jogo de Damas
Um jogo de damas desenvolvido em Java com interface gráfica.
🎮 O que faz

Jogo de damas completo para 2 jogadores
Interface gráfica com tabuleiro visual
Peças representadas por emojis: 🔴 (brancas), ⚫ (pretas), 🫅 (dama branca), 👑 (dama preta)
Validação automática de movimentos
Promoção de peças simples para damas
Sistema de log que salva o histórico da partida
Indicação visual de movimentos válidos

Como preparar para jogar
1. Preparar configuração
Crie um arquivo chamado config_inicial.txt dentro da pasta damas/ com o seguinte formato:
Nome do Jogador 1
Nome do Jogador 2
BRANCA
A última linha define a cor do primeiro jogador (BRANCA ou PRETA).

2. Executar o jogo
Primeiro, execute a configuração:
bashjava damas.core.P1
Depois, inicie o jogo:
bashjava damas.ui.P2

3. Como jogar

Clique em uma peça sua para selecioná-la (aparece borda dourada)
Clique no destino para mover (locais válidos têm borda verde)
O jogo alterna automaticamente entre os jogadores
Use o botão "Salvar Log" para gerar um arquivo com o histórico da partida

Regras básicas

Peças simples: movem na diagonal, uma casa por vez
Direção: peças brancas sobem, pretas descem
Captura: pule sobre a peça do adversário na diagonal
Damas: podem mover livremente na diagonal e capturar múltiplas peças
Objetivo: capture todas as peças do adversário ou bloqueie seus movimentos

Arquivos gerados

jogo_config.dat - Configuração do jogo (gerado pelo P1)
damas_log.txt - Histórico da partida (gerado ao clicar "Salvar Log")
