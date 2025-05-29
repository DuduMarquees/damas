package damas.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import damas.exceptions.PosicaoInvalidaException;
import damas.exceptions.MovimentoInvalidoException;

/**
 * Representa o tabuleiro do jogo de damas
 */
public class Tabuleiro implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int TAMANHO = 8;
    
    private Peca[][] grade; // Matriz para armazenar as peças
    private List<Peca> pecasCapturadas; // Lista de peças capturadas
    
    public Tabuleiro() {
        this.grade = new Peca[TAMANHO][TAMANHO];
        this.pecasCapturadas = new ArrayList<>();
        inicializarTabuleiro();
    }
    
    /**
     * Inicializa o tabuleiro com as peças na posição inicial
     * APENAS EM CASAS ESCURAS (como nas damas tradicionais)
     */
    private void inicializarTabuleiro() {
        System.out.println("=== DEBUG: Inicializando tabuleiro ===");
        
        // Coloca peças pretas nas 3 primeiras fileiras (APENAS casas escuras)
        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                if ((linha + coluna) % 2 == 1) { // Apenas casas escuras
                    Posicao pos = new Posicao(linha, coluna);
                    PecaSimples peca = new PecaSimples(CorPeca.PRETA, pos);
                    grade[linha][coluna] = peca;
                    System.out.println("Criou peça PRETA em (" + linha + "," + coluna + ")");
                }
            }
        }
        
        // Coloca peças brancas nas 3 últimas fileiras (APENAS casas escuras)  
        for (int linha = 5; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                if ((linha + coluna) % 2 == 1) { // Apenas casas escuras
                    Posicao pos = new Posicao(linha, coluna);
                    PecaSimples peca = new PecaSimples(CorPeca.BRANCA, pos);
                    grade[linha][coluna] = peca;
                    System.out.println("Criou peça BRANCA em (" + linha + "," + coluna + ")");
                }
            }
        }
        
        System.out.println("=== DEBUG: Tabuleiro inicializado ===");
    }
    
    /**
     * Método que repassa exceções (SEM try-catch)
     */
    public Peca getPeca(Posicao posicao) throws PosicaoInvalidaException {
        validarPosicao(posicao);
        Peca peca = grade[posicao.getLinha()][posicao.getColuna()];
        
        // DEBUG: Log só para as primeiras chamadas
        if (posicao.getLinha() < 3 || posicao.getLinha() > 4) {
            if (peca != null) {
                System.out.println("getPeca(" + posicao.getLinha() + "," + posicao.getColuna() + ") = " + peca.getCor());
            } else {
                System.out.println("getPeca(" + posicao.getLinha() + "," + posicao.getColuna() + ") = NULL");
            }
        }
        
        return peca;
    }
    
    /**
     * Outro método que repassa exceções (SEM try-catch)
     */
    public void moverPeca(Posicao origem, Posicao destino) throws PosicaoInvalidaException, MovimentoInvalidoException {
        validarPosicao(origem);
        validarPosicao(destino);
        
        Peca peca = getPeca(origem);
        if (peca == null) {
            throw new MovimentoInvalidoException("Não há peça na posição de origem");
        }
        
        // NOVA REGRA: Verificar se destino é casa escura
        if ((destino.getLinha() + destino.getColuna()) % 2 == 0) {
            throw new MovimentoInvalidoException("Peças só podem mover para casas escuras!");
        }
        
        // NOVA REGRA: Verificar capturas obrigatórias
        List<Posicao> capturasObrigatorias = getCapturasObrigatorias(peca.getCor());
        boolean temCapturaObrigatoria = !capturasObrigatorias.isEmpty();
        
        // Verifica se o movimento é válido usando POLIMORFISMO
        if (!peca.podeMoverPara(destino, this)) {
            throw new MovimentoInvalidoException("Movimento inválido para esta peça");
        }
        
        // Verifica se destino está ocupado
        if (getPeca(destino) != null) {
            throw new MovimentoInvalidoException("Posição de destino já ocupada");
        }
        
        // Verifica se é uma captura (movimento de 2 casas)
        int diferencaLinha = Math.abs(destino.getLinha() - origem.getLinha());
        int diferencaColuna = Math.abs(destino.getColuna() - origem.getColuna());
        boolean ehCaptura = (diferencaLinha == 2 && diferencaColuna == 2);
        
        // NOVA REGRA: Se tem captura obrigatória mas não está capturando
        if (temCapturaObrigatoria && !ehCaptura) {
            throw new MovimentoInvalidoException("CAPTURA OBRIGATÓRIA! Você deve capturar quando possível!");
        }
        
        // NOVA REGRA: Se está tentando capturar mas a peça origem não está na lista de capturas obrigatórias
        if (ehCaptura && temCapturaObrigatoria && !capturasObrigatorias.contains(origem)) {
            throw new MovimentoInvalidoException("Você deve capturar com uma das peças marcadas!");
        }
        
        if (ehCaptura) {
            // É uma captura! Vamos capturar a peça no meio
            capturarPeca(origem, destino);
        }
        
        // Realiza o movimento
        grade[origem.getLinha()][origem.getColuna()] = null;
        grade[destino.getLinha()][destino.getColuna()] = peca;
        peca.setPosicao(destino);
        
        // Verifica se deve virar dama
        verificarPromocaoDama(peca);
    }
    
    /**
     * NOVA FUNCIONALIDADE: Retorna lista de posições que podem fazer capturas obrigatórias
     */
    public List<Posicao> getCapturasObrigatorias(CorPeca cor) {
        List<Posicao> capturas = new ArrayList<>();
        
        for (int linha = 0; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                try {
                    Posicao pos = new Posicao(linha, coluna);
                    Peca peca = getPeca(pos);
                    if (peca != null && peca.getCor() == cor) {
                        if (podeCapturar(pos)) {
                            capturas.add(pos);
                        }
                    }
                } catch (PosicaoInvalidaException e) {
                    // Ignora posições inválidas
                }
            }
        }
        
        return capturas;
    }
    
    /**
     * NOVA FUNCIONALIDADE: Verifica se uma peça pode capturar
     */
    public boolean podeCapturar(Posicao origem) throws PosicaoInvalidaException {
        Peca peca = getPeca(origem);
        if (peca == null) return false;
        
        // Verifica todas as 4 direções diagonais para capturas
        int[][] direcoes = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
        
        for (int[] dir : direcoes) {
            int novaLinha = origem.getLinha() + dir[0];
            int novaColuna = origem.getColuna() + dir[1];
            
            if (novaLinha >= 0 && novaLinha < TAMANHO && 
                novaColuna >= 0 && novaColuna < TAMANHO) {
                
                Posicao destino = new Posicao(novaLinha, novaColuna);
                
                try {
                    // Verifica se destino está vazio
                    if (getPeca(destino) == null) {
                        // Verifica se há peça adversária no meio
                        int linhaMeio = origem.getLinha() + dir[0]/2;
                        int colunaMeio = origem.getColuna() + dir[1]/2;
                        Posicao meio = new Posicao(linhaMeio, colunaMeio);
                        
                        Peca pecaMeio = getPeca(meio);
                        if (pecaMeio != null && pecaMeio.getCor() != peca.getCor()) {
                            // Verifica se a peça pode mover nesta direção
                            try {
                                if (peca.podeMoverPara(destino, this)) {
                                    return true;
                                }
                            } catch (MovimentoInvalidoException e) {
                                // Esta direção não é válida, tenta próxima
                            }
                        }
                    }
                } catch (PosicaoInvalidaException e) {
                    // Esta posição não é válida, tenta próxima
                }
            }
        }
        
        return false;
    }
    
    /**
     * Captura uma peça que está entre origem e destino
     */
    private void capturarPeca(Posicao origem, Posicao destino) throws MovimentoInvalidoException, PosicaoInvalidaException {
        // Calcula posição da peça no meio
        int linhaMeio = (origem.getLinha() + destino.getLinha()) / 2;
        int colunaMeio = (origem.getColuna() + destino.getColuna()) / 2;
        Posicao posicaoMeio = new Posicao(linhaMeio, colunaMeio);
        
        Peca pecaCapturada = getPeca(posicaoMeio);
        if (pecaCapturada == null) {
            throw new MovimentoInvalidoException("Não há peça para capturar!");
        }
        
        Peca pecaAtacante = getPeca(origem);
        if (pecaCapturada.getCor() == pecaAtacante.getCor()) {
            throw new MovimentoInvalidoException("Não pode capturar peça da mesma cor!");
        }
        
        // Remove a peça capturada
        grade[linhaMeio][colunaMeio] = null;
        pecasCapturadas.add(pecaCapturada);
        
        System.out.println("Peça capturada: " + pecaCapturada);
    }
    
    /**
     * Verifica se uma peça deve ser promovida a dama
     */
    private void verificarPromocaoDama(Peca peca) {
        if (peca instanceof PecaSimples) {
            boolean devePromover = false;
            
            if (peca.getCor() == CorPeca.BRANCA && peca.getPosicao().getLinha() == 0) {
                devePromover = true;
            } else if (peca.getCor() == CorPeca.PRETA && peca.getPosicao().getLinha() == 7) {
                devePromover = true;
            }
            
            if (devePromover) {
                Posicao pos = peca.getPosicao();
                grade[pos.getLinha()][pos.getColuna()] = new PecaDama(peca.getCor(), pos);
            }
        }
    }
    
    /**
     * Valida se uma posição está dentro dos limites do tabuleiro
     */
    private void validarPosicao(Posicao posicao) throws PosicaoInvalidaException {
        if (posicao.getLinha() < 0 || posicao.getLinha() >= TAMANHO ||
            posicao.getColuna() < 0 || posicao.getColuna() >= TAMANHO) {
            throw new PosicaoInvalidaException("Posição fora dos limites do tabuleiro: " + posicao);
        }
    }
    
    public static int getTamanho() {
        return TAMANHO;
    }
    
    public List<Peca> getPecasCapturadas() {
        return new ArrayList<>(pecasCapturadas);
    }
    
    public boolean posicaoVazia(Posicao posicao) throws PosicaoInvalidaException {
        return getPeca(posicao) == null;
    }
}