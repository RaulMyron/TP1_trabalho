package telas.recrutamento.model;

import telas.administracaoGestao.model.Usuario;
import telas.administracaoGestao.model.Perfil;
import telas.administracaoGestao.model.Vaga;
import telas.recrutamento.exception.RecrutamentoException;
import telas.recrutamento.util.ValidadorRecrutamento;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

/**
 * Classe que representa um Recrutador no sistema de RH.
 * Responsável por gerenciar vagas e processos de recrutamento.
 */
public class Recrutador extends Usuario {

    private static final long serialVersionUID = 664657771188656315L;

    private String departamento;
    private List<Vaga> vagasGerenciadas;
    private List<String> especialidades;
    private Date dataContratacao;
    private boolean ativo;
    private int totalContratacoesSolicitadas;
    private int totalContratacaoAprovadas;

    /**
     * Construtor principal do Recrutador.
     * Aplica validações nos dados fornecidos.
     */
    public Recrutador(String cpf, String nome, String email, String login, String senha)
            throws RecrutamentoException {
        super(nome, cpf, email, login, senha);

        // Validações
        ValidadorRecrutamento.validarCPF(cpf);
        ValidadorRecrutamento.validarNome(nome);
        ValidadorRecrutamento.validarEmail(email);
        ValidadorRecrutamento.validarLogin(login);
        ValidadorRecrutamento.validarSenha(senha);

        addPerfil(Perfil.RECRUTADOR);
        this.vagasGerenciadas = new ArrayList<>();
        this.especialidades = new ArrayList<>();
        this.dataContratacao = new Date();
        this.ativo = true;
        this.totalContratacoesSolicitadas = 0;
        this.totalContratacaoAprovadas = 0;
    }

    // --- Métodos de Gerenciamento de Vagas ---

    /**
     * Adiciona uma vaga à lista de vagas gerenciadas pelo recrutador.
     */
    public void adicionarVagaGerenciada(Vaga vaga) throws RecrutamentoException {
        if (vaga == null) {
            throw new RecrutamentoException("Vaga não pode ser nula!");
        }
        if (!vagasGerenciadas.contains(vaga)) {
            vagasGerenciadas.add(vaga);
            vaga.setRecrutadorResponsavel(this);
        }
    }

    /**
     * Remove uma vaga da lista de vagas gerenciadas.
     */
    public void removerVagaGerenciada(Vaga vaga) {
        vagasGerenciadas.remove(vaga);
    }

    /**
     * Retorna lista imutável de vagas gerenciadas.
     */
    public List<Vaga> getVagasGerenciadas() {
        return Collections.unmodifiableList(vagasGerenciadas);
    }

    /**
     * Retorna quantidade de vagas abertas sob responsabilidade do recrutador.
     */
    public int getQuantidadeVagasAbertas() {
        return (int) vagasGerenciadas.stream()
                .filter(v -> "aberta".equalsIgnoreCase(v.getStatus()))
                .count();
    }

    /**
     * Retorna quantidade total de vagas gerenciadas.
     */
    public int getTotalVagasGerenciadas() {
        return vagasGerenciadas.size();
    }

    // --- Métodos de Especialidades ---

    /**
     * Adiciona uma especialidade ao recrutador.
     */
    public void adicionarEspecialidade(String especialidade) throws RecrutamentoException {
        if (especialidade == null || especialidade.trim().isEmpty()) {
            throw new RecrutamentoException("Especialidade não pode ser vazia!");
        }
        if (!especialidades.contains(especialidade.trim())) {
            especialidades.add(especialidade.trim());
        }
    }

    /**
     * Remove uma especialidade do recrutador.
     */
    public void removerEspecialidade(String especialidade) {
        especialidades.remove(especialidade);
    }

    /**
     * Retorna lista imutável de especialidades.
     */
    public List<String> getEspecialidades() {
        return Collections.unmodifiableList(especialidades);
    }

    /**
     * Verifica se o recrutador possui uma especialidade específica.
     */
    public boolean temEspecialidade(String especialidade) {
        return especialidades.contains(especialidade);
    }

    // --- Métodos de Estatísticas ---

    /**
     * Incrementa o contador de contratações solicitadas.
     */
    public void incrementarContratacoesSolicitadas() {
        this.totalContratacoesSolicitadas++;
    }

    /**
     * Incrementa o contador de contratações aprovadas.
     */
    public void incrementarContratacoesAprovadas() {
        this.totalContratacaoAprovadas++;
    }

    /**
     * Calcula a taxa de aprovação de contratações.
     */
    public double getTaxaAprovacao() {
        if (totalContratacoesSolicitadas == 0) {
            return 0.0;
        }
        return (double) totalContratacaoAprovadas / totalContratacoesSolicitadas * 100;
    }

    // --- Getters e Setters ---

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        // Validação removida do setter para manter compatibilidade
        // A validação é feita no construtor
        this.departamento = departamento;
    }

    public Date getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(Date dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    public int getTotalContratacoesSolicitadas() {
        return totalContratacoesSolicitadas;
    }

    public int getTotalContratacaoAprovadas() {
        return totalContratacaoAprovadas;
    }

    /**
     * Valida se o recrutador está apto a gerenciar novas vagas.
     */
    public boolean isAptoParaNovasVagas() {
        return ativo && isAtivo();
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%d vagas)",
            getNome(),
            departamento != null ? departamento : "Sem departamento",
            getQuantidadeVagasAbertas());
    }

    /**
     * Método para garantir compatibilidade com dados antigos.
     * Inicializa campos que podem ser nulos ao deserializar.
     */
    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();

        // Inicializa campos novos se forem nulos (dados antigos)
        if (vagasGerenciadas == null) {
            vagasGerenciadas = new ArrayList<>();
        }
        if (especialidades == null) {
            especialidades = new ArrayList<>();
        }
        if (dataContratacao == null) {
            dataContratacao = new Date();
        }
    }
}