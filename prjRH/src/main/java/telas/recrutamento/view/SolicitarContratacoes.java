/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package telas.recrutamento.view;
import telas.recrutamento.controller.ContratacaoController;
import telas.recrutamento.model.Recrutador;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import telas.administracaoGestao.controller.GestaoService;
import telas.administracaoGestao.model.Vaga;

public class SolicitarContratacoes extends javax.swing.JFrame {
    
    private Main menuPai;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SolicitarContratacoes.class.getName());
    private ContratacaoController contratacaoController;
    private Recrutador recrutadorLogado;
    private GestaoService gestaoService;
    
    public SolicitarContratacoes(Recrutador recrutador) {
        initComponents();
        this.recrutadorLogado = recrutador;
        this.contratacaoController = new ContratacaoController();
        this.gestaoService = GestaoService.getInstance();
        setLocationRelativeTo(null);
        setTitle("Solicitar Contratações");
        configurarEventos();
        carregarVagas();
        carregarCandidatosAprovados();
    }

    private void configurarEventos() {
        jButton1.addActionListener(e -> aplicarFiltros());
        jButton2.addActionListener(e -> verCurriculo());
        jButton3.addActionListener(e -> enviarSolicitacao());
        jButton4.addActionListener(e -> voltarMenu());
    }
    
    private void carregarVagas() {
        jComboBox1.removeAllItems();
        List<Vaga> vagas = gestaoService.listarTodasVagas();
        
        jComboBox1.addItem("Selecione uma vaga");
        for (Vaga v : vagas) {
            jComboBox1.addItem(v.getCargo());
        }
        
        // Popular regime
        jComboBox2.removeAllItems();
        jComboBox2.addItem("CLT");
        jComboBox2.addItem("PJ");
        jComboBox2.addItem("Estágio");
    }
    
    private void carregarCandidatosAprovados() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        // Aqui você vai buscar candidaturas com status "Aprovado"
        // E que tenham entrevista realizada
        // Quando tiver integração com Candidatura
        
        // Dados de exemplo
        model.addRow(new Object[]{
            "João Silva", 
            "123.456.789-00", 
            "Desenvolvedor Java", 
            "15/11/2024", 
            "8.5", 
            "Aprovado",
            "Não"
        });
        
        model.addRow(new Object[]{
            "Maria Santos", 
            "987.654.321-00", 
            "Analista RH", 
            "18/11/2024", 
            "9.0", 
            "Aprovado",
            "Não"
        });
        
        JOptionPane.showMessageDialog(this, 
            model.getRowCount() + " candidatos aprovados encontrados!");
    }
    
    private void aplicarFiltros() {
        boolean filtrarPorVaga = jCheckBox1.isSelected();
        boolean apenasComEntrevista = jCheckBox2.isSelected();
        boolean apenasAprovados = jCheckBox3.isSelected();
        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        
        if (!apenasAprovados && !apenasComEntrevista && !filtrarPorVaga) {
            carregarCandidatosAprovados();
            return;
        }
        
        // Aplicar filtros na tabela
        model.setRowCount(0);
        
        // Lógica de filtro aqui
        JOptionPane.showMessageDialog(this, "Filtros aplicados!");
    }
    
    private void verCurriculo() {
        int row = jTable1.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato!");
            return;
        }
        
        String nome = jTable1.getValueAt(row, 0).toString();
        String cpf = jTable1.getValueAt(row, 1).toString();
        
        // Abrir janela com currículo detalhado
        String curriculo = "=== CURRÍCULO ===\n\n" +
                          "Nome: " + nome + "\n" +
                          "CPF: " + cpf + "\n" +
                          "Formação: Ciência da Computação - UFMG\n" +
                          "Experiência: 3 anos\n" +
                          "Habilidades: Java, Spring, MySQL\n" +
                          "Idiomas: Inglês (Fluente)";
        
        JOptionPane.showMessageDialog(this, curriculo, 
            "Currículo - " + nome, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void enviarSolicitacao() {
        int row = jTable1.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato da tabela!");
            return;
        }
        
        try {
            String vagaSel = (String) jComboBox1.getSelectedItem();
            String regime = (String) jComboBox2.getSelectedItem();
            String dataInicioStr = jTextField1.getText();
            String salarioStr = jTextField2.getText();
            String justificativa = jTextArea1.getText();
            
            if (vagaSel.equals("Selecione uma vaga") || regime == null || 
                dataInicioStr.isEmpty() || salarioStr.isEmpty() || justificativa.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }
            
            // Validar data
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dataInicio = sdf.parse(dataInicioStr);
            
            // Validar que data não é no passado
            if (dataInicio.before(new Date())) {
                JOptionPane.showMessageDialog(this, "A data de início não pode ser no passado!");
                return;
            }
            
            // Validar salário
            double salario = Double.parseDouble(salarioStr.replace(",", "."));
            if (salario <= 0) {
                JOptionPane.showMessageDialog(this, "Salário deve ser maior que zero!");
                return;
            }
            
            // Pegar CPF do candidato da tabela
            String cpfCandidato = jTable1.getValueAt(row, 1).toString();
            String candidaturaId = cpfCandidato; // Temporário
            
            contratacaoController.solicitar(
                candidaturaId,
                recrutadorLogado.getCpf(),
                regime,
                salario,
                dataInicio,
                justificativa
            );
            
            // Marcar na tabela que já possui solicitação
            jTable1.setValueAt("Sim", row, 6);
            
            limparCampos();
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de data inválido! Use dd/MM/yyyy");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de salário inválido! Use apenas números");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao enviar solicitação: " + e.getMessage());
        }
    }
    
    private void limparCampos() {
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jTextField1.setText("dd/MM/yyyy");
        jTextField2.setText("");
        jTextArea1.setText("");
        jTable1.clearSelection();
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
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Solicitar Autorização de Contratação"));

        jLabel1.setText("Filtrar por:");

        jCheckBox1.setText("Por Vaga");

        jCheckBox2.setText("Mostrar apenas com entrevista realizada");

        jCheckBox3.setText("Mostrar apenas aprovados");

        jButton1.setText("Aplicar Filtros");

        jLabel2.setText("Candidatos");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Nome", "CPF", "Vaga", "Data Entrevista ", "Nota Entrevista ", "Status Candidatura ", "Possui Solicitação?"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton2.setText("Ver Currículo Completo");

        jLabel3.setText("Solicitar Contratação do Selecionado Acima");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Vaga");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setText("Regime Trabalhista");

        jLabel6.setText("Data de Inicio");

        jLabel7.setText("Salário Proposto");

        jLabel8.setText("Justificativa Contratação");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jButton3.setText("✅ Enviar Solicitação ao Gestor");

        jButton4.setText("Voltar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 813, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jCheckBox1)
                        .addComponent(jCheckBox2)
                        .addComponent(jCheckBox3)
                        .addComponent(jButton1))
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addComponent(jButton3))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            new SolicitarContratacoes(recrutadorTeste).setVisible(true);
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
