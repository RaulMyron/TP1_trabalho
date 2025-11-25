/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package telas.recrutamento.view;

import telas.recrutamento.controller.ContratacaoController;
import telas.recrutamento.model.Contratacao;
import telas.recrutamento.model.Recrutador;
import telas.candidatura.Controller.CandidatoController;
import telas.candidatura.Model.Candidatura;
import telas.administracaoGestao.controller.GestaoService;
import telas.administracaoGestao.model.Vaga;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JFrame;

public class ConsultarContratações extends javax.swing.JFrame {
    
    private JFrame menuPai;
    private ContratacaoController contratacaoController;
    private CandidatoController candidatoController;
    private Recrutador recrutadorLogado;
    private GestaoService gestaoService;
    
    public ConsultarContratações(JFrame menuPai, Recrutador recrutador) {
        initComponents();
        this.menuPai = menuPai;
        this.recrutadorLogado = recrutador;
        
        this.contratacaoController = new ContratacaoController();
        this.candidatoController = new CandidatoController();
        this.gestaoService = GestaoService.getInstance();
        
        setLocationRelativeTo(null);
        setTitle("Consultar Contratações");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        configurarComponentesIniciais();
        carregarContratacoes(); 
    }

    public ConsultarContratações() {
        initComponents();
    }

    private void configurarComponentesIniciais() {
        jComboBox4.removeAllItems();
        jComboBox4.addItem("Todas");
        if (this.gestaoService != null) {
            for (Vaga v : this.gestaoService.listarTodasVagas()) {
                jComboBox4.addItem(v.getCargo());
            }
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
        filtrarDados("Todas", "Todas");
    }
    
    private void filtrarDados(String statusFiltro, String vagaFiltro) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        List<Contratacao> listaContratacoes = contratacaoController.listarTodas(); 
        List<Candidatura> listaCandidaturas = candidatoController.getListaCandidaturas();

        if (listaContratacoes == null) return;

        for (Contratacao c : listaContratacoes) {
            
            String nomeVaga = "Vaga não identificada";
            String nomeCandidato = c.getCandidaturaId(); 

            if (listaCandidaturas != null) {
                for (Candidatura cand : listaCandidaturas) {
                    String idSalvo = c.getCandidaturaId();
                    if (cand.getCandidato() != null && (
                        cand.getCandidato().getCpf().equals(idSalvo) || 
                        cand.getCandidato().getNome().equalsIgnoreCase(idSalvo)
                    )) {
                        if (cand.getVaga() != null) {
                            nomeVaga = cand.getVaga().getCargo();
                        }
                        nomeCandidato = cand.getCandidato().getNome();
                        break;
                    }
                }
            }
            
            boolean passaStatus = statusFiltro.equals("Todas") || 
                                  (c.getStatusSolicitacao() != null && c.getStatusSolicitacao().equalsIgnoreCase(statusFiltro));
            
            boolean passaVaga = vagaFiltro.equals("Todas") || nomeVaga.equalsIgnoreCase(vagaFiltro);

            if (passaStatus && passaVaga) {
                
                String dataSolicitacao = c.getDataSolicitacao() != null ? sdf.format(c.getDataSolicitacao()) : "-";
                String dataResposta = c.getDataAutorizacao() != null ? sdf.format(c.getDataAutorizacao()) : "-";
                String gestor = c.getGestorAutorizador() != null ? c.getGestorAutorizador() : "-";
                
                model.addRow(new Object[]{
                    c.getId(),
                    nomeCandidato,
                    c.getCandidaturaId(),
                    nomeVaga,            
                    c.getRegimeContratacao(),
                    "R$ " + c.getSalarioProposto(),
                    dataSolicitacao,
                    c.getStatusSolicitacao(),
                    gestor,
                    dataResposta
                });
            }
        }
    }
    
    private void aplicarFiltros() {
        String status = jComboBox3.getSelectedItem() != null ? jComboBox3.getSelectedItem().toString() : "Todas";
        String vaga = jComboBox4.getSelectedItem() != null ? jComboBox4.getSelectedItem().toString() : "Todas";
        filtrarDados(status, vaga);
        JOptionPane.showMessageDialog(this, "Filtros aplicados.");
    }
    
    private void limparFiltros() {
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        carregarContratacoes();
    }
    
    private void efetivarContratacao() {
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha na tabela.");
            return;
        }
        
        int id = Integer.parseInt(jTable1.getValueAt(row, 0).toString());
        String statusAtual = jTable1.getValueAt(row, 7).toString();
        
        if (!statusAtual.equalsIgnoreCase("Autorizada")) {
            JOptionPane.showMessageDialog(this, "Apenas contratações AUTORIZADAS podem ser efetivadas.");
            return;
        }
        
        if (JOptionPane.showConfirmDialog(this, "Efetivar contratação #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            contratacaoController.efetivar(id);
            carregarContratacoes();
            JOptionPane.showMessageDialog(this, "Contratação efetivada!");
        }
    }
    
    private void cancelarSolicitacao() {
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha na tabela.");
            return;
        }
        
        int id = Integer.parseInt(jTable1.getValueAt(row, 0).toString());
        String statusAtual = jTable1.getValueAt(row, 7).toString();
        
        if (!statusAtual.equalsIgnoreCase("Pendente")) {
            JOptionPane.showMessageDialog(this, "Apenas solicitações PENDENTES podem ser canceladas.");
            return;
        }
        
        if (JOptionPane.showConfirmDialog(this, "Cancelar solicitação #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            contratacaoController.negar(id, "Cancelado pelo Recrutador", "Cancelamento Manual");
            carregarContratacoes();
            JOptionPane.showMessageDialog(this, "Solicitação cancelada.");
        }
    }
    
    private void voltarMenu() {
        this.dispose();
        if (menuPai != null) {
            menuPai.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aplicarFiltros();
            }
        });

        jLabel2.setText("Status:");

        jLabel9.setText("Vaga:");

        jLabel10.setText("Periodo");

        jButton4.setText("Limpar Filtros");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limparFiltros();
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Candidato", "CPF", "Vaga", "Regime", "Salário", "Data Sol.", "Status", "Gestor", "Data Resp."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton2.setText("Efetivar Contratação");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                efetivarContratacao();
            }
        });

        jButton3.setText("Cancelar Solicitação");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarSolicitacao();
            }
        });

        jButton5.setText("Voltar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voltarMenu();
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)))
                        .addGap(0, 10, Short.MAX_VALUE))
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
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    }// </editor-fold>                        

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ConsultarContratações().setVisible(true));
    }

    // Variables declaration - do not modify                     
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
    // End of variables declaration                   
}