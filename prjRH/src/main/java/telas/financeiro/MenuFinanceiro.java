
package telas.financeiro;
import javax.swing.JOptionPane;
import telas.administracaoGestao.controller.GestaoService;
import telas.administracaoGestao.model.Usuario;
import telas.administracaoGestao.view.TelaPrincipal;

public class MenuFinanceiro extends javax.swing.JFrame {

    private Usuario usuarioLogado;
    private GestaoService gestaoService;

    public MenuFinanceiro(Usuario usuario, GestaoService service) {
        initComponents();
        this.usuarioLogado = usuario;
        this.gestaoService = service;
        configurarTela();
    }

    private void configurarTela() {
        setTitle("Menu Financeiro - Sistema de RH");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    
    

    private void abrirCadastroFuncionario() {
        CadastroFuncionario tela = new CadastroFuncionario();
        tela.setVisible(true);
        
    }


    private void abrirListagemFuncionarios() {
        ListagemFuncionarios tela = new ListagemFuncionarios();
        tela.setVisible(true);
        
    }


    private void abrirConfigurarRegras() {
        ConfigurarRegrasSalariais tela = new ConfigurarRegrasSalariais();
        tela.setVisible(true);
        
    }


    private void abrirGerarFolha() {
        GerarFolhaPagamento tela = new GerarFolhaPagamento();
        tela.setVisible(true);
        
    }


    private void abrirContracheques() {
        VisualizarContracheques tela = new VisualizarContracheques();
        tela.setVisible(true);
        
    }
    
    
    private void sair() {
        int confirma = JOptionPane.showConfirmDialog(this,
            "Deseja realmente sair do sistema?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION);
        
        if (confirma == JOptionPane.YES_OPTION) {
            TelaPrincipal telaPrincipal = new TelaPrincipal(this.usuarioLogado, this.gestaoService);
            telaPrincipal.setVisible(true);
            
            this.dispose();
        }
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnCadastrar = new javax.swing.JButton();
        btnListar = new javax.swing.JButton();
        btnConfigurar = new javax.swing.JButton();
        btnGerarFolha = new javax.swing.JButton();
        btnContracheques = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("MÓDULO FINANCEIRO - SISTEMA DE RH");

        btnCadastrar.setText("Cadastrar Funcionário");
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        btnListar.setText("Listar Funcionários");
        btnListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListarActionPerformed(evt);
            }
        });

        btnConfigurar.setText("Configurar Regras Salariais");
        btnConfigurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigurarActionPerformed(evt);
            }
        });

        btnGerarFolha.setText("Gerar Folha de Pagamento");
        btnGerarFolha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarFolhaActionPerformed(evt);
            }
        });

        btnContracheques.setText("Visualizar Contracheques");
        btnContracheques.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContrachequesActionPerformed(evt);
            }
        });

        btnSair.setText("Sair");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnCadastrar))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnListar))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnConfigurar))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnGerarFolha))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnContracheques))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnSair)))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(btnCadastrar)
                .addGap(18, 18, 18)
                .addComponent(btnListar)
                .addGap(18, 18, 18)
                .addComponent(btnConfigurar)
                .addGap(18, 18, 18)
                .addComponent(btnGerarFolha)
                .addGap(18, 18, 18)
                .addComponent(btnContracheques)
                .addGap(18, 18, 18)
                .addComponent(btnSair)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        // TODO add your handling code here:
        abrirCadastroFuncionario();
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void btnListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListarActionPerformed
        // TODO add your handling code here:
        abrirListagemFuncionarios();
    }//GEN-LAST:event_btnListarActionPerformed

    private void btnConfigurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigurarActionPerformed
        // TODO add your handling code here:
        abrirConfigurarRegras();
    }//GEN-LAST:event_btnConfigurarActionPerformed

    private void btnGerarFolhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarFolhaActionPerformed
        // TODO add your handling code here:
        abrirGerarFolha();
    }//GEN-LAST:event_btnGerarFolhaActionPerformed

    private void btnContrachequesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContrachequesActionPerformed
        // TODO add your handling code here:
        abrirContracheques();
    }//GEN-LAST:event_btnContrachequesActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        // TODO add your handling code here:
        sair();
    }//GEN-LAST:event_btnSairActionPerformed

   
    public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(() -> {
        telas.administracaoGestao.controller.GestaoService serviceTemp = 
            telas.administracaoGestao.controller.GestaoService.getInstance();
        
        try {
            telas.administracaoGestao.model.Usuario usuarioTemp = 
                serviceTemp.login("admin", "admin123".toCharArray());
            
            new MenuFinanceiro(usuarioTemp, serviceTemp).setVisible(true);
        } catch (Exception e) {
            System.out.println("Erro ao iniciar: " + e.getMessage());
        }
    });
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnConfigurar;
    private javax.swing.JButton btnContracheques;
    private javax.swing.JButton btnGerarFolha;
    private javax.swing.JButton btnListar;
    private javax.swing.JButton btnSair;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
