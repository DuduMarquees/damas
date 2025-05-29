package damas.core;

import java.io.*;
import java.util.Scanner;

/**
 * Programa P1 - Cria configuração inicial do jogo e salva em formato binário
 */
public class P1 {
    public static void main(String[] args) {
        try {
            System.out.println("=== Inicializador do Jogo de Damas ===");
            
            // Lê dados iniciais do arquivo texto
            ConfiguracaoJogo config = lerConfiguracaoArquivo("config_inicial.txt");
            
            // Salva em formato binário
            salvarConfiguracaoBinaria(config, "jogo_config.dat");
            
            System.out.println("Configuração salva com sucesso!");
            
        } catch (IOException e) {
            System.err.println("Erro ao processar arquivos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Lê configurações iniciais do arquivo texto
     */
    private static ConfiguracaoJogo lerConfiguracaoArquivo(String nomeArquivo) throws IOException {
        ConfiguracaoJogo config = new ConfiguracaoJogo();
        
        try (Scanner scanner = new Scanner(new File(nomeArquivo))) {
            // Lê nome do jogador 1
            config.setNomeJogador1(scanner.nextLine().trim());
            
            // Lê nome do jogador 2  
            config.setNomeJogador2(scanner.nextLine().trim());
            
            // Lê cor do jogador 1 (BRANCA ou PRETA)
            String corJogador1 = scanner.nextLine().trim();
            config.setCorJogador1(CorPeca.valueOf(corJogador1.toUpperCase()));
            
            // Define cor do jogador 2 automaticamente
            config.setCorJogador2(config.getCorJogador1() == CorPeca.BRANCA ? 
                                 CorPeca.PRETA : CorPeca.BRANCA);
        }
        
        return config;
    }
    
    /**
     * Salva configuração em formato binário
     */
    private static void salvarConfiguracaoBinaria(ConfiguracaoJogo config, String nomeArquivo) 
            throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(nomeArquivo))) {
            oos.writeObject(config);
        }
    }
}