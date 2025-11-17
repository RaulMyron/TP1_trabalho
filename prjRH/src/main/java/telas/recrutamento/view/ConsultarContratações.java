/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package telas.recrutamento.view;
import telas.recrutamento.controller.ContratacaoController;
import telas.recrutamento.model.Contratacao;
import telas.recrutamento.model.Recrutador;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;
import telas.administracaoGestao.controller.GestaoService;
import telas.administracaoGestao.model.Vaga;

public class ConsultarContratações extends javax.swing.JFrame {
    
    private Main menuPai;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ConsultarContratações.class.getName());
    private ContratacaoController contratacaoController;
    private Recrutador recrutadorLogado;
    private Contratacao contratacaoSelecionada;
    private GestaoService gestaoService;
    
    public ConsultarContratações(Recrutador recrutador) {
        initComponents();
        this.recrutadorLogado = recrutador;
        this.contratacaoController = new ContratacaoController();
        this.gestaoService = GestaoService.getInstance();
        setLocationRelativeTo(null);
        setTitle("Consultar Contratações");
        configurarEventos();
        inicializarFiltros();
        carregarContratacoes();
    }
    private void configurarEventos() {
        jButton1.addActionListener(e -> aplicarFiltros());
        jButton4.addActionListener(e -> limparFiltros());
        jButton2.addActionListener(e -> efetivarContratacao());
        jButton3.addActionListener(e -> cancelarSolicitacao());
        jButton5.addActionListener(e -> voltarMenu());
        
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarDetalhesContratacao();
            }
        });
    }
    
    private void inicializarFiltros() {
        // ComboBox Status
        jComboBox3.removeAllItems();
        jComboBox3.addItem("Todas");
        jComboBox3.addItem("Pendente");
        jComboBox3.addItem("Autorizada");
        jComboBox3.addItem("Negada");
        jComboBox3.addItem("Efetivada");
        
        // ComboBox Vagas
        jComboBox4.removeAllItems();
        jComboBox4.addItem("Todas");
        List<Vaga> vagas = this.gestaoService.listarTodasVagas();
        for (Vaga v : vagas) {
            jComboBox4.addItem(v.getCargo());
        }
        
        // Combo Regime (parte de baixo)
        jComboBox2.removeAllItems();
        jComboBox2.addItem("CLT");
        jComboBox2.addItem("PJ");
        jComboBox2.addItem("Estágio");
        
        // Combo Vaga (parte de baixo)
        jComboBox1.removeAllItems();
        jComboBox1.addItem("Selecione");
        for (Vaga v : vagas) {
            jComboBox1.addItem(v.getCargo());
        }
        
        // Formato de período
        jTextField3.setText("dd/MM/yyyy - dd/MM/yyyy");
    }
    
    private void carregarContratacoes() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        List<Contratacao> contratacoes = contratacaoController.listarPorRecrutador(recrutadorLogado.getCpf());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Contratacao c : contratacoes) {
            String dataSolicitacao = c.getDataSolicitacao() != null ? sdf.format(c.getDataSolicitacao()) : "-";
            String dataResposta = c.getDataAutorizacao() != null ? sdf.format(c.getDataAutorizacao()) : "-";
            String gestor = c.getGestorAutorizador() != null ? c.getGestorAutorizador() : "-";
            
            model.addRow(new Object[]{
                c.getId(),
                "Candidato " + c.getCandidaturaId(),
                c.getCandidaturaId(),
                "Vaga X",
                c.getRegimeContratacao(),
                dataSolicitacao,
                c.getStatusSolicitacao(),
                gestor,
                dataResposta
            });
        }
        
        JOptionPane.showMessageDialog(this, 
            contratacoes.size() + " contratações encontradas!");
    }
    
    private void aplicarFiltros() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        String statusFiltro = (String) jComboBox3.getSelectedItem();
        String vagaFiltro = (String) jComboBox4.getSelectedItem();
        
        List<Contratacao> contratacoes = contratacaoController.listarPorRecrutador(recrutadorLogado.getCpf());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        int count = 0;
        for (Contratacao c : contratacoes) {
            // Aplicar filtro de status
            if (!statusFiltro.equals("Todas") && !c.getStatusSolicitacao().equals(statusFiltro)) {
                continue;
            }
            
            count++;
            String dataSolicitacao = c.getDataSolicitacao() != null ? sdf.format(c.getDataSolicitacao()) : "-";
            String dataResposta = c.getDataAutorizacao() != null ? sdf.format(c.getDataAutorizacao()) : "-";
            String gestor = c.getGestorAutorizador() != null ? c.getGestorAutorizador() : "-";
            
            model.addRow(new Object[]{
                c.getId(),
                "Candidato " + c.getCandidaturaId(),
                c.getCandidaturaId(),
                "Vaga X",
                c.getRegimeContratacao(),
                dataSolicitacao,
                c.getStatusSolicitacao(),
                gestor,
                dataResposta
            });
        }
        
        JOptionPane.showMessageDialog(this, 
            "Filtros aplicados! " + count + " contratações encontradas.");
    }
    
    private void limparFiltros() {
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        jTextField3.setText("dd/MM/yyyy - dd/MM/yyyy");
        carregarContratacoes();
    }
    
    private void carregarDetalhesContratacao() {
        int row = jTable1.getSelectedRow();
        if (row < 0) return;
        
        int id = (int) jTable1.getValueAt(row, 0);
        contratacaoSelecionada = contratacaoController.buscar(id);
        
        if (contratacaoSelecionada != null) {
            jTextField4.setText(String.valueOf(contratacaoSelecionada.getSalarioProposto()));
            jComboBox2.setSelectedItem(contratacaoSelecionada.getRegimeContratacao());
            
            if (contratacaoSelecionada.getDataInicioProposta() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                jTextField2.setText(sdf.format(contratacaoSelecionada.getDataInicioProposta()));
            }
        }
    }
    
    private void efetivarContratacao() {
        if (contratacaoSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma contratação da tabela!");
            return;
        }
        
        if (!contratacaoSelecionada.isAutorizada()) {
            JOptionPane.showMessageDialog(this, 
                "Apenas contratações AUTORIZADAS podem ser efetivadas!\n" +
                "Status atual: " + contratacaoSelecionada.getStatusSolicitacao());
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja efetivar a contratação #" + contratacaoSelecionada.getId() + "?\n\n" +
            "Candidato: " + contratacaoSelecionada.getCandidaturaId() + "\n" +
            "Salário: R$ " + String.format("%.2f", contratacaoSelecionada.getSalarioProposto()) + "\n" +
            "Regime: " + contratacaoSelecionada.getRegimeContratacao(),
            "Confirmar Efetivação",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            contratacaoController.efetivar(contratacaoSelecionada.getId());
            carregarContratacoes();
            limparSelecao();
        }
    }
    
    private void cancelarSolicitacao() {
        if (contratacaoSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma contratação da tabela!");
            return;
        }
        
        if (!contratacaoSelecionada.isPendente()) {
            JOptionPane.showMessageDialog(this, 
                "Apenas contratações PENDENTES podem ser canceladas!\n" +
                "Status atual: " + contratacaoSelecionada.getStatusSolicitacao());
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja cancelar a solicitação #" + contratacaoSelecionada.getId() + "?",
            "Confirmar Cancelamento",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            contratacaoController.negar(contratacaoSelecionada.getId(), 
                recrutadorLogado.getNome(), "Cancelado pelo recrutador");
            carregarContratacoes();
            limparSelecao();
        }
    }
    
    private void limparSelecao() {
        jTable1.clearSelection();
        jTextField4.setText("");
        jTextField2.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        contratacaoSelecionada = null;
    }
    
    private void voltarMenu() {
        Main menu = new Main();
        menu.carregarRecrutador(recrutadorLogado.getCpf());
        menu.setVisible(true);
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField4 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Acompanhamento de Solicitações de Contratação"));

        jLabel1.setText("Filtrar por:");

        jButton1.setText("Aplicar Filtros");

        jLabel2.setText("Status da Solicitação:");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "" }));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "" }));

        jLabel9.setText("Por Vaga");

        jLabel10.setText("Periodo");

        jButton4.setText("Limpar Filtros");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome Candidato", "CPF", "Vaga ", "Regime Proposto ", "Data Solicitação ", "Status ", "Gestor Responsável", "Data Resposta"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton2.setText("Efetivar Contratação");

        jButton3.setText("Cancelar Solicitação");

        jButton5.setText("Voltar");

        jLabel3.setText("Salário");

        jLabel4.setText("Regime Trabalhista");

        jLabel5.setText("Vaga");

        jLabel6.setText("Data de Inicio");

        jTextField2.setText("jTextField1");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(128, 128, 128))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(40, 40, 40)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField4)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(147, 147, 147)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jButton5)
                            .addComponent(jButton2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addGap(450, 450, 450)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            Recrutador recrutadorTeste = new Recrutador("João Teste", "12345678900", "teste@empresa.com", "12345678900", "senha123");
            new ConsultarContratações(recrutadorTeste).setVisible(true);
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
