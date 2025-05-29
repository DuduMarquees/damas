package damas.ui;

import java.io.*;
import damas.core.*;

/**
 * Programa P2 - Carrega configuração binária e inicia interface gráfica
 */
public class P2 {
    public static void main(String[] args) {
        try {
            System.out.println("=== Jogo de Damas - Interface Gráfica ===");
            
            // Carrega configuração do arquivo binário
            ConfiguracaoJogo config = carregarConfiguracaoBinaria("jogo_config.dat");
            
            System.out.println("Configuração carregada: " + config);
            
            // Cria o jogo
            Jogo jogo = new Jogo(config);
            
            // Inicia interface gráfica
            InterfaceJogo interfaceJogo = new InterfaceJogo(jogo);
            interfaceJogo.iniciar();
            
        } catch (IOException e) {
            System.err.println("Erro ao carregar configuração: " + e.getMessage());
            System.err.println("Certifique-se de executar o programa P1 primeiro!");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro na estrutura do arquivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Carrega configuração do arquivo binário
     */
    private static ConfiguracaoJogo carregarConfiguracaoBinaria(String nomeArquivo) 
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(nomeArquivo))) {
            return (ConfiguracaoJogo) ois.readObject();
        }
    }
}