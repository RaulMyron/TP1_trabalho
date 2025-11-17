package Controller;

import Excecao.RHException;
import Model.Candidato;
import Model.CandidatoDAO;
import java.util.List;
import telas.administracaoGestao.model.Vaga;
import Model.Candidatura;
import java.util.ArrayList;
import telas.administracaoGestao.model.Vaga;
import telas.candidatura.Model.Candidato;
import telas.candidatura.Model.CandidatoDAO;
import telas.candidatura.Model.Candidatura;
import telas.candidatura.Model.CandidaturaDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsável por intermediar as ações da View com a lógica do Model
 * para a entidade Candidato.
 */
public class CandidatoController {

    private final CandidatoDAO candidatoDAO;
    private final List<Candidato> candidatos = new ArrayList<>();
    private final List<Candidatura> candidaturas = new ArrayList<>(); // Para gerenciar as candidaturas em memória
    private final List<Vaga> vagasDisponiveis = new ArrayList<>(); // Para simular as vagas disponíveis
    private final CandidaturaDAO candidaturaDAO;

    // O construtor é executado quando o controller é criado.
    public CandidatoController() {
        this.candidatoDAO = new CandidatoDAO();
        this.candidaturaDAO = new CandidaturaDAO();
    }

    public List<Candidatura> getListaCandidaturas() {
        return candidaturaDAO.carregar(); 
    }

    public List<Candidato> getListaCandidatos() {
            // CORREÇÃO: Mesma coisa para o candidatoDAO, se estiver errado
            return candidatoDAO.carregar();
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
     * Busca um candidato pelo seu CPF.
     * @param cpf O CPF a ser buscado.
     * @return O objeto Candidato se encontrado, ou null se não existir.
     */
    public Candidato buscarPorCpf(String cpf) {
            // Usa carregar() para buscar na lista
            return candidatoDAO.carregar().stream()
                    .filter(c -> c.getCpf().equals(cpf))
                    .findFirst()
                    .orElse(null);
    }

    public List<Vaga> getVagasDisponiveis() {
        return this.vagasDisponiveis;
    }

    /**
     * Cria e salva uma nova candidatura.
     * @param candidato O candidato que está se aplicando.
     * @param vaga A vaga para a qual ele está se aplicando.
     * @throws RHException se a candidatura já existir.
     */
    public void criarCandidatura(Candidato candidato, Vaga vaga) {
            List<Candidatura> lista = candidaturaDAO.carregar();
            Candidatura nova = new Candidatura(candidato, vaga, "Pendente");
            lista.add(nova);
            candidaturaDAO.salvar(lista);
        }
    
    public List<Candidatura> filtrarCandidaturas(String vaga, String status, String nomeCpf) {
        List<Candidatura> todas = this.getListaCandidaturas(); // Usa o método corrigido acima
        
        return todas.stream()
            .filter(c -> {
                // Filtro de Vaga (se digitou algo)
                boolean matchVaga = true;
                if (!vaga.isEmpty()) {
                    // Proteção contra nulos
                    String nomeVaga = (c.getVaga() != null) ? c.getVaga().getCargo() : "";
                    matchVaga = nomeVaga.toLowerCase().contains(vaga.toLowerCase());
                }
                
                // Filtro de Status (se não for "Todos")
                boolean matchStatus = true;
                if (!status.equals("Todos")) {
                    matchStatus = c.getStatus().equalsIgnoreCase(status);
                }

                // Filtro de Nome/CPF
                boolean matchNome = true;
                if (!nomeCpf.isEmpty()) {
                     String nome = (c.getCandidato() != null) ? c.getCandidato().getNome() : "";
                     String cpf = (c.getCandidato() != null) ? c.getCandidato().getCpf() : "";
                     matchNome = nome.toLowerCase().contains(nomeCpf.toLowerCase()) || cpf.contains(nomeCpf);
                }

                return matchVaga && matchStatus && matchNome;
            })
            .collect(Collectors.toList());
    }

    public void alterarStatusCandidatura(Candidatura candidatura, String novoStatus) {
        List<Candidatura> lista = candidaturaDAO.carregar();
        
        // Encontra a candidatura na lista e atualiza
        for (Candidatura c : lista) {
            // Compara por referência de objetos ou por chaves únicas (CPF + Vaga)
            if (c.getCandidato().getCpf().equals(candidatura.getCandidato().getCpf()) &&
                c.getVaga().getCargo().equals(candidatura.getVaga().getCargo())) {
                c.setStatus(novoStatus);
                break;
            }
        }
        candidaturaDAO.salvar(lista);
    }

}