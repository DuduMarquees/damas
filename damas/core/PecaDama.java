package damas.core;

import damas.core.Tabuleiro;
import damas.exceptions.MovimentoInvalidoException;

public class PecaDama extends Peca {
    private static final long serialVersionUID = 1L;

    public PecaDama(CorPeca cor, Posicao posicao) {
        super(cor, posicao);
    }

    @Override
    public boolean podeMoverPara(Posicao destino, Tabuleiro tabuleiro) throws MovimentoInvalidoException {
        int diffLinha = Math.abs(destino.getLinha() - posicao.getLinha());
        int diffColuna = Math.abs(destino.getColuna() - posicao.getColuna());

        // 1. Verifica se é diagonal
        if (diffLinha != diffColuna) {
            throw new MovimentoInvalidoException("Dama deve mover na diagonal");
        }

        // 2. Verifica peças no caminho
        int passoLinha = Integer.compare(destino.getLinha(), posicao.getLinha());
        int passoColuna = Integer.compare(destino.getColuna(), posicao.getColuna());
    
        int pecasNoCaminho = 0;
        Posicao posicaoCaptura = null;

        for (int i = 1; i < diffLinha; i++) {
            Posicao atual = new Posicao(
                posicao.getLinha() + (i * passoLinha),
                posicao.getColuna() + (i * passoColuna)
            );
        
            if (tabuleiro.getPeca(atual) != null) {  // <--- CORREÇÃO AQUI
                pecasNoCaminho++;
                posicaoCaptura = atual;
            }
        }

        // 3. Regras de movimento
        if (pecasNoCaminho == 0) {
            return true; // Movimento livre
        } 
        else if (pecasNoCaminho == 1 && diffLinha > 1) {
            // Verifica se é captura válida
            Peca pecaNoCaminho = tabuleiro.getPeca(posicaoCaptura);  // <--- Usando a instância
            return pecaNoCaminho.getCor() != this.getCor();
        }

        throw new MovimentoInvalidoException("Movimento inválido para dama");
    }
    @Override
    public String getTipo() {
        return "Dama";
    }
}