package damas.core;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import damas.exceptions.MovimentoInvalidoException;
import damas.exceptions.PosicaoInvalidaException;

public class Jogo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Tabuleiro tabuleiro;
    private Jogador jogador1;
    private Jogador jogador2;
    private Jogador jogadorAtual;
    private List<String> historicoMovimentos;
    private boolean jogoAtivo;
    private Jogador vencedor;

    public Jogo(ConfiguracaoJogo config) {
        this.tabuleiro = new Tabuleiro();
        this.jogador1 = new Jogador(config.getNomeJogador1(), config.getCorJogador1());
        this.jogador2 = new Jogador(config.getNomeJogador2(), config.getCorJogador2());
        this.jogadorAtual = config.getCorJogador1() == CorPeca.BRANCA ? jogador1 : jogador2;
        this.historicoMovimentos = new ArrayList<>();
        this.jogoAtivo = true;
        this.vencedor = null;
    }

    public boolean executarMovimento(Posicao origem, Posicao destino) {
        try {
            Peca peca = tabuleiro.getPeca(origem);
            if (peca == null) {
                throw new MovimentoInvalidoException("Não há peça na posição de origem");
            }

            if (peca.getCor() != jogadorAtual.getCor()) {
                throw new MovimentoInvalidoException("Esta peça não pertence ao jogador atual");
            }

            int totalAntes = tabuleiro.contarPecas(CorPeca.BRANCA) + tabuleiro.contarPecas(CorPeca.PRETA);

            tabuleiro.moverPeca(origem, destino);

            // Verifica promoção a dama
            Peca pecaMovida = tabuleiro.getPeca(destino);
            if (pecaMovida != null) {
                tabuleiro.verificarPromocaoDama(pecaMovida);
            }

            // Verifica capturas ocorridas
            int totalDepois = tabuleiro.contarPecas(CorPeca.BRANCA) + tabuleiro.contarPecas(CorPeca.PRETA);
            int capturas = totalAntes - totalDepois;
            if (capturas > 0) {
                jogadorAtual.incrementarPecasCapturadas(capturas);
            }

            // Registra movimento
            String movimento = String.format("[%s] %s: %s -> %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                jogadorAtual.getNome(), origem, destino);
            historicoMovimentos.add(movimento);

            verificarFimDeJogo();
            if (jogoAtivo) {
                alternarJogador();
            }
            return true;

        } catch (PosicaoInvalidaException | MovimentoInvalidoException e) {
            System.err.println("Erro no movimento: " + e.getMessage());
            return false;
        }
    }

    private void alternarJogador() {
        jogadorAtual = (jogadorAtual == jogador1) ? jogador2 : jogador1;
    }

    private void verificarFimDeJogo() {
        int brancas = tabuleiro.contarPecas(CorPeca.BRANCA);
        int pretas = tabuleiro.contarPecas(CorPeca.PRETA);
        if (brancas == 0 || pretas == 0) {
            jogoAtivo = false;
            vencedor = brancas == 0 ? jogador2 : jogador1;
        }
    }

    public void salvarLogPartida(String nomeArquivo) throws IOException {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.write("=== LOG DA PARTIDA ===\n");
            writer.write("Jogadores: " + jogador1.getNome() + " vs " + jogador2.getNome() + "\n\n");
            
            for (String movimento : historicoMovimentos) {
                writer.write(movimento + "\n");
            }
            
            writer.write("\n=== RESULTADO ===\n");
            writer.write("Status: " + (jogoAtivo ? "Em andamento" : "Finalizado") + "\n");
            if (vencedor != null) {
                writer.write("Vencedor: " + vencedor.getNome() + "\n");
            } else {
                writer.write("Próximo jogador: " + jogadorAtual.getNome() + "\n");
            }
            writer.write(String.format("%s capturou %d peças\n", jogador1.getNome(), jogador1.getPecasCapturadas()));
            writer.write(String.format("%s capturou %d peças\n", jogador2.getNome(), jogador2.getPecasCapturadas()));
            writer.write("Total de movimentos: " + historicoMovimentos.size() + "\n");
        }
    }

    // Getters
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }
    
    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }
    
    public Jogador getJogador1() {
        return jogador1;
    }
    
    public Jogador getJogador2() {
        return jogador2;
    }
    
    public boolean isJogoAtivo() {
        return jogoAtivo;
    }
    
    public List<String> getHistoricoMovimentos() {
        return new ArrayList<>(historicoMovimentos);
    }

    public Jogador getVencedor() {
        return vencedor;
    }
    
    public void finalizarJogo() {
        this.jogoAtivo = false;
    }
}