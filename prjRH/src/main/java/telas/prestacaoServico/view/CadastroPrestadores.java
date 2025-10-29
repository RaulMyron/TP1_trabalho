package telas.prestacaoservico.view;

import telas.prestacaoservico.controller.GerenciadorDados;
import telas.prestacaoservico.model.PrestadorServico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CadastroPrestadores extends JFrame {
    private GerenciadorDados gerenciador;
    private JTextField txtNome, txtCpfCnpj, txtContato, txtEmail, txtEndereco;
    private JComboBox<String> cmbTipo, cmbCategoria, cmbFiltroCategoria, cmbFiltroStatus;
    private JTable tabelaPrestadores;
    private DefaultTableModel modeloTabela;
    private PrestadorServico prestadorSelecionado;
    
    public CadastroPrestadores() {
        gerenciador = GerenciadorDados.getInstancia();
        initComponents();
        carregarPrestadores();
    }
    
    private void initComponents() {
        setTitle("Cadastro de Prestadores de Serviço");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Painel superior
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(52, 152, 219));
        JLabel lblTitulo = new JLabel("CADASTRO DE PRESTADORES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        painelTitulo.add(lblTitulo);
        
        // Painel de formulário
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Prestador"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        String[] tipos = {"Pessoa Física", "Pessoa Jurídica"};
        cmbTipo = new JComboBox<>(tipos);
        cmbTipo.addActionListener(e -> atualizarLabelCpfCnpj());
        painelFormulario.add(cmbTipo, gbc);
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtNome = new JTextField(30);
        painelFormulario.add(txtNome, gbc);
        gbc.gridwidth = 1;
        
        // CPF/CNPJ
        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("CPF/CNPJ:"), gbc);
        gbc.gridx = 1;
        txtCpfCnpj = new JTextField(20);
        painelFormulario.add(txtCpfCnpj, gbc);
        
        // Categoria
        gbc.gridx = 2; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 3;
        String[] categorias = {"Limpeza", "Manutenção", "TI", "Consultoria"};
        cmbCategoria = new JComboBox<>(categorias);
        painelFormulario.add(cmbCategoria, gbc);
        
        // Contato
        gbc.gridx = 0; gbc.gridy = 3;
        painelFormulario.add(new JLabel("Contato:"), gbc);
        gbc.gridx = 1;
        txtContato = new JTextField(20);
        painelFormulario.add(txtContato, gbc);
        
        // Email
        gbc.gridx = 2;
        painelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        txtEmail = new JTextField(20);
        painelFormulario.add(txtEmail, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 4;
        painelFormulario.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtEndereco = new JTextField(40);
        painelFormulario.add(txtEndereco, gbc);
        gbc.gridwidth = 1;
        
        // Painel de botões do formulário
        JPanel painelBotoesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnNovo = new JButton("Novo");
        JButton btnSalvar = new JButton("Salvar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");
        
        btnNovo.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(e -> salvarPrestador());
        btnEditar.addActionListener(e -> editarPrestador());
        btnExcluir.addActionListener(e -> excluirPrestador());
        btnLimpar.addActionListener(e -> limparCampos());
        
        painelBotoesForm.add(btnNovo);
        painelBotoesForm.add(btnSalvar);
        painelBotoesForm.add(btnEditar);
        painelBotoesForm.add(btnExcluir);
        painelBotoesForm.add(btnLimpar);
        
        // Painel de filtros
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));
        
        painelFiltros.add(new JLabel("Categoria:"));
        String[] filtrosCat = {"Todas", "Limpeza", "Manutenção", "TI", "Consultoria"};
        cmbFiltroCategoria = new JComboBox<>(filtrosCat);
        painelFiltros.add(cmbFiltroCategoria);
        
        painelFiltros.add(new JLabel("Status:"));
        String[] filtrosStatus = {"Todos", "Apenas Ativos", "Apenas Inativos"};
        cmbFiltroStatus = new JComboBox<>(filtrosStatus);
        painelFiltros.add(cmbFiltroStatus);
        
        JButton btnFiltrar = new JButton("Aplicar Filtros");
        btnFiltrar.addActionListener(e -> aplicarFiltros());
        painelFiltros.add(btnFiltrar);
        
        JButton btnLimparFiltros = new JButton("Limpar Filtros");
        btnLimparFiltros.addActionListener(e -> {
            cmbFiltroCategoria.setSelectedIndex(0);
            cmbFiltroStatus.setSelectedIndex(0);
            carregarPrestadores();
        });
        painelFiltros.add(btnLimparFiltros);
        
        // Tabela de prestadores
        String[] colunas = {"ID", "Nome", "Tipo", "CPF/CNPJ", "Categoria", "Contato", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaPrestadores = new JTable(modeloTabela);
        tabelaPrestadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaPrestadores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selecionarPrestador();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabelaPrestadores);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Prestadores Cadastrados"));
        
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
    
    private void atualizarLabelCpfCnpj() {
        String tipo = (String) cmbTipo.getSelectedItem();
        // Atualização visual já está no label fixo
    }
    
    private void salvarPrestador() {
        if (!validarCampos()) {
            return;
        }
        
        String tipo = cmbTipo.getSelectedIndex() == 0 ? "PF" : "PJ";
        String cpfCnpj = txtCpfCnpj.getText().trim();
        
        // Valida CPF ou CNPJ
        if (tipo.equals("PF")) {
            if (!PrestadorServico.validarCPF(cpfCnpj)) {
                JOptionPane.showMessageDialog(this, "CPF inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (!PrestadorServico.validarCNPJ(cpfCnpj)) {
                JOptionPane.showMessageDialog(this, "CNPJ inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        PrestadorServico prestador = new PrestadorServico();
        prestador.setNome(txtNome.getText().trim());
        prestador.setTipo(tipo);
        prestador.setCpfCnpj(cpfCnpj);
        prestador.setContato(txtContato.getText().trim());
        prestador.setEmail(txtEmail.getText().trim());
        prestador.setEndereco(txtEndereco.getText().trim());
        prestador.setCategoria((String) cmbCategoria.getSelectedItem());
        
        if (gerenciador.adicionarPrestador(prestador)) {
            JOptionPane.showMessageDialog(this, "Prestador cadastrado com sucesso!");
            limparCampos();
            carregarPrestadores();
        } else {
            JOptionPane.showMessageDialog(this, "CPF/CNPJ já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarPrestador() {
        if (prestadorSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um prestador para editar!");
            return;
        }
        
        if (!validarCampos()) {
            return;
        }
        
        String tipo = cmbTipo.getSelectedIndex() == 0 ? "PF" : "PJ";
        String cpfCnpj = txtCpfCnpj.getText().trim();
        
        // Valida CPF ou CNPJ
        if (tipo.equals("PF")) {
            if (!PrestadorServico.validarCPF(cpfCnpj)) {
                JOptionPane.showMessageDialog(this, "CPF inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (!PrestadorServico.validarCNPJ(cpfCnpj)) {
                JOptionPane.showMessageDialog(this, "CNPJ inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        prestadorSelecionado.setNome(txtNome.getText().trim());
        prestadorSelecionado.setTipo(tipo);
        prestadorSelecionado.setCpfCnpj(cpfCnpj);
        prestadorSelecionado.setContato(txtContato.getText().trim());
        prestadorSelecionado.setEmail(txtEmail.getText().trim());
        prestadorSelecionado.setEndereco(txtEndereco.getText().trim());
        prestadorSelecionado.setCategoria((String) cmbCategoria.getSelectedItem());
        
        if (gerenciador.editarPrestador(prestadorSelecionado)) {
            JOptionPane.showMessageDialog(this, "Prestador editado com sucesso!");
            limparCampos();
            carregarPrestadores();
        }
    }
    
    private void excluirPrestador() {
        if (prestadorSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um prestador para excluir!");
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o prestador: " + prestadorSelecionado.getNome() + "?",
            "Confirmação", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            if (gerenciador.excluirPrestador(prestadorSelecionado.getId())) {
                JOptionPane.showMessageDialog(this, "Prestador excluído com sucesso!");
                limparCampos();
                carregarPrestadores();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Não é possível excluir prestador com contrato ativo!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void selecionarPrestador() {
        int linha = tabelaPrestadores.getSelectedRow();
        if (linha >= 0) {
            int id = (int) modeloTabela.getValueAt(linha, 0);
            prestadorSelecionado = gerenciador.buscarPrestadorPorId(id);
            
            if (prestadorSelecionado != null) {
                txtNome.setText(prestadorSelecionado.getNome());
                cmbTipo.setSelectedIndex(prestadorSelecionado.getTipo().equals("PF") ? 0 : 1);
                txtCpfCnpj.setText(prestadorSelecionado.getCpfCnpj());
                txtContato.setText(prestadorSelecionado.getContato());
                txtEmail.setText(prestadorSelecionado.getEmail());
                txtEndereco.setText(prestadorSelecionado.getEndereco());
                cmbCategoria.setSelectedItem(prestadorSelecionado.getCategoria());
            }
        }
    }
    
    private void carregarPrestadores() {
        modeloTabela.setRowCount(0);
        for (PrestadorServico p : gerenciador.listarPrestadores()) {
            String status = p.temContratoAtivo() ? "Ativo" : "Inativo";
            modeloTabela.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getTipo().equals("PF") ? "Pessoa Física" : "Pessoa Jurídica",
                p.getCpfCnpj(),
                p.getCategoria(),
                p.getContato(),
                status
            });
        }
    }
    
    private void aplicarFiltros() {
        modeloTabela.setRowCount(0);
        
        String categoria = (String) cmbFiltroCategoria.getSelectedItem();
        String statusFiltro = (String) cmbFiltroStatus.getSelectedItem();
        
        java.util.List<PrestadorServico> prestadores = gerenciador.listarPrestadores();
        
        for (PrestadorServico p : prestadores) {
            boolean incluir = true;
            
            // Filtro por categoria
            if (!categoria.equals("Todas") && !p.getCategoria().equals(categoria)) {
                incluir = false;
            }
            
            // Filtro por status
            if (statusFiltro.equals("Apenas Ativos") && !p.temContratoAtivo()) {
                incluir = false;
            } else if (statusFiltro.equals("Apenas Inativos") && p.temContratoAtivo()) {
                incluir = false;
            }
            
            if (incluir) {
                String status = p.temContratoAtivo() ? "Ativo" : "Inativo";
                modeloTabela.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getTipo().equals("PF") ? "Pessoa Física" : "Pessoa Jurídica",
                    p.getCpfCnpj(),
                    p.getCategoria(),
                    p.getContato(),
                    status
                });
            }
        }
    }
    
    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do prestador!");
            txtNome.requestFocus();
            return false;
        }
        
        if (txtCpfCnpj.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o CPF/CNPJ!");
            txtCpfCnpj.requestFocus();
            return false;
        }
        
        if (txtContato.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o contato!");
            txtContato.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void limparCampos() {
        txtNome.setText("");
        txtCpfCnpj.setText("");
        txtContato.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbCategoria.setSelectedIndex(0);
        prestadorSelecionado = null;
        tabelaPrestadores.clearSelection();
    }
}