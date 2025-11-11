package telas.prestacaoservico.view;

import telas.prestacaoservico.controller.GerenciadorDados;
import telas.prestacaoservico.model.PrestadorServico;
import telas.prestacaoservico.model.ContratoServico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatoriosPrestadores extends JFrame {
    private GerenciadorDados gerenciador;
    private JComboBox<String> cmbTipoRelatorio;
    private JComboBox<String> cmbParametro;
    private JTable tabelaRelatorio;
    private DefaultTableModel modeloTabela;
    private JTextArea txtResumo;
    private DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public RelatoriosPrestadores() {
        gerenciador = GerenciadorDados.getInstancia();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Relatórios de Prestadores");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Painel superior
        JPanel painelTitulo = new JPanel();
        JLabel lblTitulo = new JLabel("RELATÓRIOS DE PRESTADORES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelTitulo.add(lblTitulo);
        
        // Painel de seleção de relatório
        JPanel painelSelecao = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelSelecao.setBorder(BorderFactory.createTitledBorder("Selecione o Tipo de Relatório"));
        
        painelSelecao.add(new JLabel("Tipo de Relatório:"));
        String[] tiposRelatorio = {
            "Prestadores por Categoria",
            "Prestadores por Status de Contrato",
            "Contratos por Tipo de Serviço",
            "Prestadores Ativos com Detalhes",
            "Histórico Completo de Contratos"
        };
        cmbTipoRelatorio = new JComboBox<>(tiposRelatorio);
        cmbTipoRelatorio.addActionListener(e -> atualizarParametros());
        painelSelecao.add(cmbTipoRelatorio);
        
        painelSelecao.add(new JLabel("Parâmetro:"));
        cmbParametro = new JComboBox<>();
        painelSelecao.add(cmbParametro);
        
        JButton btnGerar = new JButton("Gerar Relatório");
        btnGerar.addActionListener(e -> gerarRelatorio());
        painelSelecao.add(btnGerar);
        
        JButton btnExportar = new JButton("Exportar para Texto");
        btnExportar.addActionListener(e -> exportarRelatorio());
        painelSelecao.add(btnExportar);
        
        JPanel painelResumo = new JPanel(new BorderLayout());
        painelResumo.setBorder(BorderFactory.createTitledBorder("Resumo"));
        txtResumo = new JTextArea(3, 50);
        txtResumo.setEditable(false);
        txtResumo.setFont(new Font("Arial", Font.PLAIN, 12));
        txtResumo.setBackground(new Color(245, 245, 245));
        JScrollPane scrollResumo = new JScrollPane(txtResumo);
        painelResumo.add(scrollResumo, BorderLayout.CENTER);
        
        modeloTabela = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaRelatorio = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaRelatorio);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Dados do Relatório"));
        
        // Painel central
        JPanel painelCentral = new JPanel(new BorderLayout(5, 5));
        painelCentral.add(painelResumo, BorderLayout.NORTH);
        painelCentral.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botão voltar
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(e -> dispose());
        painelRodape.add(btnVoltar);
        
        add(painelTitulo, BorderLayout.NORTH);
        add(painelSelecao, BorderLayout.PAGE_START);
        add(painelCentral, BorderLayout.CENTER);
        add(painelRodape, BorderLayout.SOUTH);
        
        atualizarParametros();
    }
    
    private void atualizarParametros() {
        cmbParametro.removeAllItems();
        int tipoSelecionado = cmbTipoRelatorio.getSelectedIndex();
        
        switch (tipoSelecionado) {
            case 0: // Prestadores por Categoria
                cmbParametro.addItem("Todas");
                cmbParametro.addItem("Limpeza");
                cmbParametro.addItem("Manutenção");
                cmbParametro.addItem("TI");
                cmbParametro.addItem("Consultoria");
                break;
            case 1: // Prestadores por Status
                cmbParametro.addItem("Ativos");
                cmbParametro.addItem("Inativos");
                cmbParametro.addItem("Todos");
                break;
            case 2: // Contratos por Tipo de Serviço
                cmbParametro.addItem("Todos");
                cmbParametro.addItem("Limpeza");
                cmbParametro.addItem("Manutenção");
                cmbParametro.addItem("Consultoria");
                cmbParametro.addItem("TI");
                break;
            case 3: // Prestadores Ativos com Detalhes
                cmbParametro.addItem("Todos os Ativos");
                break;
            case 4: // Histórico Completo
                cmbParametro.addItem("Todos os Registros");
                break;
        }
    }
    
    private void gerarRelatorio() {
        int tipoSelecionado = cmbTipoRelatorio.getSelectedIndex();
        String parametro = (String) cmbParametro.getSelectedItem();
        
        switch (tipoSelecionado) {
            case 0:
                gerarRelatorioPorCategoria(parametro);
                break;
            case 1:
                gerarRelatorioPorStatus(parametro);
                break;
            case 2:
                gerarRelatorioContratosPorTipo(parametro);
                break;
            case 3:
                gerarRelatorioAtivosDetalhados();
                break;
            case 4:
                gerarRelatorioHistoricoCompleto();
                break;
        }
    }
    
    private void gerarRelatorioPorCategoria(String categoria) {
        modeloTabela.setRowCount(0);
        modeloTabela.setColumnCount(0);
        
        String[] colunas = {"ID", "Nome", "CPF/CNPJ", "Categoria", "Contato", "Email", "Status"};
        for (String col : colunas) {
            modeloTabela.addColumn(col);
        }
        
        List<PrestadorServico> prestadores;
        if (categoria.equals("Todas")) {
            prestadores = gerenciador.listarPrestadores();
        } else {
            prestadores = gerenciador.filtrarPrestadoresPorCategoria(categoria);
        }
        
        int totalAtivos = 0;
        int totalInativos = 0;
        
        for (PrestadorServico p : prestadores) {
            boolean ativo = p.temContratoAtivo();
            if (ativo) totalAtivos++;
            else totalInativos++;
            
            modeloTabela.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getCpfCnpj(),
                p.getCategoria(),
                p.getContato(),
                p.getEmail(),
                ativo ? "Ativo" : "Inativo"
            });
        }
        
        String resumo = String.format(
            "Relatório de Prestadores por Categoria: %s\n" +
            "Total de Prestadores: %d\n" +
            "Ativos: %d | Inativos: %d",
            categoria, prestadores.size(), totalAtivos, totalInativos
        );
        txtResumo.setText(resumo);
    }
    
    private void gerarRelatorioPorStatus(String status) {
        modeloTabela.setRowCount(0);
        modeloTabela.setColumnCount(0);
        
        String[] colunas = {"ID", "Nome", "Categoria", "CPF/CNPJ", "Contato", "Contratos Ativos"};
        for (String col : colunas) {
            modeloTabela.addColumn(col);
        }
        
        List<PrestadorServico> prestadores;
        if (status.equals("Ativos")) {
            prestadores = gerenciador.filtrarPrestadoresPorStatus(true);
        } else if (status.equals("Inativos")) {
            prestadores = gerenciador.filtrarPrestadoresPorStatus(false);
        } else {
            prestadores = gerenciador.listarPrestadores();
        }
        
        for (PrestadorServico p : prestadores) {
            int contratosAtivos = 0;
            for (ContratoServico c : p.getContratos()) {
                if (c.getStatus().equals("Ativo")) {
                    contratosAtivos++;
                }
            }
            
            modeloTabela.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getCategoria(),
                p.getCpfCnpj(),
                p.getContato(),
                contratosAtivos
            });
        }
        
        String resumo = String.format(
            "Relatório de Prestadores - Status: %s\n" +
            "Total de Prestadores: %d",
            status, prestadores.size()
        );
        txtResumo.setText(resumo);
    }
    
    private void gerarRelatorioContratosPorTipo(String tipoServico) {
        modeloTabela.setRowCount(0);
        modeloTabela.setColumnCount(0);
        
        String[] colunas = {"ID", "Prestador", "Tipo Serviço", "Valor", "Data Início", "Data Fim", "Status"};
        for (String col : colunas) {
            modeloTabela.addColumn(col);
        }
        
        List<ContratoServico> contratos;
        if (tipoServico.equals("Todos")) {
            contratos = gerenciador.listarContratos();
        } else {
            contratos = gerenciador.filtrarContratosPorTipoServico(tipoServico);
        }
        
        double valorTotal = 0;
        int ativos = 0;
        int encerrados = 0;
        int pendentes = 0;
        
        for (ContratoServico c : contratos) {
            valorTotal += c.getValor();
            
            switch (c.getStatus()) {
                case "Ativo":
                    ativos++;
                    break;
                case "Encerrado":
                    encerrados++;
                    break;
                case "Pendente de Renovação":
                    pendentes++;
                    break;
            }
            
            modeloTabela.addRow(new Object[]{
                c.getId(),
                c.getPrestador().getNome(),
                c.getTipoServico(),
                String.format("R$ %.2f", c.getValor()),
                c.getDataInicio().format(formatoData),
                c.getDataFim().format(formatoData),
                c.getStatus()
            });
        }
        
        String resumo = String.format(
            "Relatório de Contratos - Tipo: %s\n" +
            "Total de Contratos: %d | Valor Total: R$ %.2f\n" +
            "Ativos: %d | Encerrados: %d | Pendentes: %d",
            tipoServico, contratos.size(), valorTotal, ativos, encerrados, pendentes
        );
        txtResumo.setText(resumo);
    }
    
    private void gerarRelatorioAtivosDetalhados() {
        modeloTabela.setRowCount(0);
        modeloTabela.setColumnCount(0);
        
        String[] colunas = {"Prestador", "Categoria", "Tipo Serviço", "Valor Mensal", "Vencimento", "Dias Restantes"};
        for (String col : colunas) {
            modeloTabela.addColumn(col);
        }
        
        List<PrestadorServico> prestadores = gerenciador.filtrarPrestadoresPorStatus(true);
        double valorTotalMensal = 0;
        int totalContratos = 0;
        
        for (PrestadorServico p : prestadores) {
            for (ContratoServico c : p.getContratos()) {
                if (c.getStatus().equals("Ativo")) {
                    valorTotalMensal += c.getValor();
                    totalContratos++;
                    
                    modeloTabela.addRow(new Object[]{
                        p.getNome(),
                        p.getCategoria(),
                        c.getTipoServico(),
                        String.format("R$ %.2f", c.getValor()),
                        c.getDataFim().format(formatoData),
                        c.diasRestantes() + " dias"
                    });
                }
            }
        }
        
        String resumo = String.format(
            "Relatório Detalhado de Prestadores Ativos\n" +
            "Prestadores com Contrato Ativo: %d\n" +
            "Total de Contratos Ativos: %d | Valor Total Mensal: R$ %.2f",
            prestadores.size(), totalContratos, valorTotalMensal
        );
        txtResumo.setText(resumo);
    }
    
    private void gerarRelatorioHistoricoCompleto() {
        modeloTabela.setRowCount(0);
        modeloTabela.setColumnCount(0);
        
        String[] colunas = {"ID Contrato", "Prestador", "CPF/CNPJ", "Categoria", "Tipo Serviço", "Valor", "Início", "Fim", "Status"};
        for (String col : colunas) {
            modeloTabela.addColumn(col);
        }
        
        List<ContratoServico> contratos = gerenciador.listarContratos();
        double valorTotal = 0;
        
        for (ContratoServico c : contratos) {
            valorTotal += c.getValor();
            
            modeloTabela.addRow(new Object[]{
                c.getId(),
                c.getPrestador().getNome(),
                c.getPrestador().getCpfCnpj(),
                c.getPrestador().getCategoria(),
                c.getTipoServico(),
                String.format("R$ %.2f", c.getValor()),
                c.getDataInicio().format(formatoData),
                c.getDataFim().format(formatoData),
                c.getStatus()
            });
        }
        
        String resumo = String.format(
            "Histórico Completo de Contratos\n" +
            "Total de Contratos Registrados: %d\n" +
            "Valor Total Acumulado: R$ %.2f",
            contratos.size(), valorTotal
        );
        txtResumo.setText(resumo);
    }
    
    private void exportarRelatorio() {
        if (modeloTabela.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Gere um relatório antes de exportar!");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(80)).append("\n");
        sb.append(txtResumo.getText()).append("\n");
        sb.append("=".repeat(80)).append("\n\n");
        
        // Cabeçalhos
        for (int i = 0; i < modeloTabela.getColumnCount(); i++) {
            sb.append(modeloTabela.getColumnName(i)).append("\t");
        }
        sb.append("\n");
        sb.append("-".repeat(80)).append("\n");
        
        // Dados
        for (int i = 0; i < modeloTabela.getRowCount(); i++) {
            for (int j = 0; j < modeloTabela.getColumnCount(); j++) {
                sb.append(modeloTabela.getValueAt(i, j)).append("\t");
            }
            sb.append("\n");
        }
        
        JTextArea txtExportar = new JTextArea(sb.toString());
        txtExportar.setEditable(false);
        txtExportar.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        JScrollPane scrollPane = new JScrollPane(txtExportar);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Relatório Exportado", JOptionPane.INFORMATION_MESSAGE);
    }
}