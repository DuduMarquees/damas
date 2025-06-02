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
    
    // Cores do tabuleiro
    private final Color COR_CLARA = new Color(240, 217, 181); // Bege claro
    private final Color COR_ESCURA = new Color(181, 136, 99); // Marrom claro
    
    public InterfaceJogo(Jogo jogo) {
        this.jogo = jogo;
        this.botoes = new JButton[8][8];
        this.selecionada = null;
        
        setTitle("Jogo de Damas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Painel de informações
        infoLabel = new JLabel("", JLabel.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(infoLabel, BorderLayout.NORTH);
        
        // Tabuleiro
        JPanel tabuleiro = new JPanel(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(80, 80));
                btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
                btn.setHorizontalAlignment(SwingConstants.CENTER);
                
                // Configuração CRUCIAL para o fundo visível
                btn.setContentAreaFilled(false);
                btn.setOpaque(true);
                btn.setBorderPainted(false);
                
                // Define cores alternadas do tabuleiro
                btn.setBackground((i + j) % 2 == 0 ? COR_CLARA : COR_ESCURA);
                
                final int linha = i, coluna = j;
                btn.addActionListener(e -> aoClicar(linha, coluna));
                
                botoes[i][j] = btn;
                tabuleiro.add(btn);
            }
        }
        add(tabuleiro, BorderLayout.CENTER);
        
        // Botão de log
        JButton btnSalvar = new JButton("Salvar Log");
        btnSalvar.addActionListener(e -> salvarLog());
        add(btnSalvar, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        atualizarInterface();
    }

    private void atualizarInterface() {
        // Atualiza peças
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                try {
                    Peca peca = jogo.getTabuleiro().getPeca(new Posicao(i, j));
                    JButton btn = botoes[i][j];
                    btn.setText("");
                    btn.setBorder(null);
                    
                    if (peca != null) {
                        if (peca instanceof PecaDama) {
                            btn.setText(peca.getCor() == CorPeca.BRANCA ? "🫅" : "👑");
                        } else {
                            btn.setText(peca.getCor() == CorPeca.BRANCA ? "🔴" : "⚫");
                        }
                    }
                } catch (Exception e) {
                    botoes[i][j].setText("");
                }
            }
        }
        
        // Atualiza informação do jogador
        infoLabel.setText("Vez de: " + jogo.getJogadorAtual().getNome() + 
                        " (Peças: " + jogo.getJogadorAtual().getCor() + ")");
        infoLabel.setForeground(jogo.getJogadorAtual().getCor() == CorPeca.BRANCA ? Color.RED : Color.BLACK);
    }

    private void aoClicar(int linha, int coluna) {
        Posicao pos = new Posicao(linha, coluna);
        
        try {
            if (selecionada == null) {
                Peca peca = jogo.getTabuleiro().getPeca(pos);
                if (peca != null && peca.getCor() == jogo.getJogadorAtual().getCor()) {
                    selecionada = pos;
                    botoes[linha][coluna].setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
                    
                    // Mostra movimentos válidos
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            Posicao destino = new Posicao(i, j);
                            if (jogo.getTabuleiro().podeMover(selecionada, destino)) {
                                botoes[i][j].setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                            }
                        }
                    }
                } else {
                    infoLabel.setText("ATENÇÃO: É a vez das peças " + jogo.getJogadorAtual().getCor());
                }
            } else {
                if (selecionada.equals(pos)) {
                    // Desseleciona
                    selecionada = null;
                    atualizarInterface();
                } else if (jogo.executarMovimento(selecionada, pos)) {
                    selecionada = null;
                    atualizarInterface();
                } else {
                    infoLabel.setText("Movimento inválido! Tente novamente.");
                }
            }
        } catch (Exception e) {
            infoLabel.setText("Erro: " + e.getMessage());
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