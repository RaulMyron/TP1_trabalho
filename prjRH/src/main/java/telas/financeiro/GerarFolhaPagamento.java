
package telas.financeiro;
import telas.financeiro.controller.ControllerManager;
import telas.financeiro.controller.FinanceiroController;
import telas.financeiro.model.FolhaPagamento;
import telas.financeiro.exception.FolhaPagamentoException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.YearMonth;

public class GerarFolhaPagamento extends javax.swing.JFrame {
    
    
    private FinanceiroController controller;
    private DefaultTableModel modeloTabela;
    private FolhaPagamento folhaAtual;
    
    public GerarFolhaPagamento() {
        initComponents();
        this.controller = ControllerManager.getFinanceiroController();
        configurarTela();
    }
    
    private void configurarTela() {
        setTitle("Gerar Folha de Pagamento");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        
        modeloTabela = (DefaultTableModel) tblFolha.getModel();
        
        
        LocalDate hoje = LocalDate.now();
        cmbMes.setSelectedIndex(hoje.getMonthValue() - 1); 
        cmbAno.setSelectedItem(String.valueOf(hoje.getYear()));
        
        
        btnExportar.setEnabled(false);
    }
    
    
    private void gerarFolha() {
        try {
                        
            int mes = cmbMes.getSelectedIndex() + 1; 
            int ano = Integer.parseInt((String) cmbAno.getSelectedItem());
            YearMonth mesReferencia = YearMonth.of(ano, mes);
            
            
            String mesNome = (String) cmbMes.getSelectedItem();
            int confirma = JOptionPane.showConfirmDialog(this,
                "Deseja gerar a folha de pagamento para " + mesNome + "/" + ano + "?",
                "Confirmar Geração",
                JOptionPane.YES_NO_OPTION);
            
            if (confirma != JOptionPane.YES_OPTION) {
                return;
            }
            
            
            folhaAtual = controller.gerarFolhaPagamento(mesReferencia);
            
             
        exibirFolhaNaTabela(folhaAtual);
            
            
            exibirFolhaNaTabela(folhaAtual);
            
            
            JOptionPane.showMessageDialog(this,
                "Folha de pagamento gerada com sucesso!\n" +
                "Total de funcionários: " + folhaAtual.getQuantidadeFuncionarios() +
                "\nTotal da folha: R$ " + String.format("%.2f", folhaAtual.getTotalFolha()),
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            
            
            btnExportar.setEnabled(true);
            
        } catch (FolhaPagamentoException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Erro ao gerar folha",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void exibirFolhaNaTabela(FolhaPagamento folha) {
        
        modeloTabela.setRowCount(0);
        
        
        for (FolhaPagamento.ItemFolha item : folha.getItens()) {
            Object[] linha = {
                item.getMatricula(),
                item.getNomeFuncionario(),
                item.getCargo(),
                item.getDepartamento(),
                item.getRegimeContratacao(),
                String.format("R$ %.2f", item.getSalarioBase()),
                String.format("R$ %.2f", item.getBeneficios()),
                String.format("R$ %.2f", item.getDescontos()),
                String.format("R$ %.2f", item.getSalarioLiquido())
            };
            modeloTabela.addRow(linha);
        }
        
        
        lblTotalFuncionarios.setText(String.valueOf(folha.getQuantidadeFuncionarios()));
        lblTotalFolha.setText(String.format("R$ %.2f", folha.getTotalFolha()));
    }
    
    
    private void exportarPDF() {
        if (folhaAtual == null) {
            JOptionPane.showMessageDialog(this,
                "Gere uma folha antes de exportar!",
                "Atenção",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        
        JOptionPane.showMessageDialog(this,
            "Funcionalidade de exportação em desenvolvimento!\n\n" +
            "A folha contém:\n" +
            "- " + folhaAtual.getQuantidadeFuncionarios() + " funcionários\n" +
            "- Total: R$ " + String.format("%.2f", folhaAtual.getTotalFolha()),
            "Exportar PDF",
            JOptionPane.INFORMATION_MESSAGE);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmbMes = new javax.swing.JComboBox<>();
        cmbAno = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFolha = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblTotalFuncionarios = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblTotalFolha = new javax.swing.JLabel();
        btnGerar = new javax.swing.JButton();
        btnExportar = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cmbMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" }));

        cmbAno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2024", "2025", "2026" }));

        tblFolha.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Matrícula", "Nome", "Cargo", "Departamento", "Regime", "Salário Base ", "Benefícios", "Descontos", "Salário Líquido   "
            }
        ));
        jScrollPane1.setViewportView(tblFolha);

        jLabel1.setText("Total de Funcionários:");

        lblTotalFuncionarios.setText("0");

        jLabel2.setText("Total da Folha:");

        lblTotalFolha.setText("R$ 0,00");

        btnGerar.setText("Gerar Folha");
        btnGerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarActionPerformed(evt);
            }
        });

        btnExportar.setText("Exportar PDF");
        btnExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarActionPerformed(evt);
            }
        });

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Gerar Folha de Pagamento");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblTotalFuncionarios, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblTotalFolha)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(294, 294, 294)
                        .addComponent(btnGerar)
                        .addGap(18, 18, 18)
                        .addComponent(btnExportar)
                        .addGap(18, 18, 18)
                        .addComponent(btnVoltar)
                        .addGap(0, 130, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(240, 240, 240)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(19, 19, 19)
                .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lblTotalFuncionarios))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(lblTotalFolha))
                        .addGap(0, 67, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnExportar)
                            .addComponent(btnGerar)
                            .addComponent(btnVoltar))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarActionPerformed
        // TODO add your handling code here:
        gerarFolha();
    }//GEN-LAST:event_btnGerarActionPerformed

    private void btnExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarActionPerformed
        // TODO add your handling code here:
        exportarPDF();
    }//GEN-LAST:event_btnExportarActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new GerarFolhaPagamento().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportar;
    private javax.swing.JButton btnGerar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JComboBox<String> cmbAno;
    private javax.swing.JComboBox<String> cmbMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalFolha;
    private javax.swing.JLabel lblTotalFuncionarios;
    private javax.swing.JTable tblFolha;
    // End of variables declaration//GEN-END:variables
}
