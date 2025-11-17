package telas.prestacaoservico.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import telas.administracaoGestao.controller.GestaoService;
import telas.administracaoGestao.model.Usuario;
import telas.administracaoGestao.model.Perfil;
import telas.administracaoGestao.view.TelaPrincipal;

public class MenuPrestacaoServico extends JFrame {

    private Usuario usuarioLogado;
    private GestaoService gestaoService;

    public MenuPrestacaoServico() {
        initComponents();
    }

    public MenuPrestacaoServico(Usuario usuario, GestaoService service) {
        this.usuarioLogado = usuario;
        this.gestaoService = service;
        initComponents();
        validarPermissoes();
    }

    private void validarPermissoes() {
        // Valida se o usuário tem permissão para acessar o módulo de Prestação de Serviço
        // Apenas ADMINISTRADOR e GESTOR devem ter acesso
        boolean isAdmin = usuarioLogado.getPerfis().contains(Perfil.ADMINISTRADOR);
        boolean isGestor = usuarioLogado.getPerfis().contains(Perfil.GESTOR);

        if (!isAdmin && !isGestor) {
            JOptionPane.showMessageDialog(this,
                "Você não tem permissão para acessar o Módulo de Prestação de Serviço.\n" +
                "Acesso permitido apenas para Administradores e Gestores.",
                "Acesso Negado",
                JOptionPane.ERROR_MESSAGE);
            this.dispose();
            new TelaPrincipal(usuarioLogado, gestaoService).setVisible(true);
        }
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
            this.dispose(); // Fecha esta tela para evitar múltiplas telas abertas
        });

        btnGestaoContratos.addActionListener((ActionEvent e) -> {
            new GestaoContratos().setVisible(true);
            this.dispose(); // Fecha esta tela para evitar múltiplas telas abertas
        });

        btnRelatorios.addActionListener((ActionEvent e) -> {
            new RelatoriosPrestadores().setVisible(true);
            this.dispose(); // Fecha esta tela para evitar múltiplas telas abertas
        });

        btnAlertas.addActionListener((ActionEvent e) -> {
            new AlertasVencimento().setVisible(true);
            this.dispose(); // Fecha esta tela para evitar múltiplas telas abertas
        });

        btnSair.addActionListener((ActionEvent e) -> {
            // Volta para a Tela Principal se usuário estiver logado
            if (usuarioLogado != null && gestaoService != null) {
                new TelaPrincipal(usuarioLogado, gestaoService).setVisible(true);
            }
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