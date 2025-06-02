package damas.ui;

import damas.core.*;
import java.io.*;

public class P2 {
    public static void main(String[] args) {
        try {
            ConfiguracaoJogo config = carregarConfiguracaoBinaria("jogo_config.dat");
            Jogo jogo = new Jogo(config);
            new InterfaceJogo(jogo).iniciar();
        } catch (Exception e) {
            System.err.println("Erro ao iniciar: " + e.getMessage());
        }
    }
    
    private static ConfiguracaoJogo carregarConfiguracaoBinaria(String arquivo) 
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(arquivo))) {
            return (ConfiguracaoJogo) ois.readObject();
        }
    }
}