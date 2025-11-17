package telas.candidatura.Controller;

import telas.candidatura.Excecao.RHException;
import telas.candidatura.Model.Candidato;
import telas.candidatura.Model.CandidatoDAO;
import java.util.List;
import telas.administracaoGestao.model.Vaga;
import telas.candidatura.Model.Candidatura;
import java.util.ArrayList;
import telas.candidatura.Model.CandidaturaDAO;
import telas.administracaoGestao.controller.GestaoService;

public class CandidatoController {
    
    private final CandidatoDAO candidatoDAO = new CandidatoDAO();
    private final CandidaturaDAO candidaturaDAO = new CandidaturaDAO();
    private final GestaoService gestaoService = GestaoService.getInstance();
    private final List<Candidato> candidatos = candidatoDAO.carregar();
    private final List<Candidatura> candidaturas = candidaturaDAO.carregar();
        
    public void adicionarCandidato(String nome, String cpf, String email, 
                                   String formacao, String experiencia, 
                                   double pretensaoSalarial, String disponibilidade) 
                                   throws RHException {
        
        // Valida se CPF já existe
        for (Candidato c : this.candidatos) {
            if (c.getCpf().equals(cpf)) {
                throw new RHException("Erro: Já existe um candidato cadastrado com o CPF " + cpf);
            }
        }
        
        // Cria e adiciona o novo candidato
        Candidato novoCandidato = new Candidato(nome, cpf, email, formacao, 
                                                experiencia, pretensaoSalarial, disponibilidade);
        this.candidatos.add(novoCandidato);
        this.candidatoDAO.salvar(this.candidatos);
    }



    public List<Candidato> getListaCandidatos() {
        return this.candidatos;
    }
    

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
    return this.gestaoService.listarTodasVagas(); 
}
    
    /**
     * Retorna a lista de todas as candidaturas feitas.
     */
    public List<Candidatura> getListaCandidaturas() {
        return this.candidaturas;
    }

/**
 * Busca na lista de candidaturas por um termo (Nome ou CPF do candidato).
 * A busca não diferencia maiúsculas de minúsculas.
 *
 * @param termo O texto a ser buscado no nome ou CPF.
 * @return Uma nova lista contendo apenas as candidaturas que correspondem ao filtro.
 */

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
        this.candidaturaDAO.salvar(this.candidaturas);
        
        System.out.println("Candidatura criada com sucesso!");
    }
    
    
}