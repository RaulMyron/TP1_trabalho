package telas.prestacaoservico.view;

import telas.prestacaoservico.controller.GerenciadorDados;
import telas.prestacaoservico.model.ContratoServico;
import telas.prestacaoservico.model.PrestadorServico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GestaoContratos extends JFrame {
    private GerenciadorDados gerenciador;
    private JComboBox<PrestadorServico> cmbPrestador;
    private JComboBox<String> cmbTipoServico, cmbStatus, cmbFiltroStatus, cmbFiltroTipo;
    private JTextField txtValor, txtDataInicio, txtDataFim, txtObservacoes;
    private JTable tabelaContratos;
    private DefaultTableModel modeloTabela;
    private ContratoServico contratoSelecionado;
    private DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public GestaoContratos() {
        gerenciador = GerenciadorDados.getInstancia();
        initComponents();
        carregarContratos();
    }
    
    private void initComponents() {
        setTitle("Gestão de Contratos de Serviço");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Painel superior
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(46, 204, 113));
        JLabel lblTitulo = new JLabel("GESTÃO DE CONTRATOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        painelTitulo.add(lblTitulo);
        
        // Painel de formulário
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Contrato"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Prestador
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Prestador:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        cmbPrestador = new JComboBox<>();
        carregarPrestadores();
        painelFormulario.add(cmbPrestador, gbc);
        gbc.gridwidth = 1;
        
        // Tipo de Serviço
        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Tipo de Serviço:"), gbc);
        gbc.gridx = 1;
        String[] tiposServico = {"Limpeza", "Manutenção", "Consultoria", "TI"};
        cmbTipoServico = new JComboBox<>(tiposServico);
        painelFormulario.add(cmbTipoServico, gbc);
        
        // Valor
        gbc.gridx = 2;
        painelFormulario.add(new JLabel("Valor (R$):"), gbc);
        gbc.gridx = 3;
        txtValor = new JTextField(15);
        painelFormulario.add(txtValor, gbc);
        
        // Data Início
        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Data Início:"), gbc);
        gbc.gridx = 1;
        txtDataInicio = new JTextField(15);
        txtDataInicio.setToolTipText("Formato: dd/MM/yyyy");
        painelFormulario.add(txtDataInicio, gbc);
        
        // Data Fim
        gbc.gridx = 2;
        painelFormulario.add(new JLabel("Data Fim:"), gbc);
        gbc.gridx = 3;
        txtDataFim = new JTextField(15);
        txtDataFim.setToolTipText("Formato: dd/MM/yyyy");
        painelFormulario.add(txtDataFim, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 3;
        painelFormulario.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        String[] status = {"Ativo", "Encerrado", "Pendente de Renovação"};
        cmbStatus = new JComboBox<>(status);
        cmbStatus.setEnabled(false); // Status é calculado automaticamente
        painelFormulario.add(cmbStatus, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 4;
        painelFormulario.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtObservacoes = new JTextField(40);
        painelFormulario.add(txtObservacoes, gbc);
        gbc.gridwidth = 1;
        
        // Painel de botões do formulário
        JPanel painelBotoesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnNovo = new JButton("Novo");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");
        
        btnNovo.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(e -> salvarContrato());
        btnEditar.addActionListener(e -> editarContrato());
        btnExcluir.addActionListener(e -> excluirContrato());
        btnLimpar.addActionListener(e -> limparCampos());
        
        painelBotoesForm.add(btnNovo);
        painelBotoesForm.add(btnSalvar);
        painelBotoesForm.add(btnEditar);
        painelBotoesForm.add(btnExcluir);
        painelBotoesForm.add(btnLimpar);
        
        // Painel de filtros
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));
        
        painelFiltros.add(new JLabel("Status:"));
        String[] filtrosStatus = {"Todos", "Ativo", "Encerrado", "Pendente de Renovação"};
        cmbFiltroStatus = new JComboBox<>(filtrosStatus);
        painelFiltros.add(cmbFiltroStatus);
        
        painelFiltros.add(new JLabel("Tipo de Serviço:"));
        String[] filtrosTipo = {"Todos", "Limpeza", "Manutenção", "Consultoria", "TI"};
        cmbFiltroTipo = new JComboBox<>(filtrosTipo);
        painelFiltros.add(cmbFiltroTipo);
        
        JButton btnFiltrar = new JButton("Aplicar Filtros");
        btnFiltrar.addActionListener(e -> aplicarFiltros());
        painelFiltros.add(btnFiltrar);
        
        JButton btnLimparFiltros = new JButton("Limpar Filtros");
        btnLimparFiltros.addActionListener(e -> {
            cmbFiltroStatus.setSelectedIndex(0);
            cmbFiltroTipo.setSelectedIndex(0);
            carregarContratos();
        });
        painelFiltros.add(btnLimparFiltros);
        
        // Tabela de contratos
        String[] colunas = {"ID", "Prestador", "Tipo Serviço", "Valor", "Data Início", "Data Fim", "Status", "Dias Restantes"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaContratos = new JTable(modeloTabela);
        tabelaContratos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaContratos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarContrato();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabelaContratos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contratos Cadastrados"));
        
        // Painel central
        JPanel painelCentral = new JPanel(new BorderLayout(5, 5));
        painelCentral.add(painelFormulario, BorderLayout.NORTH);
        painelCentral.add(painelBotoesForm, BorderLayout.CENTER);
        
        // Painel inferior
        JPanel painelInferior = new JPanel(new BorderLayout(5, 5));
        painelInferior.add(painelFiltros, BorderLayout.NORTH);
        painelInferior.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botão voltar
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(e -> dispose());
        painelRodape.add(btnVoltar);
        
        add(painelTitulo, BorderLayout.NORTH);
        add(painelCentral, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);
        add(painelRodape, BorderLayout.PAGE_END);
    }
    
    private void carregarPrestadores() {
        cmbPrestador.removeAllItems();
        for (PrestadorServico p : gerenciador.listarPrestadores()) {
            cmbPrestador.addItem(p);
        }
    }
    
    private void salvarContrato() {
        if (!validarCampos()) {
            return;
        }
        
        try {
            PrestadorServico prestador = (PrestadorServico) cmbPrestador.getSelectedItem();
            if (prestador == null) {
                JOptionPane.showMessageDialog(this, "Selecione um prestador!");
                return;
            }
            
            String tipoServico = (String) cmbTipoServico.getSelectedItem();
            double valor = Double.parseDouble(txtValor.getText().trim().replace(",", "."));
            LocalDate dataInicio = LocalDate.parse(txtDataInicio.getText().trim(), formatoData);
            LocalDate dataFim = LocalDate.parse(txtDataFim.getText().trim(), formatoData);
            
            ContratoServico contrato = new ContratoServico();
            contrato.setPrestador(prestador);
            contrato.setTipoServico(tipoServico);
            contrato.setValor(valor);
            contrato.setDataInicio(dataInicio);
            contrato.setDataFim(dataFim);
            contrato.setObservacoes(txtObservacoes.getText().trim());
            
            if (gerenciador.adicionarContrato(contrato)) {
                JOptionPane.showMessageDialog(this, "Contrato cadastrado com sucesso!");
                limparCampos();
                carregarContratos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao cadastrar contrato!\n" +
                    "Verifique:\n" +
                    "- Data de fim deve ser posterior à data de início\n" +
                    "- Não pode haver dois contratos ativos para o mesmo serviço",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data inválida! Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarContrato() {
        if (contratoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um contrato para editar!");
            return;
        }
        
        if (!validarCampos()) {
            return;
        }
        
        try {
            PrestadorServico prestador = (PrestadorServico) cmbPrestador.getSelectedItem();
            String tipoServico = (String) cmbTipoServico.getSelectedItem();
            double valor = Double.parseDouble(txtValor.getText().trim().replace(",", "."));
            LocalDate dataInicio = LocalDate.parse(txtDataInicio.getText().trim(), formatoData);
            LocalDate dataFim = LocalDate.parse(txtDataFim.getText().trim(), formatoData);
            
            contratoSelecionado.setPrestador(prestador);
            contratoSelecionado.setTipoServico(tipoServico);
            contratoSelecionado.setValor(valor);
            contratoSelecionado.setDataInicio(dataInicio);
            contratoSelecionado.setDataFim(dataFim);
            contratoSelecionado.setObservacoes(txtObservacoes.getText().trim());
            
            if (gerenciador.editarContrato(contratoSelecionado)) {
                JOptionPane.showMessageDialog(this, "Contrato editado com sucesso!");
                limparCampos();
                carregarContratos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao editar contrato! Verifique as datas.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data inválida! Use o formato dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirContrato() {
        if (contratoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um contrato para excluir!");
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o contrato #" + contratoSelecionado.getId() + "?",
            "Confirmação", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            if (gerenciador.excluirContrato(contratoSelecionado.getId())) {
                JOptionPane.showMessageDialog(this, "Contrato excluído com sucesso!");
                limparCampos();
                carregarContratos();
            }
        }
    }
    
    private void selecionarContrato() {
        int linha = tabelaContratos.getSelectedRow();
        if (linha >= 0) {
            int id = (int) modeloTabela.getValueAt(linha, 0);
            contratoSelecionado = gerenciador.buscarContratoPorId(id);
            
            if (contratoSelecionado != null) {
                // Seleciona o prestador no combo
                for (int i = 0; i < cmbPrestador.getItemCount(); i++) {
                    if (cmbPrestador.getItemAt(i).getId() == contratoSelecionado.getPrestador().getId()) {
                        cmbPrestador.setSelectedIndex(i);
                        break;
                    }
                }
                
                cmbTipoServico.setSelectedItem(contratoSelecionado.getTipoServico());
                txtValor.setText(String.format("%.2f", contratoSelecionado.getValor()));
                txtDataInicio.setText(contratoSelecionado.getDataInicio().format(formatoData));
                txtDataFim.setText(contratoSelecionado.getDataFim().format(formatoData));
                cmbStatus.setSelectedItem(contratoSelecionado.getStatus());
                txtObservacoes.setText(contratoSelecionado.getObservacoes());
            }
        }
    }
    
    private void carregarContratos() {
        modeloTabela.setRowCount(0);
        for (ContratoServico c : gerenciador.listarContratos()) {
            long diasRestantes = c.diasRestantes();
            String diasRestantesStr = diasRestantes >= 0 ? String.valueOf(diasRestantes) : "Encerrado";
            
            modeloTabela.addRow(new Object[]{
                c.getId(),
                c.getPrestador().getNome(),
                c.getTipoServico(),
                String.format("R$ %.2f", c.getValor()),
                c.getDataInicio().format(formatoData),
                c.getDataFim().format(formatoData),
                c.getStatus(),
                diasRestantesStr
            });
        }
    }
    
    private void aplicarFiltros() {
        modeloTabela.setRowCount(0);
        
        String statusFiltro = (String) cmbFiltroStatus.getSelectedItem();
        String tipoFiltro = (String) cmbFiltroTipo.getSelectedItem();
        
        java.util.List<ContratoServico> contratos = gerenciador.listarContratos();
        
        for (ContratoServico c : contratos) {
            boolean incluir = true;
            
            // Filtro por status
            if (!statusFiltro.equals("Todos") && !c.getStatus().equals(statusFiltro)) {
                incluir = false;
            }
            
            // Filtro por tipo de serviço
            if (!tipoFiltro.equals("Todos") && !c.getTipoServico().equals(tipoFiltro)) {
                incluir = false;
            }
            
            if (incluir) {
                long diasRestantes = c.diasRestantes();
                String diasRestantesStr = diasRestantes >= 0 ? String.valueOf(diasRestantes) : "Encerrado";
                
                modeloTabela.addRow(new Object[]{
                    c.getId(),
                    c.getPrestador().getNome(),
                    c.getTipoServico(),
                    String.format("R$ %.2f", c.getValor()),
                    c.getDataInicio().format(formatoData),
                    c.getDataFim().format(formatoData),
                    c.getStatus(),
                    diasRestantesStr
                });
            }
        }
    }
    
    private boolean validarCampos() {
        if (cmbPrestador.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um prestador!");
            return false;
        }
        
        if (txtValor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o valor do contrato!");
            txtValor.requestFocus();
            return false;
        }
        
        if (txtDataInicio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a data de início!");
            txtDataInicio.requestFocus();
            return false;
        }
        
        if (txtDataFim.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a data de fim!");
            txtDataFim.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void limparCampos() {
        if (cmbPrestador.getItemCount() > 0) {
            cmbPrestador.setSelectedIndex(0);
        }
        cmbTipoServico.setSelectedIndex(0);
        txtValor.setText("");
        txtDataInicio.setText("");
        txtDataFim.setText("");
        txtObservacoes.setText("");
        contratoSelecionado = null;
        tabelaContratos.clearSelection();
    }
}
