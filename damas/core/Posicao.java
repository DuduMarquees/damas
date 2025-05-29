package damas.core;

import java.io.Serializable;

/**
 * Representa uma posição no tabuleiro
 */
public class Posicao implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int linha;
    private int coluna;
    
    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }
    
    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Posicao posicao = (Posicao) obj;
        return linha == posicao.linha && coluna == posicao.coluna;
    }
    
    @Override
    public int hashCode() {
        return linha * 31 + coluna;
    }
    
    @Override
    public String toString() {
        return String.format("(%d,%d)", linha, coluna);
    }
}