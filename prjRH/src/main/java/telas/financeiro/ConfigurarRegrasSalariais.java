
package telas.financeiro;
import telas.financeiro.controller.ControllerManager;
import telas.financeiro.controller.FinanceiroController;
import telas.financeiro.model.RegraSalario;
import javax.swing.JOptionPane;


public class ConfigurarRegrasSalariais extends javax.swing.JFrame {
    
    
    private FinanceiroController controller;
    
   
    public ConfigurarRegrasSalariais() {
        initComponents(); 
        this.controller = ControllerManager.getFinanceiroController();
        configurarTela();
        carregarValoresAtuais(); 
    }
    
    
    private void configurarTela() {
        setTitle("Configurar Regras Salariais");
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
    }
    
    
    private void carregarValoresAtuais() {
        
        RegraSalario regra = controller.getRegraSalarialAtual();
        
        
        txtPercentualINSS.setText(String.valueOf(regra.getPercentualINSS()));
        txtPercentualIRRF.setText(String.valueOf(regra.getPercentualIRRF()));
        txtValeTransporte.setText(String.valueOf(regra.getValorValeTransporte()));
        txtValeAlimentacao.setText(String.valueOf(regra.getValorValeAlimentacao()));
        txtAuxilioEstagio.setText(String.valueOf(regra.getValorAuxilioTransporteEstagio()));
    }
    
    
    private void salvarConfiguracoes() {
        try {
            
            if (camposVazios()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, preencha todos os campos!", 
                    "Atenção", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            
            double percentualINSS = Double.parseDouble(txtPercentualINSS.getText().trim());
            double percentualIRRF = Double.parseDouble(txtPercentualIRRF.getText().trim());
            double valeTransporte = Double.parseDouble(txtValeTransporte.getText().trim());
            double valeAlimentacao = Double.parseDouble(txtValeAlimentacao.getText().trim());
            double auxilioEstagio = Double.parseDouble(txtAuxilioEstagio.getText().trim());
            
            
            if (percentualINSS < 0 || percentualIRRF < 0 || 
                valeTransporte < 0 || valeAlimentacao < 0 || auxilioEstagio < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Os valores não podem ser negativos!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            
            if (percentualINSS > 100 || percentualIRRF > 100) {
                JOptionPane.showMessageDialog(this, 
                    "Os percentuais não podem ser maiores que 100%!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            
            RegraSalario novaRegra = new RegraSalario(
                percentualINSS,
                percentualIRRF,
                valeTransporte,
                valeAlimentacao,
                auxilioEstagio
            );
            
            
            int confirma = JOptionPane.showConfirmDialog(this,
                "Atenção! Esta alteração afetará o cálculo de TODOS os salários.\n" +
                "Deseja realmente salvar estas configurações?",
                "Confirmar Alteração",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirma != JOptionPane.YES_OPTION) {
                return; 
            }
            
            
            controller.atualizarRegraSalarial(novaRegra);
            
            
            JOptionPane.showMessageDialog(this,
                "Configurações salvas com sucesso!\n" +
                "Os novos valores serão aplicados no próximo cálculo de folha.",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
           
            JOptionPane.showMessageDialog(this,
                "Valores inválidos! Use apenas números.\n" +
                "Para decimais, use ponto (ex: 11.5)",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private boolean camposVazios() {
        return txtPercentualINSS.getText().trim().isEmpty() ||
               txtPercentualIRRF.getText().trim().isEmpty() ||
               txtValeTransporte.getText().trim().isEmpty() ||
               txtValeAlimentacao.getText().trim().isEmpty() ||
               txtAuxilioEstagio.getText().trim().isEmpty();
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtPercentualINSS = new javax.swing.JTextField();
        txtPercentualIRRF = new javax.swing.JTextField();
        txtValeTransporte = new javax.swing.JTextField();
        txtValeAlimentacao = new javax.swing.JTextField();
        txtAuxilioEstagio = new javax.swing.JTextField();
        btnSalvar = new javax.swing.JButton();
        btnCarregar = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtPercentualINSS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPercentualINSSActionPerformed(evt);
            }
        });

        txtValeAlimentacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValeAlimentacaoActionPerformed(evt);
            }
        });

        btnSalvar.setText("Salvar Configurações");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnCarregar.setText("Carregar Valores Atuais");
        btnCarregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCarregarActionPerformed(evt);
            }
        });

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Configurar Regras Salariais");

        jLabel2.setText("Percentual IRRF (%):");

        jLabel3.setText("Percentual INSS (%):");

        jLabel4.setText("Valor Vale Transporte (R$):");

        jLabel5.setText("Valor Vale Alimentação (R$):");

        jLabel6.setText("Auxílio Transporte Estágio (R$):");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPercentualINSS, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPercentualIRRF, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtValeTransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValeAlimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(127, 127, 127)
                                .addComponent(btnSalvar))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAuxilioEstagio, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(btnCarregar)
                        .addGap(18, 18, 18)
                        .addComponent(btnVoltar)))
                .addContainerGap(9, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(150, 150, 150))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPercentualINSS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPercentualIRRF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValeTransporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValeAlimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAuxilioEstagio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar)
                    .addComponent(btnCarregar)
                    .addComponent(btnVoltar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        // TODO add your handling code here:
        salvarConfiguracoes();
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCarregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCarregarActionPerformed
        // TODO add your handling code here:
        carregarValoresAtuais();
    JOptionPane.showMessageDialog(this,
        "Valores atuais carregados!",
        "Informação",
        JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnCarregarActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void txtPercentualINSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPercentualINSSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPercentualINSSActionPerformed

    private void txtValeAlimentacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValeAlimentacaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValeAlimentacaoActionPerformed

    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new ConfigurarRegrasSalariais().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCarregar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtAuxilioEstagio;
    private javax.swing.JTextField txtPercentualINSS;
    private javax.swing.JTextField txtPercentualIRRF;
    private javax.swing.JTextField txtValeAlimentacao;
    private javax.swing.JTextField txtValeTransporte;
    // End of variables declaration//GEN-END:variables
}
