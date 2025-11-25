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
        
    public void adicionarCandidato(String nome, String cpf, String email, String formacao, String experiencia, double pretensaoSalarial, String disponibilidade) throws RHException {
        
        
        // Primeiro limpamos o CPF para garantir que estamos verificando apenas números
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        
        if (!isCPFValido(cpfLimpo)) {
            throw new RHException("O CPF informado é inválido. Verifique os dígitos.");
        }
        // -----------------------------

        // Regra de negócio: Verifica se já existe um candidato com este CPF
        for (Candidato c : this.candidatos) {
            // Compara com o CPF limpo para evitar erros de formatação
            String cpfExistenteLimpo = c.getCpf().replaceAll("[^0-9]", "");
            
            if (cpfExistenteLimpo.equals(cpfLimpo)) {
                throw new RHException("Erro: Já existe um candidato cadastrado com o CPF " + cpf);
            }
        }
        
       
        Candidato novoCandidato = new Candidato(nome, cpf, email, formacao, experiencia, pretensaoSalarial, disponibilidade);
        
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
     * Cria uma nova candidatura vinculando um candidato a uma vaga.
     * Garante que não existam candidaturas duplicadas para o mesmo par (Candidato, Vaga).
     */
    public void criarCandidatura(Candidato candidato, Vaga vaga) throws RHException {
        
        // 1. Validação de Segurança: Verifica se os objetos não são nulos
        if (candidato == null || vaga == null) {
            throw new RHException("Candidato e Vaga são obrigatórios para criar uma candidatura.");
        }

        // 2. Regra de Negócio: Candidatura Única por Vaga
        // Percorre todas as candidaturas já existentes
        for (Candidatura c : this.candidaturas) {
            // Pula candidaturas com dados inválidos/corrompidos
            if (c.getCandidato() == null || c.getVaga() == null) {
                continue;
            }

            // Verifica se é o mesmo candidato (pelo CPF)
            boolean mesmoCandidato = c.getCandidato().getCpf().equals(candidato.getCpf());

            // Verifica se é a mesma vaga (pelo Nome do Cargo)
            boolean mesmaVaga = c.getVaga().getCargo().equals(vaga.getCargo());

            // Se for o MESMO candidato na MESMA vaga -> BLOQUEIA
            if (mesmoCandidato && mesmaVaga) {
                throw new RHException("Este candidato já possui uma candidatura ativa para a vaga de " + vaga.getCargo());
            }
        }
        
        // 3. Se passou pelo loop, significa que é uma combinação nova.
        // Pode criar a candidatura!
        Candidatura novaCandidatura = new Candidatura(candidato, vaga);
        this.candidaturas.add(novaCandidatura);
        
        // 4. Salva no arquivo
        this.candidaturaDAO.salvar(this.candidaturas);
        
        System.out.println("Candidatura realizada com sucesso: " + candidato.getNome() + " -> " + vaga.getCargo());
    }
/**
     * Método auxiliar para validar CPF
     * Verifica tamanho, dígitos repetidos e dígitos verificadores.
     */
    private boolean isCPFValido(String cpf) {
        // 1. Remove caracteres não numéricos (pontos e traços)
        cpf = cpf.replaceAll("[^0-9]", "");

        // 2. Verifica se tem 11 dígitos ou se são todos iguais (ex: 000.000.000-00)
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // 3. Cálculo dos dígitos verificadores (Lógica padrão da Receita Federal)
        try {
            // Calculando o primeiro dígito verificador
            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }

            int r = 11 - (soma % 11);
            char dig10 = (r == 10 || r == 11) ? '0' : (char) (r + '0');

            // Calculando o segundo dígito verificador
            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }

            r = 11 - (soma % 11);
            char dig11 = (r == 10 || r == 11) ? '0' : (char) (r + '0');

            // Verifica se os dígitos calculados batem com os informados
            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));

        } catch (Exception e) {
            return false;
        }
    }   
/**
     * Exclui um candidato e TODAS as suas candidaturas associadas (Exclusão em Cascata).
     * @param cpf O CPF do candidato a ser excluído.
     * @throws RHException Se o candidato não for encontrado.
     */
    public void excluirCandidato(String cpf) throws RHException {
        
        // 1. Busca o candidato para garantir que ele existe
        Candidato candidatoAlvo = buscarPorCpf(cpf);
        
        if (candidatoAlvo == null) {
            throw new RHException("Não foi possível excluir. Candidato não encontrado com o CPF: " + cpf);
        }
        
        // 2. EXCLUSÃO EM CASCATA (O passo mais importante)
        // Remove da lista de candidaturas TODAS as que pertencem a este candidato.
        // Usamos o método removeIf (Java 8+) que é seguro e rápido.
        boolean candidaturasRemovidas = this.candidaturas.removeIf(c -> c.getCandidato().getCpf().equals(cpf));
        
        if (candidaturasRemovidas) {
            System.out.println("Candidaturas associadas foram removidas.");
        }
        
        // 3. Remove o candidato da lista de candidatos
        this.candidatos.remove(candidatoAlvo);
        
        // 4. Salva AMBAS as listas nos arquivos para persistir a mudança
        this.candidaturaDAO.salvar(this.candidaturas); // Salva a lista de candidaturas limpa
        this.candidatoDAO.salvar(this.candidatos);     // Salva a lista de candidatos sem ele
        
        System.out.println("Candidato " + candidatoAlvo.getNome() + " excluído com sucesso.");
    }    
}