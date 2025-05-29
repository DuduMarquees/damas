package damas.ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import damas.core.*;


public class InterfaceJogo extends JFrame {
    private Jogo jogo;
    private JButton[][] botoes;
    private JLabel infoLabel;
    private Posicao selecionada;
    
    public InterfaceJogo(Jogo jogo) {
        this.jogo = jogo;
        this.botoes = new JButton[8][8];
        this.selecionada = null;
        
        // Criar interface
        setTitle("Eduardo vs Victor - Damas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Info no topo
        infoLabel = new JLabel("Clique numa peça", JLabel.CENTER);
        add(infoLabel, BorderLayout.NORTH);
        
        // Tabuleiro 8x8
        JPanel tabuleiro = new JPanel(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(60, 60));
                btn.setFont(new Font("Arial", Font.BOLD, 24));
                btn.setOpaque(true);
                
                // Cores alternadas
                if ((i + j) % 2 == 0) {
                    btn.setBackground(new Color(240, 217, 181)); // Bege
                } else {
                    btn.setBackground(new Color(160, 110, 80));  // Marrom
                }
                
                final int linha = i, coluna = j;
                btn.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        aoClicar(linha, coluna);
                    }
                });
                
                botoes[i][j] = btn;
                tabuleiro.add(btn);
            }
        }
        add(tabuleiro, BorderLayout.CENTER);
        
        // Botão salvar
        JButton btnSalvar = new JButton("Salvar Log");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                salvarLog();
            }
        });
        add(btnSalvar, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        
        // Colocar peças inicial
        atualizarPecas();
    }
    
    private void atualizarPecas() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                try {
                    Peca peca = jogo.getTabuleiro().getPeca(new Posicao(i, j));
                    if (peca != null) {
                        if (peca.getCor() == CorPeca.BRANCA) {
                            botoes[i][j].setText("🔴");
                            botoes[i][j].setForeground(Color.RED);
                        } else {
                            botoes[i][j].setText("⚫");
                            botoes[i][j].setForeground(Color.BLACK);
                        }
                    } else {
                        botoes[i][j].setText("");
                    }
                } catch (Exception e) {
                    botoes[i][j].setText("");
                }
            }
        }
    }
    
    private void aoClicar(int linha, int coluna) {
        Posicao pos = new Posicao(linha, coluna);
        
        if (selecionada == null) {
            // Selecionar peça
            try {
                Peca peca = jogo.getTabuleiro().getPeca(pos);
                if (peca != null && peca.getCor() == jogo.getJogadorAtual().getCor()) {
                    selecionada = pos;
                    botoes[linha][coluna].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                    infoLabel.setText("Peça selecionada - clique no destino");
                } else {
                    infoLabel.setText("Selecione uma peça sua!");
                }
            } catch (Exception e) {
                infoLabel.setText("Erro: " + e.getMessage());
            }
        } else {
            // Mover ou desselecionar
            if (selecionada.equals(pos)) {
                // Desselecionar
                botoes[selecionada.getLinha()][selecionada.getColuna()].setBorder(null);
                selecionada = null;
                infoLabel.setText("Seleção cancelada");
            } else {
                // Tentar mover
                if (jogo.executarMovimento(selecionada, pos)) {
                    // Movimento OK
                    botoes[selecionada.getLinha()][selecionada.getColuna()].setBorder(null);
                    selecionada = null;
                    atualizarPecas();
                    infoLabel.setText("Movimento realizado! Vez de: " + jogo.getJogadorAtual().getNome());
                } else {
                    infoLabel.setText("Movimento inválido!");
                }
            }
        }
    }
    
    private void salvarLog() {
        try {
            jogo.salvarLogPartida("damas_log.txt");
            infoLabel.setText("Log salvo em damas_log.txt");
        } catch (IOException e) {
            infoLabel.setText("Erro ao salvar: " + e.getMessage());
        }
    }
    
    public void iniciar() {
        setVisible(true);
    }
}