package damas.core;

import java.io.Serializable;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import damas.exceptions.MovimentoInvalidoException;
import damas.exceptions.PosicaoInvalidaException;

/**
 * Classe principal que gerencia o jogo de damas
 */
public class Jogo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Tabuleiro tabuleiro;
    private Jogador jogador1;
    private Jogador jogador2;
    private Jogador jogadorAtual;
    private List<String> historicoMovimentos;
    private boolean jogoAtivo;
    
    public Jogo(ConfiguracaoJogo config) {
        this.tabuleiro = new Tabuleiro();
        this.jogador1 = new Jogador(config.getNomeJogador1(), config.getCorJogador1());
        this.jogador2 = new Jogador(config.getNomeJogador2(), config.getCorJogador2());
        this.jogadorAtual = config.getCorJogador1() == CorPeca.BRANCA ? jogador1 : jogador2;
        this.historicoMovimentos = new ArrayList<>();
        this.jogoAtivo = true;
    }
    
    /**
     * Executa um movimento no jogo
     */
    public boolean executarMovimento(Posicao origem, Posicao destino) {
        try {
            // Verifica se há peça na origem
            Peca peca = tabuleiro.getPeca(origem);
            if (peca == null) {
                throw new MovimentoInvalidoException("Não há peça na posição de origem");
            }
            
            // Verifica se a peça pertence ao jogador atual
            if (peca.getCor() != jogadorAtual.getCor()) {
                throw new MovimentoInvalidoException("Esta peça não pertence ao jogador atual");
            }
            
            // Verifica se é uma captura (movimento de 2 casas)
            int diferencaLinha = Math.abs(destino.getLinha() - origem.getLinha());
            int diferencaColuna = Math.abs(destino.getColuna() - origem.getColuna());
            boolean ehCaptura = (diferencaLinha == 2 && diferencaColuna == 2);
            
            // Executa o movimento
            tabuleiro.moverPeca(origem, destino);
            
            // Se foi captura, incrementa contador do jogador
            if (ehCaptura) {
                jogadorAtual.incrementarPecasCapturadas();
            }
            
            // Registra o movimento
            String tipoMovimento = ehCaptura ? "CAPTURA" : "movimento";
            String movimento = String.format("[%s] %s: %s %s -> %s", 
                                           LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                           jogadorAtual.getNome(), tipoMovimento, origem, destino);
            historicoMovimentos.add(movimento);
            
            // REGRA CORRETA: Após captura, só continua se tiver MAIS capturas disponíveis
            if (ehCaptura) {
                // Verifica se a peça que capturou pode capturar novamente
                boolean podeCapturarNovamente = false;
                try {
                    podeCapturarNovamente = tabuleiro.podeCapturar(destino);
                } catch (PosicaoInvalidaException e) {
                    // Se der erro, assume que não pode capturar
                    podeCapturarNovamente = false;
                }
                
                // Se não pode capturar novamente, alterna jogador
                if (!podeCapturarNovamente) {
                    alternarJogador();
                }
                // Se pode capturar novamente, mantém o mesmo jogador
            } else {
                // Movimento normal sempre alterna jogador
                alternarJogador();
            }
            
            return true;
            
        } catch (MovimentoInvalidoException | PosicaoInvalidaException e) {
            System.err.println("Erro no movimento: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Alterna entre os jogadores
     */
    private void alternarJogador() {
        jogadorAtual = (jogadorAtual == jogador1) ? jogador2 : jogador1;
    }
    
    /**
     * Salva o log da partida em arquivo
     */
    public void salvarLogPartida(String nomeArquivo) throws IOException {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.write("=== LOG DA PARTIDA DE DAMAS ===\n");
            writer.write(String.format("Data/Hora: %s\n", 
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            writer.write(String.format("Jogadores: %s vs %s\n\n", jogador1, jogador2));
            
            writer.write("=== HISTÓRICO DE MOVIMENTOS ===\n");
            for (String movimento : historicoMovimentos) {
                writer.write(movimento + "\n");
            }
            
            writer.write("\n=== RESULTADO FINAL ===\n");
            if (!jogoAtivo) {
                writer.write("Jogo finalizado\n");
                // Aqui você pode adicionar lógica para determinar o vencedor
            } else {
                writer.write("Jogo em andamento\n");
            }
            
            writer.write(String.format("Próximo jogador: %s\n", jogadorAtual.getNome()));
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
    
    public void finalizarJogo() {
        this.jogoAtivo = false;
    }
}