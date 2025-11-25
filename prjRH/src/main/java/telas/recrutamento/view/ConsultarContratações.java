package telas.recrutamento.view;

import telas.recrutamento.controller.ContratacaoController;
import telas.recrutamento.model.Contratacao;
import telas.recrutamento.model.Recrutador;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JFrame;
import telas.administracaoGestao.controller.GestaoService;
import telas.administracaoGestao.model.Vaga;
import telas.candidatura.Controller.CandidatoController;
import telas.candidatura.Model.Candidato;

public class ConsultarContratações extends javax.swing.JFrame {
    
    private JFrame menuPai;
    private ContratacaoController contratacaoController;
    private Recrutador recrutadorLogado;
    private GestaoService gestaoService;
    private CandidatoController candidatoController;
    
    public ConsultarContratações(JFrame menuPai, Recrutador recrutador) {
        initComponents();
        this.menuPai = menuPai;
        this.recrutadorLogado = recrutador;
        this.contratacaoController = new ContratacaoController();
        this.gestaoService = GestaoService.getInstance();
        this.candidatoController = new CandidatoController();
        
        setLocationRelativeTo(null);
        setTitle("Consultar Contratações");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        inicializarFiltros();
        carregarContratacoes();
    }

    public ConsultarContratações() {
        initComponents();
    }

    private void inicializarFiltros() {
        jComboBox4.removeAllItems();
        jComboBox4.addItem("Todas");
        for (Vaga v : this.gestaoService.listarTodasVagas()) {
            jComboBox4.addItem(v.getCargo());
        }
        
        jComboBox3.removeAllItems();
        jComboBox3.addItem("Todas");
        jComboBox3.addItem("Pendente");
        jComboBox3.addItem("Autorizada");
        jComboBox3.addItem("Negada");
        jComboBox3.addItem("Efetivada");
        
        jLabel10.setVisible(false);
        jTextField3.setVisible(false);
    }
    
    private void carregarContratacoes() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        String statusFiltro = jComboBox3.getSelectedItem().toString();
        String vagaFiltro = jComboBox4.getSelectedItem().toString();
        
        List<Contratacao> lista = contratacaoController.listarTodas(); 
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Contratacao c : lista) {
            if (!statusFiltro.equals("Todas") && !c.getStatusSolicitacao().equalsIgnoreCase(statusFiltro)) continue;

            // Busca nome do candidato pelo CPF (ID da candidatura)
            String nomeCandidato = c.getCandidaturaId();
            try {
                Candidato cand = candidatoController.buscarPorCpf(c.getCandidaturaId());
                if(cand != null) nomeCandidato = cand.getNome();
            } catch(Exception e) {}

            // Dados formatados
            String dataSolicitacao = c.getDataSolicitacao() != null ? sdf.format(c.getDataSolicitacao()) : "-";
            String dataResposta = c.getDataAutorizacao() != null ? sdf.format(c.getDataAutorizacao()) : "-";
            String gestor = c.getGestorAutorizador() != null ? c.getGestorAutorizador() : "-";
            
            model.addRow(new Object[]{
                c.getId(),
                nomeCandidato,
                c.getCandidaturaId(), // CPF
                "Vaga (Ver Detalhes)", // O modelo de Contratação idealmente teria a Vaga
                c.getRegimeContratacao(),
                "R$ " + c.getSalarioProposto(),
                dataSolicitacao,
                c.getStatusSolicitacao(),
                gestor,
                dataResposta
            });
        }
    }

    private void efetivarContratacao() {
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha!");
            return;
        }
        int id = (int) jTable1.getValueAt(row, 0);
        contratacaoController.efetivar(id);
        carregarContratacoes();
        JOptionPane.showMessageDialog(this, "Efetivado com sucesso!");
    }
    
    private void cancelarSolicitacao() {
        int row = jTable1.getSelectedRow();
        if (row == -1) return;
        int id = (int) jTable1.getValueAt(row, 0);
        contratacaoController.negar(id, "Cancelado", "Cancelado pelo usuario");
        carregarContratacoes();
        JOptionPane.showMessageDialog(this, "Cancelado!");
    }

    private void voltarMenu() {
        this.dispose();
        if (menuPai != null) menuPai.setVisible(true);
    }

    @SuppressWarnings("unchecked")
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Acompanhamento de Solicitações"));
        jLabel1.setText("Filtrar por:");
        jButton1.setText("Aplicar Filtros");
        jButton1.addActionListener(e -> carregarContratacoes());

        jLabel2.setText("Status:");
        jLabel9.setText("Vaga:");
        jLabel10.setText("Periodo");
        jButton4.setText("Limpar Filtros");
        jButton4.addActionListener(e -> {
            jComboBox3.setSelectedIndex(0);
            jComboBox4.setSelectedIndex(0);
            carregarContratacoes();
        });

        jTable1.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Candidato", "CPF", "Vaga", "Regime", "Salário", "Data Sol.", "Status", "Gestor", "Data Resp."
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton2.setText("Efetivar");
        jButton2.addActionListener(e -> efetivarContratacao());

        jButton3.setText("Cancelar");
        jButton3.addActionListener(e -> cancelarSolicitacao());

        jButton5.setText("Voltar");
        jButton5.addActionListener(e -> voltarMenu());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, 0, 100, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, 0, 100, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton4))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton5))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ConsultarContratações().setVisible(true));
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField3;
}