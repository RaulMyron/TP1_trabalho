package telas.prestacaoservico.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuPrestacaoServico extends JFrame {
    
    public MenuPrestacaoServico() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Gestão de Prestadores de Serviço");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        JLabel lblTitulo = new JLabel("Gestão de Prestadores de Serviço");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        painelTitulo.add(lblTitulo);
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(5, 1, 10, 10));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JButton btnCadastrarPrestador = new JButton("Cadastro de Prestadores");
        JButton btnGestaoContratos = new JButton("Gestão de Contratos");
        JButton btnRelatorios = new JButton("Relatórios");
        JButton btnAlertas = new JButton("Alertas de Vencimento");
        JButton btnSair = new JButton("Voltar");
        
        btnCadastrarPrestador.addActionListener((ActionEvent e) -> {
            new CadastroPrestadores().setVisible(true);
        });
        
        btnGestaoContratos.addActionListener((ActionEvent e) -> {
            new GestaoContratos().setVisible(true);
        });
        
        btnRelatorios.addActionListener((ActionEvent e) -> {
            new RelatoriosPrestadores().setVisible(true);
        });
        
        btnAlertas.addActionListener((ActionEvent e) -> {
            new AlertasVencimento().setVisible(true);
        });
        
        btnSair.addActionListener((ActionEvent e) -> {
            dispose();
        });
        
        painelBotoes.add(btnCadastrarPrestador);
        painelBotoes.add(btnGestaoContratos);
        painelBotoes.add(btnRelatorios);
        painelBotoes.add(btnAlertas);
        painelBotoes.add(btnSair);
        
        add(painelTitulo, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new MenuPrestacaoServico().setVisible(true);
        });
    }
}