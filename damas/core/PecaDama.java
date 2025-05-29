package damas.core;

import damas.exceptions.MovimentoInvalidoException;

/**
 * Representa uma dama do jogo de damas
 * ESPECIALIZAÇÃO da classe Peca
 */
public class PecaDama extends Peca {
    private static final long serialVersionUID = 1L;
    
    public PecaDama(CorPeca cor, Posicao posicao) {
        super(cor, posicao);
    }
    
    @Override
    public String getTipo() {
        return "Dama";
    }
    
    @Override
    public boolean podeMoverPara(Posicao destino, Tabuleiro tabuleiro) 
            throws MovimentoInvalidoException {
        
        int diferencaLinha = destino.getLinha() - posicao.getLinha();
        int diferencaColuna = destino.getColuna() - posicao.getColuna();
        
        // Dama move apenas na diagonal
        if (Math.abs(diferencaLinha) != Math.abs(diferencaColuna)) {
            throw new MovimentoInvalidoException("Dama só pode mover na diagonal");
        }
        
        // Dama pode mover qualquer número de casas na diagonal
        // Para simplicidade, vamos permitir movimentos de 1 ou 2 casas
        int distancia = Math.abs(diferencaLinha);
        if (distancia < 1 || distancia > 2) {
            throw new MovimentoInvalidoException("Dama pode mover 1 ou 2 casas na diagonal");
        }
        
        // Dama pode mover em qualquer direção (diferente da peça simples)
        return true;
    }
}