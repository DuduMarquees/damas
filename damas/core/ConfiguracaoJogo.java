package damas.core;

import java.io.Serializable;

/**
 * Classe que representa a configuração inicial do jogo
 */
public class ConfiguracaoJogo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nomeJogador1;
    private String nomeJogador2;
    private CorPeca corJogador1;
    private CorPeca corJogador2;
    
    // Construtor padrão
    public ConfiguracaoJogo() {}
    
    // Getters e Setters
    public String getNomeJogador1() {
        return nomeJogador1;
    }
    
    public void setNomeJogador1(String nomeJogador1) {
        this.nomeJogador1 = nomeJogador1;
    }
    
    public String getNomeJogador2() {
        return nomeJogador2;
    }
    
    public void setNomeJogador2(String nomeJogador2) {
        this.nomeJogador2 = nomeJogador2;
    }
    
    public CorPeca getCorJogador1() {
        return corJogador1;
    }
    
    public void setCorJogador1(CorPeca corJogador1) {
        this.corJogador1 = corJogador1;
    }
    
    public CorPeca getCorJogador2() {
        return corJogador2;
    }
    
    public void setCorJogador2(CorPeca corJogador2) {
        this.corJogador2 = corJogador2;
    }
    
    @Override
    public String toString() {
        return String.format("Jogador 1: %s (%s) vs Jogador 2: %s (%s)", 
                           nomeJogador1, corJogador1, nomeJogador2, corJogador2);
    }
}