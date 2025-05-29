package damas.core;

/**
 * Enumeração das cores das peças
 */
public enum CorPeca {
    BRANCA("Branca"),
    PRETA("Preta");
    
    private final String nome;
    
    CorPeca(String nome) {
        this.nome = nome;
    }
    
    public String getNome() {
        return nome;
    }
    
    @Override
    public String toString() {
        return nome;
    }
}
