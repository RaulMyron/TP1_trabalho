/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package telas.recrutamento.view;

import telas.recrutamento.controller.RecrutadorController;
import telas.recrutamento.model.Recrutador;
import javax.swing.JOptionPane;
import telas.administracaoGestao.controller.GestaoService;
import telas.administracaoGestao.model.Vaga;
import telas.administracaoGestao.view.TelaLogin;

public class Main extends javax.swing.JFrame {
    
    private Main menuPai;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Main.class.getName());
    private RecrutadorController recrutadorController;
    private Recrutador recrutadorLogado;
    private GestaoService gestaoService;

    public Main() {
        initComponents();
        recrutadorController = new RecrutadorController();
        setLocationRelativeTo(null);
        configurarEventos();
    }


    private void configurarEventos() {
        jButton1.addActionListener(e -> sair());
        jButton2.addActionListener(e -> abrirGerenciarCandidaturas());
        jButton3.addActionListener(e -> abrirMarcarEntrevistas());
        jButton4.addActionListener(e -> abrirConsultarContratacoes());
        jButton5.addActionListener(e -> abrirSolicitarContratacoes());
    }

    public void carregarRecrutador(String cpf) {
            System.out.println("Tentando carregar recrutador com CPF: " + cpf);

            // 1. Tenta buscar no banco de dados central (GestaoService)
            telas.administracaoGestao.controller.GestaoService gestao = 
                    telas.administracaoGestao.controller.GestaoService.getInstance();

            // Usa o método novo que acabamos de criar
            telas.administracaoGestao.model.Usuario usuarioEncontrado = gestao.buscarUsuario(cpf);

            // 2. Verifica se achou
            if (usuarioEncontrado != null) {
                System.out.println("Usuário encontrado no GestaoService: " + usuarioEncontrado.getNome());

                // Se o usuário for instância de Recrutador, faz o cast direto
                if (usuarioEncontrado instanceof Recrutador) {
                    this.recrutadorLogado = (Recrutador) usuarioEncontrado;
                } else {
                    // Se for um usuário genérico (criado pelo Admin como 'Recrutador' mas a classe é Usuario ou Gestor)
                    // Criamos um objeto Recrutador temporário apenas para a tela funcionar
                    this.recrutadorLogado = new Recrutador(
                            usuarioEncontrado.getNome(), 
                            usuarioEncontrado.getCpf(), 
                            usuarioEncontrado.getEmail(), 
                            usuarioEncontrado.getLogin(), 
                            "****"
                    );
                }
            } else {
                System.out.println("Usuário NÃO encontrado no GestaoService.");
                // Fallback: Tenta controller local (para testes isolados)
                this.recrutadorLogado = recrutadorController.buscar(cpf);
            }

            // 3. Atualiza a interface visual
            if (this.recrutadorLogado != null) {
                // Se certifica de que os componentes da tela não são nulos
                if (jTextField1 != null) jTextField1.setText(this.recrutadorLogado.getNome());
                if (jTextField2 != null) jTextField2.setText(this.recrutadorLogado.getCpf());

                if (jTextField1 != null) jTextField1.setEditable(false);
                if (jTextField2 != null) jTextField2.setEditable(false);
            }
        }
    
    private void abrirGerenciarCandidaturas() {
        if (recrutadorLogado == null) {
            JOptionPane.showMessageDialog(this, "Nenhum recrutador logado!");
            return;
        }
        GerenciarCandidaturas tela = new GerenciarCandidaturas(this, recrutadorLogado);
        tela.setVisible(true);
        this.dispose();
    }
    
    private void abrirMarcarEntrevistas() {
        if (recrutadorLogado == null) {
            JOptionPane.showMessageDialog(this, "Nenhum recrutador logado!");
            return;
        }
        MarcarEntrevistas tela = new MarcarEntrevistas(this, recrutadorLogado);
        tela.setVisible(true);
        this.dispose();
    }
    
    private void abrirConsultarContratacoes() {
        if (recrutadorLogado == null) {
            JOptionPane.showMessageDialog(this, "Nenhum recrutador logado!");
            return;
        }
        ConsultarContratações tela = new ConsultarContratações(this, recrutadorLogado);  // Passa 'this'
        tela.setVisible(true);
        this.dispose();
    }
    
    private void abrirSolicitarContratacoes() {
        if (recrutadorLogado == null) {
            JOptionPane.showMessageDialog(this, "Nenhum recrutador logado!");
            return;
        }
        SolicitarContratacoes tela = new SolicitarContratacoes(this, recrutadorLogado);  // Passa 'this'
        tela.setVisible(true);
        this.dispose();
    }
    
    private void sair() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente sair e voltar para a tela de Login?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new TelaLogin().setVisible(true);
            this.dispose();
        }
    }
    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Menu de Recrutamento"));

        jLabel1.setText("Recrutador:");

        jLabel2.setText("CPF:");

        jButton1.setText("Sair");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton6.setText("Voltar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton6))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                jPanel3ComponentRemoved(evt);
            }
        });

        jButton2.setText("Gerenciar Candidaturas");

        jButton3.setText("Marcar Entrevistas");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Consultar Contratações");

        jButton5.setText("Solicitar Contratações");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton2, jButton3, jButton4, jButton5});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton5))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel3ComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jPanel3ComponentRemoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel3ComponentRemoved

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        new TelaLogin().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        MarcarEntrevistas telaMarcar = new MarcarEntrevistas(this, this.recrutadorLogado);
        telaMarcar.setVisible(true);
        this.dispose(); // Troca setVisible(false) por dispose() para liberar recursos
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */

    public static void main(String args[]) {
            /* Configuração do visual (Nimbus) */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }

            /* SCRIPT DE TESTE (SEM ALERTA DUPLO) */
            java.awt.EventQueue.invokeLater(() -> {
                Main tela = new Main();
                String cpf = "123.456.789-00";
                
                // 1. Tenta cadastrar direto (sem 'buscar' antes, para evitar o primeiro alerta)
                try {
                    telas.recrutamento.controller.RecrutadorController ctrl = new telas.recrutamento.controller.RecrutadorController();
                    // Tenta cadastrar. Se já existir ou der erro, o catch captura e segue o baile.
                    ctrl.cadastrar(cpf, "Gestor de Teste", "teste@rh.com", "123");
                } catch (Exception e) {
                    // Silenciosamente ignora se já estiver cadastrado
                }

                // 2. Carrega na tela (Única chamada que buscará os dados)
                tela.carregarRecrutador(cpf);
                
                // 3. Preenchimento de segurança (caso o carregamento via arquivo falhe)
                try {
                    if (tela.jTextField1.getText().isEmpty()) {
                        tela.jTextField1.setText("Gestor de Teste");
                        tela.jTextField2.setText(cpf);
                        tela.jTextField1.setEditable(false);
                        tela.jTextField2.setEditable(false);
                    }
                } catch (Exception e) {
                    System.out.println("Campos inacessíveis ou erro visual ignorado.");
                }
                
                tela.setVisible(true);
            });
        }
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
