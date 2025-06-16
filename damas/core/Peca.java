package damas.core;

import java.io.Serializable;
import damas.exceptions.MovimentoInvalidoException;

/**
 * Classe abstrata que representa uma peça do jogo
 * Demonstração de HERANÇA e POLIMORFISMO
 */
public abstract class Peca implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected CorPeca cor; // Atributo protegido para acesso pelas subclasses
    protected Posicao posicao;
    
    public Peca(CorPeca cor, Posicao posicao) {
        this.cor = cor;
        this.posicao = posicao;
    }
    
    // Getters
    public CorPeca getCor() { return cor; }
    public Posicao getPosicao() { return posicao; }
    
    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }
    
    // Método abstrato - deve ser implementado pelas subclasses (POLIMORFISMO)
    public abstract boolean podeMoverPara(Posicao destino, Tabuleiro tabuleiro) 
            throws MovimentoInvalidoException;
    
    // Método ABSTRATO - comportamento específico de cada tipo de peça
    public abstract String getTipo();
    
    @Override
    public String toString() {
        return String.format("%s %s em %s", cor, getTipo(), posicao);
    }
}