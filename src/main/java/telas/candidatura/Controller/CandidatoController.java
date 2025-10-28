package telas.candidatura.Controller;

import telas.candidatura.Excecao.RHException;
import telas.candidatura.Model.Candidato;
import telas.candidatura.Model.CandidatoDAO;
import java.util.List;
import telas.candidatura.Model.Vaga;
import telas.candidatura.Model.Candidatura;
import java.util.ArrayList;
import telas.candidatura.Model.CandidaturaDAO;
import java.text.SimpleDateFormat; // Para formatar a data

/**
 * Controller responsável por intermediar as ações da View com a lógica do Model
 * para a entidade Candidato.
 */
public class CandidatoController {

    private final CandidatoDAO candidatoDAO;
    private final CandidaturaDAO candidaturaDAO;
    private List<Candidato> candidatos;
    private List<Candidatura> candidaturas; // Para gerenciar as candidaturas em memória
    private List<Vaga> vagasDisponiveis; // Para simular as vagas disponíveis

    // O construtor é executado quando o controller é criado.
    public CandidatoController() {
        this.candidatoDAO = new CandidatoDAO();
        // Carrega a lista de candidatos do arquivo para a memória.
        this.candidatos = this.candidatoDAO.carregar();
        this.candidaturaDAO = new CandidaturaDAO();
        
        // Inicializa as novas listas
        this.candidaturas = this.candidaturaDAO.carregar();
        
        // Simulação de vagas (para não depender do Aluno 1 ainda)
        this.vagasDisponiveis = new ArrayList<>();
        this.vagasDisponiveis.add(new Vaga("Desenvolvedor Java Pleno", 5000.0));
        this.vagasDisponiveis.add(new Vaga("Analista de Dados Júnior", 3500.0));
    }
    

    /**
     * Adiciona um novo candidato ao sistema.
     * @throws RHException se um candidato com o mesmo CPF já existir.
     */
    public void adicionarCandidato(String nome, String cpf, String email, String formacao, String experiencia, double pretensaoSalarial, String disponibilidade) throws RHException {
        
        // Regra de negócio: Verifica se já existe um candidato com este CPF.
        for (Candidato c : this.candidatos) {
            if (c.getCpf().equals(cpf)) {
                // Se encontrar, lança uma exceção para a View tratar.
                throw new RHException("Erro: Já existe um candidato cadastrado com o CPF " + cpf);
            }
        }
        
        // Se a validação passar, cria o novo objeto Candidato.
        Candidato novoCandidato = new Candidato(nome, cpf, email, formacao, experiencia, pretensaoSalarial, disponibilidade);
        
        // Adiciona o novo candidato à lista em memória.
        this.candidatos.add(novoCandidato);
        
        // Salva a lista atualizada de volta no arquivo.
        this.candidatoDAO.salvar(this.candidatos);
    }

    /**
     * Retorna a lista completa de candidatos para exibição na View.
     * @return A lista de todos os candidatos cadastrados.
     */
    public List<Candidato> getListaCandidatos() {
        return this.candidatos;
    }
    
    /**
     * Busca um candidato pelo seu CPF.
     * @param cpf O CPF a ser buscado.
     * @return O objeto Candidato se encontrado, ou null se não existir.
     */
    public Candidato buscarPorCpf(String cpf) {
        for (Candidato c : this.candidatos) {
            if (c.getCpf().equals(cpf)) {
                return c;
            }
        }
        return null; // Retorna null se não encontrar.
    }
    /**
     * Retorna a lista de vagas disponíveis para preencher a ComboBox.
     */
    public List<Vaga> getVagasDisponiveis() {
        return this.vagasDisponiveis;
    }
    
    /**
     * Retorna a lista de todas as candidaturas feitas.
     */
    public List<Candidatura> getListaCandidaturas() {
        return this.candidaturas;
    }

    /**
     * Cria e salva uma nova candidatura.
     * @param candidato O candidato que está se aplicando.
     * @param vaga A vaga para a qual ele está se aplicando.
     * @throws RHException se a candidatura já existir.
     */
    public void criarCandidatura(Candidato candidato, Vaga vaga) throws RHException {
        // Regra de negócio: Verifica se este candidato já se aplicou para esta vaga.
        for (Candidatura c : this.candidaturas) {
            if (c.getCandidato().equals(candidato) && c.getVaga().equals(vaga)) {
                throw new RHException("Este candidato já se candidatou para esta vaga.");
            }
        }
        
        // Se a validação passar, cria a nova candidatura.
        Candidatura novaCandidatura = new Candidatura(candidato, vaga);
        this.candidaturas.add(novaCandidatura);
        // AGORA VAMOS SALVAR A LISTA NO ARQUIVO
        this.candidaturaDAO.salvar(this.candidaturas);
        
        // Futuramente, aqui também chamaremos um DAO para salvar a lista de candidaturas.
        System.out.println("Candidatura criada com sucesso!");
    }
    
    
}