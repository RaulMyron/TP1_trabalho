
package telas.financeiro;
import telas.financeiro.controller.ControllerManager;
import telas.financeiro.controller.FinanceiroController;
import telas.financeiro.model.Funcionario;
import telas.financeiro.exception.FuncionarioException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ListagemFuncionarios extends javax.swing.JFrame {
    
    private FinanceiroController controller;
    private DefaultTableModel modeloTabela;
    
    public ListagemFuncionarios() {
        initComponents();
        this.controller = ControllerManager.getFinanceiroController();
        configurarTela();
        carregarFuncionarios();
    }
    
    private void configurarTela() {
        setTitle("Listagem de Funcionários");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        
        modeloTabela = (DefaultTableModel) tblFuncionarios.getModel();
    }
    
    
    private void carregarFuncionarios() {
        
        modeloTabela.setRowCount(0);
        
        
        List<Funcionario> funcionarios = controller.listarTodos();
        
        
        for (Funcionario func : funcionarios) {
            Object[] linha = {
                func.getMatricula(),
                func.getNome(),
                func.getCpf(),
                func.getCargo(),
                func.getDepartamento(),
                func.getRegimeContratacao(),
                String.format("R$ %.2f", func.getSalarioBase()),
                func.isAtivo() ? "Ativo" : "Inativo"
            };
            modeloTabela.addRow(linha);
        }
        
        
        setTitle("Listagem de Funcionários (" + funcionarios.size() + ")");
    }
    
    
    private void inativarSelecionado() {
        
        int linhaSelecionada = tblFuncionarios.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um funcionário na tabela!", 
                "Atenção", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        
        int confirma = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja inativar este funcionário?", 
            "Confirmar Inativação", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirma != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            
            String matricula = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
            
            
            controller.inativarFuncionario(matricula);
            
            JOptionPane.showMessageDialog(this, 
                "Funcionário inativado com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            
            carregarFuncionarios();
            
        } catch (FuncionarioException ex) {
            JOptionPane.showMessageDialog(this, 
                ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblFuncionarios = new javax.swing.JTable();
        btnAtualizar = new javax.swing.JButton();
        btnInativar = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblFuncionarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Matrícula", "Nome", "CPF", "Cargo", "Departamento", "Regime", "Salário Base", "Status"
            }
        ));
        jScrollPane1.setViewportView(tblFuncionarios);

        btnAtualizar.setText("Atualizar");
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        btnInativar.setText("Inativar Selecionado");
        btnInativar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInativarActionPerformed(evt);
            }
        });

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Lista de Funcionários");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(btnAtualizar)
                        .addGap(18, 18, 18)
                        .addComponent(btnInativar)
                        .addGap(18, 18, 18)
                        .addComponent(btnVoltar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(137, 137, 137)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAtualizar)
                    .addComponent(btnInativar)
                    .addComponent(btnVoltar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInativarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInativarActionPerformed
        // TODO add your handling code here:
        inativarSelecionado();
    }//GEN-LAST:event_btnInativarActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // TODO add your handling code here:
        carregarFuncionarios();
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new ListagemFuncionarios().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnInativar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblFuncionarios;
    // End of variables declaration//GEN-END:variables
}


