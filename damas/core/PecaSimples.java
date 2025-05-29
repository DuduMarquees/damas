package damas.core;

import damas.exceptions.MovimentoInvalidoException;

/**
 * Representa uma peça simples do jogo de damas
 * ESPECIALIZAÇÃO da classe Peca
 */
public class PecaSimples extends Peca {
    private static final long serialVersionUID = 1L;
    
    public PecaSimples(CorPeca cor, Posicao posicao) {
        super(cor, posicao);
    }
    
    @Override
    public String getTipo() {
        return "Peça Simples";
    }
    
    @Override
    public boolean podeMoverPara(Posicao destino, Tabuleiro tabuleiro) 
            throws MovimentoInvalidoException {
        
        int diferencaLinha = destino.getLinha() - posicao.getLinha();
        int diferencaColuna = Math.abs(destino.getColuna() - posicao.getColuna());
        
        // Peça simples só move na diagonal
        if (diferencaColuna != Math.abs(diferencaLinha)) {
            throw new MovimentoInvalidoException("Peça simples só pode mover na diagonal");
        }
        
        // Movimento simples (1 casa) ou captura (2 casas)
        if (Math.abs(diferencaLinha) == 1) {
            // Movimento simples - verifica direção baseada na cor
            boolean direcaoCorreta;
            if (cor == CorPeca.BRANCA) {
                direcaoCorreta = diferencaLinha < 0; // Brancas sobem
            } else {
                direcaoCorreta = diferencaLinha > 0; // Pretas descem
            }
            
            if (!direcaoCorreta) {
                throw new MovimentoInvalidoException("Peça simples não pode mover para trás (só capturas)");
            }
        } else if (Math.abs(diferencaLinha) == 2) {
            // CAPTURA - PODE SER EM QUALQUER DIREÇÃO (inclusive para trás!)
            // Peças simples podem capturar para trás nas damas tradicionais
            return true;
        } else {
            throw new MovimentoInvalidoException("Peça simples só pode mover 1 ou 2 casas");
        }
        
        return true;
    }
}