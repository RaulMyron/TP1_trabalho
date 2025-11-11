package telas.prestacaoservico.view;

import telas.prestacaoservico.controller.GerenciadorDados;
import telas.prestacaoservico.model.ContratoServico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AlertasVencimento extends JFrame {
    private GerenciadorDados gerenciador;
    private JTable tabelaAlertas;
    private DefaultTableModel modeloTabela;
    private JLabel lblTotalAlertas;
    private DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public AlertasVencimento() {
        gerenciador = GerenciadorDados.getInstancia();
        initComponents();
        carregarAlertas();
    }
    
    private void initComponents() {
        setTitle("Alertas de Contratos Próximos do Vencimento");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Painel superior
        JPanel painelTitulo = new JPanel();
        JLabel lblTitulo = new JLabel("⚠ ALERTAS DE VENCIMENTO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelTitulo.add(lblTitulo);
        
        // Painel de informações
        JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        painelInfo.setBackground(new Color(255, 243, 224));
        painelInfo.setBorder(BorderFactory.createLineBorder(new Color(230, 126, 34), 2));
        
        JLabel lblInfo = new JLabel("ℹ Contratos com vencimento nos próximos 30 dias:");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 13));
        painelInfo.add(lblInfo);
        
        lblTotalAlertas = new JLabel("0 contrato(s)");
        lblTotalAlertas.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalAlertas.setForeground(new Color(192, 57, 43));
        painelInfo.add(lblTotalAlertas);
        
        // Painel de botões de ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnAtualizar = new JButton("Atualizar Alertas");
        btnAtualizar.addActionListener(e -> carregarAlertas());
        
        JButton btnVerContrato = new JButton("Ver Detalhes do Contrato");
        btnVerContrato.addActionListener(e -> verDetalhesContrato());
        
        JButton btnGestaoContratos = new JButton("Ir para Gestão de Contratos");
        btnGestaoContratos.addActionListener(e -> {
            new GestaoContratos().setVisible(true);
        });
        
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnVerContrato);
        painelBotoes.add(btnGestaoContratos);
        
        String[] colunas = {
            "ID", "Prestador", "CPF/CNPJ", "Tipo Serviço", 
            "Valor", "Data Vencimento", "Dias Restantes", "Status"
        };
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaAlertas = new JTable(modeloTabela);
        tabelaAlertas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaAlertas.setRowHeight(25);
        
        tabelaAlertas.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    try {
                        int diasRestantes = Integer.parseInt(modeloTabela.getValueAt(row, 6).toString());
                        
                        if (diasRestantes <= 7) {
                            c.setBackground(new Color(255, 200, 200)); // Vermelho claro - crítico
                        } else if (diasRestantes <= 15) {
                            c.setBackground(new Color(255, 235, 200)); // Laranja claro - alerta
                        } else {
                            c.setBackground(new Color(255, 255, 200)); // Amarelo claro - atenção
                        }
                    } catch (Exception e) {
                        c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabelaAlertas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contratos Próximos do Vencimento"));
        
        JPanel painelLegenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        painelLegenda.setBorder(BorderFactory.createTitledBorder("Legenda de Prioridade"));
        
        JLabel lblCritico = new JLabel("■ Crítico (≤ 7 dias)");
        lblCritico.setForeground(new Color(192, 57, 43));
        lblCritico.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel lblAlerta = new JLabel("■ Alerta (8-15 dias)");
        lblAlerta.setForeground(new Color(230, 126, 34));
        lblAlerta.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel lblAtencao = new JLabel("■ Atenção (16-30 dias)");
        lblAtencao.setForeground(new Color(241, 196, 15));
        lblAtencao.setFont(new Font("Arial", Font.BOLD, 12));
        
        painelLegenda.add(lblCritico);
        painelLegenda.add(lblAlerta);
        painelLegenda.add(lblAtencao);
        
        JPanel painelCentral = new JPanel(new BorderLayout(5, 5));
        painelCentral.add(painelInfo, BorderLayout.NORTH);
        painelCentral.add(scrollPane, BorderLayout.CENTER);
        painelCentral.add(painelLegenda, BorderLayout.SOUTH);
        
        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.add(painelBotoes, BorderLayout.CENTER);
        
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(e -> dispose());
        painelRodape.add(btnVoltar);
        painelInferior.add(painelRodape, BorderLayout.SOUTH);
        
        add(painelTitulo, BorderLayout.NORTH);
        add(painelCentral, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);
    }
    
    private void carregarAlertas() {
        modeloTabela.setRowCount(0);
        
        List<ContratoServico> contratosProximos = gerenciador.filtrarContratosProximosVencimento();
        
        for (ContratoServico c : contratosProximos) {
            long diasRestantes = c.diasRestantes();
            
            modeloTabela.addRow(new Object[]{
                c.getId(),
                c.getPrestador().getNome(),
                c.getPrestador().getCpfCnpj(),
                c.getTipoServico(),
                String.format("R$ %.2f", c.getValor()),
                c.getDataFim().format(formatoData),
                diasRestantes,
                c.getStatus()
            });
        }
        
        lblTotalAlertas.setText(contratosProximos.size() + " contrato(s)");
        
        long contratosCriticos = contratosProximos.stream()
                .filter(c -> c.diasRestantes() <= 7)
                .count();
        
        if (contratosCriticos > 0) {
            JOptionPane.showMessageDialog(this,
                String.format("ATENÇÃO: %d contrato(s) vencem em 7 dias ou menos!\n" +
                             "Providencie a renovação urgentemente.", contratosCriticos),
                "Alerta Crítico",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void verDetalhesContrato() {
        int linha = tabelaAlertas.getSelectedRow();
        
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um contrato na tabela!", 
                "Aviso", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int id = (int) modeloTabela.getValueAt(linha, 0);
        ContratoServico contrato = gerenciador.buscarContratoPorId(id);
        
        if (contrato != null) {
            StringBuilder detalhes = new StringBuilder();
            detalhes.append("DETALHES DO CONTRATO\n");
            detalhes.append("=".repeat(50)).append("\n\n");
            detalhes.append("ID do Contrato: ").append(contrato.getId()).append("\n");
            detalhes.append("Prestador: ").append(contrato.getPrestador().getNome()).append("\n");
            detalhes.append("CPF/CNPJ: ").append(contrato.getPrestador().getCpfCnpj()).append("\n");
            detalhes.append("Categoria: ").append(contrato.getPrestador().getCategoria()).append("\n");
            detalhes.append("Contato: ").append(contrato.getPrestador().getContato()).append("\n");
            detalhes.append("Email: ").append(contrato.getPrestador().getEmail()).append("\n\n");
            detalhes.append("Tipo de Serviço: ").append(contrato.getTipoServico()).append("\n");
            detalhes.append("Valor Mensal: R$ ").append(String.format("%.2f", contrato.getValor())).append("\n");
            detalhes.append("Data de Início: ").append(contrato.getDataInicio().format(formatoData)).append("\n");
            detalhes.append("Data de Vencimento: ").append(contrato.getDataFim().format(formatoData)).append("\n");
            detalhes.append("Status: ").append(contrato.getStatus()).append("\n");
            detalhes.append("Dias Restantes: ").append(contrato.diasRestantes()).append(" dias\n\n");
            
            if (contrato.getObservacoes() != null && !contrato.getObservacoes().isEmpty()) {
                detalhes.append("Observações: ").append(contrato.getObservacoes()).append("\n");
            }
            
            long diasRestantes = contrato.diasRestantes();
            detalhes.append("\n").append("=".repeat(50)).append("\n");
            detalhes.append("RECOMENDAÇÃO:\n");
            
            if (diasRestantes <= 7) {
                detalhes.append("⚠ URGENTE: Renove este contrato imediatamente!\n");
                detalhes.append("O vencimento está próximo e pode haver interrupção do serviço.");
            } else if (diasRestantes <= 15) {
                detalhes.append("⚠ ALERTA: Inicie o processo de renovação em breve.\n");
                detalhes.append("Entre em contato com o prestador para negociação.");
            } else {
                detalhes.append("ℹ ATENÇÃO: Planeje a renovação deste contrato.\n");
                detalhes.append("Avalie a qualidade do serviço e necessidade de renovação.");
            }
            
            JTextArea txtDetalhes = new JTextArea(detalhes.toString());
            txtDetalhes.setEditable(false);
            txtDetalhes.setFont(new Font("Monospaced", Font.PLAIN, 12));
            txtDetalhes.setCaretPosition(0);
            
            JScrollPane scrollPane = new JScrollPane(txtDetalhes);
            scrollPane.setPreferredSize(new Dimension(600, 450));
            
            JOptionPane.showMessageDialog(this, 
                scrollPane, 
                "Detalhes do Contrato #" + id, 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}