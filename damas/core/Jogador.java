package damas.core;

import java.io.Serializable;

/**
 * Representa um jogador do jogo
 */
public class Jogador implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nome;
    private CorPeca cor;
    private int pecasCapturadas;
    
    public Jogador(String nome, CorPeca cor) {
        this.nome = nome;
        this.cor = cor;
        this.pecasCapturadas = 0;
    }
    
    // Getters e Setters com encapsulamento
    public String getNome() {
        return nome;
    }
    
    public CorPeca getCor() {
        return cor;
    }
    
    public int getPecasCapturadas() {
        return pecasCapturadas;
    }
    
    public void incrementarPecasCapturadas() {
        this.pecasCapturadas++;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %d peças capturadas", 
                           nome, cor, pecasCapturadas);
    }
}