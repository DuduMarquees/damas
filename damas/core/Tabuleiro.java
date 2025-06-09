package damas.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import damas.exceptions.*;

public class Tabuleiro implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int TAMANHO = 8;
    
    private Peca[][] grade;
    private List<Peca> pecasCapturadas;

    public Tabuleiro() {
        this.grade = new Peca[TAMANHO][TAMANHO];
        this.pecasCapturadas = new ArrayList<>();
        inicializarTabuleiro();
    }

    private void inicializarTabuleiro() {
        // Peças pretas (topo do tabuleiro)
        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                if ((linha + coluna) % 2 == 1) {
                    grade[linha][coluna] = new PecaSimples(CorPeca.PRETA, new Posicao(linha, coluna));
                }
            }
        }

        // Peças brancas (base do tabuleiro)
        for (int linha = 5; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                if ((linha + coluna) % 2 == 1) {
                    grade[linha][coluna] = new PecaSimples(CorPeca.BRANCA, new Posicao(linha, coluna));
                }
            }
        }
    }

    public Peca getPeca(Posicao posicao) throws PosicaoInvalidaException {
        validarPosicao(posicao);
        return grade[posicao.getLinha()][posicao.getColuna()];
    }

    public void moverPeca(Posicao origem, Posicao destino) 
        throws PosicaoInvalidaException, MovimentoInvalidoException {
    
        Peca peca = getPeca(origem);
        if (peca == null) throw new MovimentoInvalidoException("Sem peça na origem");

        if (!peca.podeMoverPara(destino, this)) {
            throw new MovimentoInvalidoException("Movimento inválido");
        }

        if (getPeca(destino) != null) {
            throw new MovimentoInvalidoException("Destino ocupado");
        }

        // Trata captura (para damas)
        int diffLinha = Math.abs(destino.getLinha() - origem.getLinha());
        if (diffLinha >= 2) {
            int passoLinha = Integer.compare(destino.getLinha(), origem.getLinha());
            int passoColuna = Integer.compare(destino.getColuna(), origem.getColuna());
        
            for (int i = 1; i < diffLinha; i++) {
                Posicao atual = new Posicao(
                    origem.getLinha() + (i * passoLinha),
                    origem.getColuna() + (i * passoColuna)
                );
            
                if (getPeca(atual) != null) {
                grade[atual.getLinha()][atual.getColuna()] = null;
                System.out.println("Peça capturada em " + atual);
                }
            }
        }

    // Efetua movimento
    grade[origem.getLinha()][origem.getColuna()] = null;
    grade[destino.getLinha()][destino.getColuna()] = peca;
    peca.setPosicao(destino);
}

    @SuppressWarnings("unused")
    private void capturarPeca(Posicao origem, Posicao destino) 
            throws PosicaoInvalidaException, MovimentoInvalidoException {
        
        int linhaMeio = (origem.getLinha() + destino.getLinha()) / 2;
        int colunaMeio = (origem.getColuna() + destino.getColuna()) / 2;
        Posicao posicaoMeio = new Posicao(linhaMeio, colunaMeio);
        
        Peca pecaCapturada = getPeca(posicaoMeio);
        if (pecaCapturada == null) {
            throw new MovimentoInvalidoException("Não há peça para capturar");
        }

        grade[linhaMeio][colunaMeio] = null;
        pecasCapturadas.add(pecaCapturada);
    }

    protected void verificarPromocaoDama(Peca peca) {
        if (peca instanceof PecaSimples) {
            int linha = peca.getPosicao().getLinha();
            boolean devePromover = (peca.getCor() == CorPeca.BRANCA && linha == 0) || 
                                 (peca.getCor() == CorPeca.PRETA && linha == 7);
            
            if (devePromover) {
                Posicao pos = peca.getPosicao();
                grade[pos.getLinha()][pos.getColuna()] = new PecaDama(peca.getCor(), pos);
            }
        }
    }

    public boolean podeMover(Posicao origem, Posicao destino) {
        try {
            Peca peca = getPeca(origem);
            return peca != null &&
                   peca.podeMoverPara(destino, this) &&
                   getPeca(destino) == null;
        } catch (Exception e) {
            return false;
        }
    }

    public int contarPecas(CorPeca cor) {
        int contador = 0;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                Peca p = grade[i][j];
                if (p != null && p.getCor() == cor) {
                    contador++;
                }
            }
        }
        return contador;
    }

    private void validarPosicao(Posicao posicao) throws PosicaoInvalidaException {
        if (posicao.getLinha() < 0 || posicao.getLinha() >= TAMANHO ||
            posicao.getColuna() < 0 || posicao.getColuna() >= TAMANHO) {
            throw new PosicaoInvalidaException("Posição inválida: " + posicao);
        }
    }

    // Getters
    public static int getTamanho() { return TAMANHO; }
    public List<Peca> getPecasCapturadas() { return new ArrayList<>(pecasCapturadas); }
}