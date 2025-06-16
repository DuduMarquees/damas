package damas.ui;

import damas.core.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class P2 {
    public static void main(String[] args) {
        try {
            ConfiguracaoJogo config = carregarConfiguracaoTexto("config_inicial.txt"); //Carrega configuração base
            mostrarDialogoConfiguracao(config); //Permite edição via interface gráfica
            salvarConfiguracaoTexto(config, "config_inicial.txt"); //Salva configurações atualizadas
            salvarConfiguracaoBinaria(config, "jogo_config.dat"); 
            // cria o jogo e inicia a interface
            Jogo jogo = new Jogo(config);
            new InterfaceJogo(jogo).iniciar();
        } catch (Exception e) {
            System.err.println("Erro ao iniciar: " + e.getMessage());
        }
    }

    private static ConfiguracaoJogo carregarConfiguracaoTexto(String arquivo) throws IOException {
        ConfiguracaoJogo config = new ConfiguracaoJogo();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            config.setNomeJogador1(br.readLine().trim());
            config.setNomeJogador2(br.readLine().trim());
            String cor = br.readLine().trim();
            config.setCorJogador1(CorPeca.valueOf(cor.toUpperCase()));
            config.setCorJogador2(config.getCorJogador1() == CorPeca.BRANCA ? CorPeca.PRETA : CorPeca.BRANCA);
        }
        return config;
    }

    private static void salvarConfiguracaoTexto(ConfiguracaoJogo config, String arquivo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
            pw.println(config.getNomeJogador1());
            pw.println(config.getNomeJogador2());
            pw.print(config.getCorJogador1());
        }
    }

    private static void salvarConfiguracaoBinaria(ConfiguracaoJogo config, String arquivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(config);
        }
    }

    private static void mostrarDialogoConfiguracao(ConfiguracaoJogo config) {
        JDialog dialog = new JDialog((Frame) null, "Configurar Jogadores", true);
        dialog.setLayout(new BorderLayout());

        JTextField campoBrancas = new JTextField();
        JTextField campoPretas = new JTextField();

        if (config.getCorJogador1() == CorPeca.BRANCA) {
            campoBrancas.setText(config.getNomeJogador1());
            campoPretas.setText(config.getNomeJogador2());
        } else {
            campoBrancas.setText(config.getNomeJogador2());
            campoPretas.setText(config.getNomeJogador1());
        }

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Jogador peças brancas:"));
        panel.add(campoBrancas);
        panel.add(new JLabel("Jogador peças pretas:"));
        panel.add(campoPretas);

        JButton btnSalvar = new JButton("Salvar");
        panel.add(new JLabel());
        panel.add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            String nomeBrancas = campoBrancas.getText().trim();
            String nomePretas = campoPretas.getText().trim();
            if (config.getCorJogador1() == CorPeca.BRANCA) {
                config.setNomeJogador1(nomeBrancas);
                config.setNomeJogador2(nomePretas);
            } else {
                config.setNomeJogador1(nomePretas);
                config.setNomeJogador2(nomeBrancas);
            }
            dialog.dispose();
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
