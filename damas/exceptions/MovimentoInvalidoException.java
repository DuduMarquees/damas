package damas.exceptions;

/**
 * Exceção lançada quando um movimento inválido é tentado
 */
public class MovimentoInvalidoException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public MovimentoInvalidoException(String mensagem) {
        super(mensagem);
    }
    
    public MovimentoInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
