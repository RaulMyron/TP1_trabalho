package telas.recrutamento.view;

import javax.swing.JOptionPane;
import telas.administracaoGestao.model.Perfil;
import telas.recrutamento.controller.ContratacaoController;
import telas.recrutamento.model.Contratacao;
import telas.administracaoGestao.model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class AutorizarContratacoes extends javax.swing.JFrame {
    
    private ContratacaoController contratacaoController;
    private Usuario gestorLogado;
    private Contratacao contratacaoSelecionada;
    
    public AutorizarContratacoes(Usuario gestor) {
        initComponents();
        this.gestorLogado = gestor;
        this.contratacaoController = new ContratacaoController();
        setLocationRelativeTo(null);
        setTitle("Autorizar Contratações - Gestor");
        configurarEventos();
        carregarSolicitacoesPendentes();
    }
    
    private void configurarEventos() {
        btnAutorizar.addActionListener(e -> autorizarContratacao());
        btnNegar.addActionListener(e -> negarContratacao());
        btnAtualizar.addActionListener(e -> carregarSolicitacoesPendentes());
        btnVoltar.addActionListener(e -> voltarMenu());
        
        tblSolicitacoes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarDetalhes();
            }
        });
    }
    

    private void carregarSolicitacoesPendentes() {
    DefaultTableModel model = (DefaultTableModel) tblSolicitacoes.getModel();
    model.setRowCount(0);
    
    List<Contratacao> pendentes = contratacaoController.listarPendentes();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    telas.candidatura.Controller.CandidatoController candidatoController = 
        new telas.candidatura.Controller.CandidatoController();
    
    for (Contratacao c : pendentes) {
        String dataSolicitacao = c.getDataSolicitacao() != null ? 
            sdf.format(c.getDataSolicitacao()) : "-";
        String dataInicio = c.getDataInicioProposta() != null ? 
            sdf.format(c.getDataInicioProposta()) : "-";
        
        String nomeCandidato = c.getCandidaturaId(); // Default: mostra CPF
        String cpfCandidato = c.getCandidaturaId();
        
        try {
            telas.candidatura.Model.Candidato candidato = 
                candidatoController.buscarPorCpf(cpfCandidato);
            
            if (candidato != null) {
                nomeCandidato = candidato.getNome() + " (" + cpfCandidato + ")";
            }
        } catch (Exception e) {
            // Se não encontrar, mantém o CPF
            System.out.println("Candidato não encontrado: " + cpfCandidato);
        }
        
        String nomeRecrutador = c.getRecrutadorSolicitante().getNome();
        if (nomeRecrutador == null || nomeRecrutador.isEmpty()) {
            // Se nome não existe, tenta buscar do GestaoService
            try {
                telas.administracaoGestao.controller.GestaoService gestao = 
                    telas.administracaoGestao.controller.GestaoService.getInstance();
                telas.administracaoGestao.model.Usuario usuario = 
                    gestao.buscarUsuario(c.getRecrutadorSolicitante().getCpf());
                if (usuario != null) {
                    nomeRecrutador = usuario.getNome();
                }
            } catch (Exception e) {
                nomeRecrutador = "Recrutador " + c.getRecrutadorSolicitante().getCpf();
            }
        }

        model.addRow(new Object[]{
            c.getId(),
            nomeCandidato,
            nomeRecrutador,
            c.getRegimeContratacao(),
            String.format("R$ %.2f", c.getSalarioProposto()),
            dataSolicitacao,
            dataInicio
        });
    }
    
    JOptionPane.showMessageDialog(this, 
        pendentes.size() + " solicitações pendentes de autorização!");
    }
    
    private void carregarDetalhes() {
        int row = tblSolicitacoes.getSelectedRow();
        if (row < 0) return;
        
        int id = (int) tblSolicitacoes.getValueAt(row, 0);
        contratacaoSelecionada = contratacaoController.buscar(id);
        
        if (contratacaoSelecionada != null) {
            txtJustificativa.setText(contratacaoSelecionada.getJustificativa());
            txtParecer.setText("");
            txtParecer.setEnabled(true);
        }
    }
    
    private void autorizarContratacao() {
        if (contratacaoSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma solicitação!");
            return;
        }
        
        String parecer = txtParecer.getText();
        if (parecer.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Digite um parecer sobre a contratação!");
            return;
        }
        
        String nomeCandidatoDialog = contratacaoSelecionada.getCandidaturaId();
        try {
            telas.candidatura.Controller.CandidatoController candidatoController = 
                new telas.candidatura.Controller.CandidatoController();
            telas.candidatura.Model.Candidato candidato = 
                candidatoController.buscarPorCpf(contratacaoSelecionada.getCandidaturaId());
            if (candidato != null) {
                nomeCandidatoDialog = candidato.getNome() + " (CPF: " + candidato.getCpf() + ")";
            }
        } catch (Exception e) {
            // Mantém o CPF se não encontrar
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja AUTORIZAR a contratação #" + contratacaoSelecionada.getId() + "?\n\n" +
            "Candidato: " + nomeCandidatoDialog + "\n" +
            "Salário: R$ " + String.format("%.2f", contratacaoSelecionada.getSalarioProposto()) + "\n" +
            "Regime: " + contratacaoSelecionada.getRegimeContratacao(),
            "Confirmar Autorização",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            contratacaoController.autorizar(
                contratacaoSelecionada.getId(), 
                gestorLogado.getNome(), 
                parecer
            );
            limparSelecao();
            carregarSolicitacoesPendentes();
        }
    }
    
    private void negarContratacao() {
        if (contratacaoSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma solicitação!");
            return;
        }

        String parecer = txtParecer.getText();
        if (parecer.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Digite um parecer justificando a negativa!");
            return;
        }

        String nomeCandidatoDialog = contratacaoSelecionada.getCandidaturaId();
        try {
            telas.candidatura.Controller.CandidatoController candidatoController = 
                new telas.candidatura.Controller.CandidatoController();
            telas.candidatura.Model.Candidato candidato = 
                candidatoController.buscarPorCpf(contratacaoSelecionada.getCandidaturaId());
            if (candidato != null) {
                nomeCandidatoDialog = candidato.getNome() + " (CPF: " + candidato.getCpf() + ")";
            }
        } catch (Exception e) {
            // Mantém o CPF se não encontrar
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja NEGAR a contratação #" + contratacaoSelecionada.getId() + "?\n\n" +
            "Candidato: " + nomeCandidatoDialog + "\n" +
            "Esta ação não pode ser desfeita!",
            "Confirmar Negativa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            contratacaoController.negar(
                contratacaoSelecionada.getId(), 
                gestorLogado.getNome(), 
                parecer
            );
            limparSelecao();
            carregarSolicitacoesPendentes();
        }
    }
    
    private void limparSelecao() {
        tblSolicitacoes.clearSelection();
        txtJustificativa.setText("");
        txtParecer.setText("");
        contratacaoSelecionada = null;
    }
    
    private void voltarMenu() {
        // Volta para a TelaPrincipal
        telas.administracaoGestao.view.TelaPrincipal tela = 
            new telas.administracaoGestao.view.TelaPrincipal(
                gestorLogado, 
                telas.administracaoGestao.controller.GestaoService.getInstance()
            );
        tela.setVisible(true);
        this.dispose();
    }
    
    @SuppressWarnings("unchecked")
    private void initComponents() {
        JPanel painelPrincipal = new JPanel();
        JLabel lblTitulo = new JLabel();
        JScrollPane scrollTabela = new JScrollPane();
        tblSolicitacoes = new JTable();
        JPanel painelDetalhes = new JPanel();
        JLabel lblJustificativa = new JLabel();
        JScrollPane scrollJust = new JScrollPane();
        txtJustificativa = new JTextArea();
        JLabel lblParecer = new JLabel();
        JScrollPane scrollParecer = new JScrollPane();
        txtParecer = new JTextArea();
        JPanel painelBotoes = new JPanel();
        btnAutorizar = new JButton();
        btnNegar = new JButton();
        btnAtualizar = new JButton();
        btnVoltar = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        painelPrincipal.setBorder(BorderFactory.createTitledBorder("Autorização de Contratações"));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lblTitulo.setText("Solicitações Pendentes de Aprovação");

        tblSolicitacoes.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Candidato", "Recrutador", "Regime", "Salário", "Data Solicitação", "Início Proposto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollTabela.setViewportView(tblSolicitacoes);

        painelDetalhes.setBorder(BorderFactory.createTitledBorder("Detalhes da Solicitação"));

        lblJustificativa.setText("Justificativa do Recrutador:");
        txtJustificativa.setEditable(false);
        txtJustificativa.setRows(3);
        scrollJust.setViewportView(txtJustificativa);

        lblParecer.setText("Seu Parecer (obrigatório):");
        txtParecer.setRows(3);
        scrollParecer.setViewportView(txtParecer);

        GroupLayout painelDetalhesLayout = new GroupLayout(painelDetalhes);
        painelDetalhes.setLayout(painelDetalhesLayout);
        painelDetalhesLayout.setHorizontalGroup(
            painelDetalhesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(painelDetalhesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelDetalhesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(scrollJust)
                    .addComponent(scrollParecer)
                    .addGroup(painelDetalhesLayout.createSequentialGroup()
                        .addGroup(painelDetalhesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(lblJustificativa)
                            .addComponent(lblParecer))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        painelDetalhesLayout.setVerticalGroup(
            painelDetalhesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(painelDetalhesLayout.createSequentialGroup()
                .addComponent(lblJustificativa)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollJust, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblParecer)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollParecer, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAutorizar.setBackground(new java.awt.Color(46, 125, 50));
        btnAutorizar.setForeground(new java.awt.Color(255, 255, 255));
        btnAutorizar.setText("Autorizar");
        btnNegar.setBackground(new java.awt.Color(198, 40, 40));
        btnNegar.setForeground(new java.awt.Color(255, 255, 255));
        btnNegar.setText("Negar");
        btnAtualizar.setText("Atualizar");
        btnVoltar.setText("Voltar");

        GroupLayout painelBotoesLayout = new GroupLayout(painelBotoes);
        painelBotoes.setLayout(painelBotoesLayout);
        painelBotoesLayout.setHorizontalGroup(
            painelBotoesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(painelBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnVoltar, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAtualizar, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNegar, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAutorizar, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        painelBotoesLayout.setVerticalGroup(
            painelBotoesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(painelBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelBotoesLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAutorizar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNegar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAtualizar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVoltar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout painelPrincipalLayout = new GroupLayout(painelPrincipal);
        painelPrincipal.setLayout(painelPrincipalLayout);
        painelPrincipalLayout.setHorizontalGroup(
            painelPrincipalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(painelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelPrincipalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(scrollTabela)
                    .addComponent(painelDetalhes, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(painelBotoes, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(painelPrincipalLayout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        painelPrincipalLayout.setVerticalGroup(
            painelPrincipalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(painelPrincipalLayout.createSequentialGroup()
                .addComponent(lblTitulo)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTabela, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(painelDetalhes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(painelBotoes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(painelPrincipal, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(painelPrincipal, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }
    
    private JTable tblSolicitacoes;
    private JTextArea txtJustificativa;
    private JTextArea txtParecer;
    private JButton btnAutorizar;
    private JButton btnNegar;
    private JButton btnAtualizar;
    private JButton btnVoltar;
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            // Para teste
            Usuario gestorTeste = new telas.administracaoGestao.model.Gestor(
                "Gestor Teste", "99988877766", "gestor@empresa.com", "gestor", "123"
            );
            new AutorizarContratacoes(gestorTeste).setVisible(true);
        });
    }
}