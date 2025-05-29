package damas.exceptions;

/**
 * Exceção lançada quando uma posição inválida é acessada
 */
public class PosicaoInvalidaException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public PosicaoInvalidaException(String mensagem) {
        super(mensagem);
    }
    
    public PosicaoInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}