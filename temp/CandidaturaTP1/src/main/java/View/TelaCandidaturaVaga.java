/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package telas.candidatura.View;

import telas.candidatura.Controller.CandidatoController;
import telas.administracaoGestao.controller.GestaoService;
import telas.administracaoGestao.model.Vaga;
import telas.candidatura.Model.Candidato;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.List;

public class TelaCandidaturaVaga extends javax.swing.JFrame {

    private JFrame menuPai;
    private final CandidatoController controller;
    private final GestaoService gestaoService;
    
    private Candidato candidatoEncontrado = null; 

    public TelaCandidaturaVaga() {
        initComponents();
        this.controller = new CandidatoController();
        this.gestaoService = GestaoService.getInstance();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popularVagas();
    }

    public TelaCandidaturaVaga(JFrame menuPai) {
        this();
        this.menuPai = menuPai;
    }

    private void popularVagas() {
        List<Vaga> vagas = gestaoService.listarTodasVagas();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        
        if (vagas.isEmpty()) {
            model.addElement("Nenhuma vaga disponível");
            jComboBox2.setEnabled(false);
        } else {
            jComboBox2.setEnabled(true);
            for (Vaga v : vagas) {
                model.addElement(v); 
            }
        }
        jComboBox2.setModel(model);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        lblNomeCandidato = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); 
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Candidatura à Vaga");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Associe um candidato cadastrado a uma vaga disponível");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        jLabel3.setText("Buscar um Candidato (CPF):");

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        jLabel4.setText("Selecionar Vaga:");

        jButton2.setText("Confirmar Candidatura");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Cancelar / Voltar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        lblNomeCandidato.setFont(new java.awt.Font("Segoe UI", 2, 12)); 
        lblNomeCandidato.setForeground(new java.awt.Color(0, 102, 255));
        lblNomeCandidato.setText("Nenhum candidato selecionado");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblNomeCandidato, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jLabel1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(lblNomeCandidato))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String cpf = jTextField1.getText();
        this.candidatoEncontrado = controller.buscarPorCpf(cpf);

        if (this.candidatoEncontrado != null) {
            lblNomeCandidato.setText(this.candidatoEncontrado.getNome()); 
            JOptionPane.showMessageDialog(this, "Candidato encontrado: " + this.candidatoEncontrado.getNome());
        } else {
            this.candidatoEncontrado = null;
            lblNomeCandidato.setText("Não encontrado");
            JOptionPane.showMessageDialog(this, "Candidato não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }                                        

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        if (this.candidatoEncontrado == null) {
            JOptionPane.showMessageDialog(this, "Busque um candidato válido primeiro.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object itemSelecionado = jComboBox2.getSelectedItem();
        if (itemSelecionado == null || itemSelecionado instanceof String) { 
            JOptionPane.showMessageDialog(this, "Selecione uma vaga válida.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Vaga vagaSelecionada = (Vaga) itemSelecionado;
            
            // Verifica duplicidade
            boolean jaExiste = controller.getListaCandidaturas().stream()
                .anyMatch(c -> c.getCandidato().getCpf().equals(candidatoEncontrado.getCpf()) 
                            && c.getVaga().getCargo().equals(vagaSelecionada.getCargo()));
            
            if (jaExiste) {
                JOptionPane.showMessageDialog(this, "Candidato já aplicou para esta vaga!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            controller.criarCandidatura(this.candidatoEncontrado, vagaSelecionada);
            JOptionPane.showMessageDialog(this, "Candidatura realizada com sucesso!");
            voltar();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }                                        

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        voltar();
    }                                        

    private void voltar() {
        this.dispose();
        if (this.menuPai != null) {
            this.menuPai.setVisible(true);
        }
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<Object> jComboBox2; 
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblNomeCandidato; 
    // End of variables declaration                   
}