package damas.core;

import damas.exceptions.MovimentoInvalidoException;
import damas.exceptions.PosicaoInvalidaException;

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
        int diferencaColuna = destino.getColuna() - posicao.getColuna();
        
        // Peça simples só move na diagonal
        if (Math.abs(diferencaColuna) != Math.abs(diferencaLinha)) {
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

            try {
                if (tabuleiro.getPeca(destino) != null) {
                    throw new MovimentoInvalidoException("Destino ocupado");
                }
            } catch (PosicaoInvalidaException e) {
                throw new MovimentoInvalidoException("Posição inválida", e);
            }
        } else if (Math.abs(diferencaLinha) == 2) {
            int meioLinha = (posicao.getLinha() + destino.getLinha()) / 2;
            int meioColuna = (posicao.getColuna() + destino.getColuna()) / 2;
            Posicao meio = new Posicao(meioLinha, meioColuna);

            try {
                Peca p = tabuleiro.getPeca(meio);
                if (p == null || p.getCor() == this.cor) {
                    throw new MovimentoInvalidoException("Captura inválida");
                }
                if (tabuleiro.getPeca(destino) != null) {
                    throw new MovimentoInvalidoException("Destino ocupado");
                }
            } catch (PosicaoInvalidaException e) {
                throw new MovimentoInvalidoException("Posição inválida", e);
            }
        } else {
            throw new MovimentoInvalidoException("Peça simples só pode mover 1 ou 2 casas");
        }

        return true;
    }
}