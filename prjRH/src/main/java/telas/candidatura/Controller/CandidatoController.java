package telas.candidatura.Controller;

import telas.candidatura.Excecao.RHException;
import telas.candidatura.Model.Candidato;
import telas.candidatura.Model.CandidatoDAO;
import java.util.List;
import telas.administracaoGestao.model.Vaga;
import telas.candidatura.Model.Candidatura;
import java.util.ArrayList;
import telas.candidatura.Model.CandidaturaDAO;
import java.text.SimpleDateFormat; // Para formatar a data
import telas.administracaoGestao.controller.GestaoService;
import telas.administracaoGestao.model.Vaga;



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
    private final GestaoService gestaoService;

    // O construtor é executado quando o controller é criado.
    public CandidatoController() {
        this.candidatoDAO = new CandidatoDAO();
        // Carrega a lista de candidatos do arquivo para a memória.
        this.candidatos = this.candidatoDAO.carregar();
        this.candidaturaDAO = new CandidaturaDAO();
        
        // Inicializa as novas listas
        this.candidaturas = this.candidaturaDAO.carregar();
        
        // Inicialize o Service dela
        this.gestaoService = new GestaoService();

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
   public List<Vaga> getVagasDisponiveis() {
    // Agora ele chama o Service da sua colega para buscar as vagas REAIS
    // Usando o nome exato do método que encontramos: "listarTodasVagas"
    return this.gestaoService.listarTodasVagas(); 
}
    
    /**
     * Retorna a lista de todas as candidaturas feitas.
     */
    public List<Candidatura> getListaCandidaturas() {
        return this.candidaturas;
    }
    // Este é um NOVO MÉTODO dentro da sua classe CandidatoController
/**
 * Busca na lista de candidaturas por um termo (Nome ou CPF do candidato).
 * A busca não diferencia maiúsculas de minúsculas.
 *
 * @param termo O texto a ser buscado no nome ou CPF.
 * @return Uma nova lista contendo apenas as candidaturas que correspondem ao filtro.
 */
// Este é o NOVO método de filtro. Adicione-o ao seu CandidatoController.

/**
 * Filtra a lista de candidaturas com base em três critérios combinados.
 * A busca não diferencia maiúsculas de minúsculas.
 *
 * @param filtroVaga O texto do cargo da vaga (ou vazio).
 * @param filtroStatus O status selecionado (ou "Todos").
 * @param filtroNomeCpf O nome ou CPF do candidato (ou vazio).
 * @return Uma nova lista contendo apenas as candidaturas que passam por todos os filtros.
 */
public List<Candidatura> filtrarCandidaturas(String filtroVaga, String filtroStatus, String filtroNomeCpf) {
    
    // Converte os filtros para minúsculas para a busca ser flexível
    String vagaTermo = filtroVaga.toLowerCase();
    String statusTermo = filtroStatus.toLowerCase();
    String nomeCpfTermo = filtroNomeCpf.toLowerCase();

    // Cria a lista de resultados
    List<Candidatura> resultados = new ArrayList<>();

    // Percorre a lista principal de candidaturas (this.candidaturas)
    for (Candidatura c : this.candidaturas) {
        // Pega os dados da candidatura atual em minúsculas
        String vagaDaCandidatura = c.getVaga().getCargo().toLowerCase();
        String statusDaCandidatura = c.getStatus().toLowerCase();
        String nomeDoCandidato = c.getCandidato().getNome().toLowerCase();
        String cpfDoCandidato = c.getCandidato().getCpf().toLowerCase();

        // --------------------------------------------------------------------
        // LÓGICA DO FILTRO: O candidato só passa se atender a TODOS os critérios
        // --------------------------------------------------------------------

        // 1. Verifica o filtro de Vaga
        // Se o filtro de vaga não estiver vazio E o cargo da candidatura não contiver o termo, REJEITA.
        if (!vagaTermo.isEmpty() && !vagaDaCandidatura.contains(vagaTermo)) {
            continue; // Pula para a próxima candidatura
        }

        // 2. Verifica o filtro de Status
        // Se o status NÃO for "todos" E o status da candidatura não contiver o termo, REJEITA.
        if (!statusTermo.equals("todos") && !statusDaCandidatura.contains(statusTermo)) {
            continue; // Pula para a próxima candidatura
        }

        // 3. Verifica o filtro de Nome/CPF
        // Se o filtro não estiver vazio E (nem o nome E nem o CPF contiverem o termo), REJEITA.
        if (!nomeCpfTermo.isEmpty() && (!nomeDoCandidato.contains(nomeCpfTermo) && !cpfDoCandidato.contains(nomeCpfTermo))) {
            continue; // Pula para a próxima candidatura
        }

        // Se a candidatura sobreviveu a todos os filtros, é um resultado válido.
        resultados.add(c);
    }

    return resultados; // Retorna a lista filtrada
}

// Este é um NOVO MÉTODO dentro da sua classe CandidatoController

/**
 * Altera o status de uma candidatura específica e salva a lista atualizada no arquivo.
 *
 * @param candidatura O objeto Candidatura a ser modificado.
 * @param novoStatus O novo status (ex: "Aprovado").
 */
public void alterarStatusCandidatura(Candidatura candidatura, String novoStatus) {
    // 1. Altera o status do objeto que está na memória
    candidatura.setStatus(novoStatus);
    
    // 2. Salva a lista inteira (this.candidaturas), que agora contém o objeto
    //    modificado, de volta no arquivo.
    this.candidaturaDAO.salvar(this.candidaturas);
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