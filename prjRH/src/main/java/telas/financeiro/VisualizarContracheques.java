/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package telas.financeiro;
import telas.financeiro.controller.ControllerManager;
import telas.financeiro.controller.FinanceiroController;
import telas.financeiro.model.Funcionario;
import telas.financeiro.model.FolhaPagamento;
import telas.financeiro.model.RegraSalario;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 *
 * @author felip
 */
public class VisualizarContracheques extends javax.swing.JFrame {
    
    // ATRIBUTOS
    private FinanceiroController controller;
    
    public VisualizarContracheques() {
        initComponents();
        this.controller = ControllerManager.getFinanceiroController();
        configurarTela();
        carregarFuncionarios();
    }
    
    private void configurarTela() {
        setTitle("Visualizar Contracheques");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Configurar meses
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                         "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        cmbMes.setModel(new javax.swing.DefaultComboBoxModel<>(meses));
        
        // Configurar anos
        String[] anos = {"2024", "2025", "2026"};
        cmbAno.setModel(new javax.swing.DefaultComboBoxModel<>(anos));
        
        // Selecionar mês/ano atual
        LocalDate hoje = LocalDate.now();
        cmbMes.setSelectedIndex(hoje.getMonthValue() - 1);
        cmbAno.setSelectedItem(String.valueOf(hoje.getYear()));
        
        // Desabilitar botão imprimir
        btnImprimir.setEnabled(false);
    }
    
    /**
     * Carrega a lista de funcionários ativos no ComboBox
     */
    private void carregarFuncionarios() {
        cmbFuncionario.removeAllItems();
        
        List<Funcionario> funcionarios = controller.listarAtivos();
        
        if (funcionarios.isEmpty()) {
            cmbFuncionario.addItem("Nenhum funcionário ativo");
            btnBuscar.setEnabled(false);
            return;
        }
        
        for (Funcionario func : funcionarios) {
            // Formato: "001 - João Silva"
            String item = func.getMatricula() + " - " + func.getNome();
            cmbFuncionario.addItem(item);
        }
    }
    
    /**
     * Busca e exibe o contracheque
     */
    private void buscarContracheque() {
        try {
            // 1. OBTER funcionário selecionado
            String itemSelecionado = (String) cmbFuncionario.getSelectedItem();
            
            if (itemSelecionado == null || itemSelecionado.equals("Nenhum funcionário ativo")) {
                JOptionPane.showMessageDialog(this,
                    "Selecione um funcionário!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Extrair matrícula (formato: "001 - João Silva")
            String matricula = itemSelecionado.split(" - ")[0];
            Funcionario func = controller.buscarPorMatricula(matricula);
            
            if (func == null) {
                JOptionPane.showMessageDialog(this,
                    "Funcionário não encontrado!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 2. OBTER mês/ano
            int mes = cmbMes.getSelectedIndex() + 1;
            int ano = Integer.parseInt((String) cmbAno.getSelectedItem());
            YearMonth mesReferencia = YearMonth.of(ano, mes);
            
            // 3. BUSCAR a folha daquele mês
            FolhaPagamento folha = controller.buscarFolhaPorMes(mesReferencia);
            
            if (folha == null) {
                JOptionPane.showMessageDialog(this,
                    "Não existe folha de pagamento gerada para " + 
                    cmbMes.getSelectedItem() + "/" + ano + "!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 4. BUSCAR o item do funcionário na folha
            FolhaPagamento.ItemFolha item = null;
            for (FolhaPagamento.ItemFolha i : folha.getItens()) {
                if (i.getMatricula().equals(matricula)) {
                    item = i;
                    break;
                }
            }
            
            if (item == null) {
                JOptionPane.showMessageDialog(this,
                    "Funcionário não consta na folha de " + 
                    cmbMes.getSelectedItem() + "/" + ano + "!",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 5. GERAR o texto do contracheque
            String contracheque = gerarTextoContracheque(func, item, mesReferencia);
            
            // 6. EXIBIR na área de texto
            txtContracheque.setText(contracheque);
            
            // Habilitar impressão
            btnImprimir.setEnabled(true);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao buscar contracheque: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Gera o texto formatado do contracheque
     */
    private String gerarTextoContracheque(Funcionario func, 
                                          FolhaPagamento.ItemFolha item, 
                                          YearMonth mesRef) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("═══════════════════════════════════════════════════════\n");
        sb.append("              CONTRACHEQUE DE PAGAMENTO                \n");
        sb.append("═══════════════════════════════════════════════════════\n\n");
        
        sb.append("Empresa: Sistema de RH - UnB\n");
        sb.append("Período: ").append(mesRef.getMonth().name()).append("/").append(mesRef.getYear()).append("\n");
        sb.append("Data de Emissão: ").append(LocalDate.now().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n\n");
        
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append("DADOS DO FUNCIONÁRIO\n");
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append("Nome: ").append(func.getNome()).append("\n");
        sb.append("CPF: ").append(func.getCpf()).append("\n");
        sb.append("Matrícula: ").append(func.getMatricula()).append("\n");
        sb.append("Cargo: ").append(func.getCargo()).append("\n");
        sb.append("Departamento: ").append(func.getDepartamento()).append("\n");
        sb.append("Regime: ").append(func.getRegimeContratacao()).append("\n\n");
        
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append("DEMONSTRATIVO DE PAGAMENTO\n");
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append(String.format("%-40s %15s\n", "Descrição", "Valor"));
        sb.append("───────────────────────────────────────────────────────\n");
        
        sb.append(String.format("%-40s R$ %12.2f\n", "Salário Base", item.getSalarioBase()));
        
        if (item.getBeneficios() > 0) {
            sb.append(String.format("%-40s R$ %12.2f\n", "(+) Benefícios", item.getBeneficios()));
        }
        
        if (item.getDescontos() > 0) {
            sb.append(String.format("%-40s R$ %12.2f\n", "(-) Descontos", item.getDescontos()));
        }
        
        sb.append("───────────────────────────────────────────────────────\n");
        sb.append(String.format("%-40s R$ %12.2f\n", "TOTAL LÍQUIDO", item.getSalarioLiquido()));
        sb.append("═══════════════════════════════════════════════════════\n\n");
        
        sb.append("DETALHAMENTO:\n");
        
        String regime = func.getRegimeContratacao().toUpperCase();
        RegraSalario regra = controller.getRegraSalarialAtual();
        
        if (regime.equals("CLT")) {
            sb.append("\nBenefícios:\n");
            sb.append(String.format("  - Vale Transporte: R$ %.2f\n", regra.getValorValeTransporte()));
            sb.append(String.format("  - Vale Alimentação: R$ %.2f\n", regra.getValorValeAlimentacao()));
            
            sb.append("\nDescontos:\n");
            double descontoINSS = item.getSalarioBase() * (regra.getPercentualINSS() / 100.0);
            double descontoIRRF = item.getSalarioBase() * (regra.getPercentualIRRF() / 100.0);
            sb.append(String.format("  - INSS (%.1f%%): R$ %.2f\n", regra.getPercentualINSS(), descontoINSS));
            sb.append(String.format("  - IRRF (%.1f%%): R$ %.2f\n", regra.getPercentualIRRF(), descontoIRRF));
            
        } else if (regime.equals("ESTÁGIO") || regime.equals("ESTAGIO")) {
            sb.append("\nBenefícios:\n");
            sb.append(String.format("  - Auxílio Transporte: R$ %.2f\n", 
                regra.getValorAuxilioTransporteEstagio()));
            sb.append("\nObs: Estagiários não têm descontos de INSS e IRRF.\n");
            
        } else if (regime.equals("PJ")) {
            sb.append("\nObs: Profissionais PJ não recebem benefícios e\n");
            sb.append("são responsáveis pelo recolhimento de seus próprios impostos.\n");
        }
        
        sb.append("\n═══════════════════════════════════════════════════════\n");
        sb.append("Este documento é meramente informativo.\n");
        sb.append("═══════════════════════════════════════════════════════\n");
        
        return sb.toString();
    }
    
    /**
     * Imprime o contracheque (simplificado)
     */
    private void imprimirContracheque() {
        if (txtContracheque.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Busque um contracheque antes de imprimir!",
                "Atenção",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Usar a funcionalidade de impressão do Java
        try {
            boolean imprimiu = txtContracheque.print();
            if (imprimiu) {
                JOptionPane.showMessageDialog(this,
                    "Contracheque enviado para impressão!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao imprimir: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cmbFuncionario = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cmbMes = new javax.swing.JComboBox<>();
        cmbAno = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtContracheque = new javax.swing.JTextArea();
        btnBuscar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Selecionar Funcionário:");

        cmbFuncionario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Mês/Ano:");

        cmbMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbAno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtContracheque.setEditable(false);
        txtContracheque.setColumns(20);
        txtContracheque.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        txtContracheque.setRows(5);
        jScrollPane1.setViewportView(txtContracheque);

        btnBuscar.setText("Buscar Contracheque");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnImprimir.setText("Imprimir");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Contracheque");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(btnBuscar)
                        .addGap(18, 18, 18)
                        .addComponent(btnImprimir)
                        .addGap(18, 18, 18)
                        .addComponent(btnVoltar)
                        .addGap(0, 212, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar)
                    .addComponent(btnImprimir)
                    .addComponent(btnVoltar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        buscarContracheque();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
        imprimirContracheque();
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new VisualizarContracheques().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JComboBox<String> cmbAno;
    private javax.swing.JComboBox<String> cmbFuncionario;
    private javax.swing.JComboBox<String> cmbMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtContracheque;
    // End of variables declaration//GEN-END:variables
}
