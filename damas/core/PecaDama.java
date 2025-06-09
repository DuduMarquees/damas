package damas.core;


import damas.exceptions.MovimentoInvalidoException;
import damas.exceptions.PosicaoInvalidaException;

public class PecaDama extends Peca {
    private static final long serialVersionUID = 1L;

    public PecaDama(CorPeca cor, Posicao posicao) {
        super(cor, posicao);
    }

    @Override
    public boolean podeMoverPara(Posicao destino, Tabuleiro tabuleiro)
            throws MovimentoInvalidoException {

        int diffLinha  = Math.abs(destino.getLinha()   - posicao.getLinha());
        int diffColuna = Math.abs(destino.getColuna()  - posicao.getColuna());

        // 1) Só move na diagonal
        if (diffLinha != diffColuna)
            throw new MovimentoInvalidoException("Dama deve mover na diagonal");

        int passoLinha  = Integer.compare(destino.getLinha(),  posicao.getLinha());
        int passoColuna = Integer.compare(destino.getColuna(), posicao.getColuna());

        int pecasNoCaminho = 0;
        Posicao posicaoCaptura = null;

        for (int i = 1; i < diffLinha; i++) {
            Posicao atual = new Posicao(
                    posicao.getLinha()  + i * passoLinha,
                    posicao.getColuna() + i * passoColuna);

            try {
                Peca p = tabuleiro.getPeca(atual);
                if (p != null) {
                    pecasNoCaminho++;
                    posicaoCaptura = atual;
                }
            } catch (PosicaoInvalidaException e) {
                // Qualquer posição fora do tabuleiro invalida o lance
                throw new MovimentoInvalidoException("Posição fora do tabuleiro", e);
            }
        }

        if (pecasNoCaminho == 0) {
            return true;                    // caminho livre
        } else if (pecasNoCaminho == 1 && diffLinha > 1) {
            try {
                Peca pecaNoCaminho = tabuleiro.getPeca(posicaoCaptura);
                return pecaNoCaminho.getCor() != this.getCor(); // captura válida
            } catch (PosicaoInvalidaException e) {
                throw new MovimentoInvalidoException("Erro ao validar captura", e);
            }
        }

        throw new MovimentoInvalidoException("Movimento inválido para dama");
    }
    @Override
    public String getTipo() {
        return "Dama";
    }
}