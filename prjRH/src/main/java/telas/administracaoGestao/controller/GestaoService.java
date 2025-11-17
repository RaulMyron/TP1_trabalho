package telas.administracaoGestao.controller;

// Os imports agora usam os pacotes do SEU projeto
import telas.administracaoGestao.model.*;
import telas.administracaoGestao.excecoes.NegocioException;
import telas.recrutamento.model.Recrutador;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// O CONTROLLER
public class GestaoService {
   
    private List<Usuario> usuarios;
    private List<Vaga> vagas;
    
    private static final String ARQUIVO_USUARIOS = "usuarios.dat";
    private static final String ARQUIVO_VAGAS = "vagas.dat";
    
    private static GestaoService instance;
    private RepositorioBase<Usuario> repoUsuarios;
    private RepositorioBase<Vaga> repoVagas;

    private GestaoService() {
        this.repoUsuarios = new RepositorioBase<>();
        this.repoVagas = new RepositorioBase<>();
        
        try {
            this.usuarios = repoUsuarios.carregar(ARQUIVO_USUARIOS);
            this.vagas = repoVagas.carregar(ARQUIVO_VAGAS);
            if (this.usuarios.isEmpty() && this.vagas.isEmpty()) {
                System.out.println("Primeira execução detectada. Inicializando dados...");
            } else {
                System.out.println("Dados carregados com sucesso: " + this.usuarios.size() + " usuários, " + this.vagas.size() + " vagas.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            this.usuarios = new ArrayList<>();
            this.vagas = new ArrayList<>();
        }
        
        if (this.usuarios.isEmpty()) {
            System.out.println("Nenhum usuário encontrado. Criando admin padrão.");
            Usuario adminPadrao = new Administrador("Admin", "00000000000", "admin@rh.com", "admin", "admin123");
            this.usuarios.add(adminPadrao);
            try {
                repoUsuarios.salvar(this.usuarios, ARQUIVO_USUARIOS);
                System.out.println("Admin padrão salvo com sucesso.");
            } catch (IOException e) {
                System.err.println("Erro ao salvar admin padrão: " + e.getMessage());
            }
        }
       
    }
    public static GestaoService getInstance() {
        if (instance == null) {
            instance = new GestaoService();
        }
        return instance;
    }
    // Método para a TELA DE LOGIN
    public Usuario login(String login, String senha) throws NegocioException {
        Optional<Usuario> optUsuario = usuarios.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst();

        if (optUsuario.isEmpty()) {
            throw new NegocioException("Usuário não encontrado.");
        }
        
        Usuario u = optUsuario.get();
        if (u.autenticar(senha)) {
            return u; // Login bem-sucedido
        } else {
            throw new NegocioException("Senha incorreta.");
        }
    }
    
    // Método para a TELA DE GESTÃO DE USUÁRIOS (ADMIN)
    public void cadastrarUsuario(Usuario ator, String nome, String cpf, String email, String login, String senha, String tipo) throws NegocioException, IOException {
        
        if (!ator.getPerfis().contains(Perfil.ADMINISTRADOR)) {
            throw new NegocioException("Acesso negado: Somente administradores podem criar usuários.");
        }
        
        if (usuarios.stream().anyMatch(u -> u.getCpf().equals(cpf))) {
            throw new NegocioException("CPF já cadastrado.");
        }
        
        Usuario novoUsuario;
        switch (tipo) {
            case "Administrador":
                novoUsuario = new Administrador(nome, cpf, email, login, senha);
                break;
                
            case "Gestor":
                novoUsuario = new Gestor(nome, cpf, email, login, senha);
                break;
            
            case "Recrutador":
                // O construtor do Recrutador é um pouco diferente
                novoUsuario = new Recrutador(cpf, nome, email, login, senha); 
                break;
            
            default:
                // Se selecionar "Candidatura" ou "Financeiro" (da sua imagem), dará um erro
                throw new NegocioException("O perfil '" + tipo + "' não é um tipo de usuário que pode ser criado por aqui.");
        }
        
        usuarios.add(novoUsuario);
        repoUsuarios.salvar(usuarios, ARQUIVO_USUARIOS); 
        
    }
    
    // Método para a TELA DE GESTÃO DE VAGAS (GESTOR)
    public void criarVaga(Usuario ator, String cargo, double salario, Usuario recrutador) throws NegocioException, IOException {
        
        if (!ator.getPerfis().contains(Perfil.GESTOR)) {
            throw new NegocioException("Acesso negado: Somente gestores podem criar vagas.");
        }
        
        if (recrutador == null) {
            throw new NegocioException("Erro: Você deve selecionar um recrutador responsável.");
        }
        
        Vaga novaVaga = new Vaga(cargo, salario);
        
        novaVaga.setRecrutadorResponsavel(recrutador);
        
        vagas.add(novaVaga);
        repoVagas.salvar(vagas, ARQUIVO_VAGAS);
    }
    
    public List<Usuario> listarTodosUsuarios() {
        return this.usuarios;
    }
    public List<Vaga> listarTodasVagas() {
        // Este método simplesmente retorna a lista de vagas que o 
        // GestaoService já carregou do arquivo no construtor.
        return this.vagas;
    }
    public List<Usuario> listarRecrutadores() {
        return this.usuarios.stream()
            .filter(u -> u.getPerfis().contains(Perfil.RECRUTADOR))
            .collect(Collectors.toList());
    }
// Em GestaoService.java
// Substitua o método login(String, char[]) antigo por este:

    public Usuario login(String login, char[] senha) throws NegocioException {
        // 1. Converte o array de char para String
        String senhaString = new String(senha);

        // 2. CORREÇÃO: Procurar pelo LOGIN, não pelo CPF
        Optional<Usuario> optUsuario = usuarios.stream()
                .filter(u -> u.getLogin().equals(login)) // <-- MUDANÇA AQUI
                .findFirst();

        if (optUsuario.isEmpty()) {
            throw new NegocioException("Usuário não encontrado."); // Mensagem de erro
        }
        
        Usuario u = optUsuario.get();
        
        // 3. Autentica usando a senha como String
        if (u.autenticar(senhaString)) {
            return u; // Login bem-sucedido
        } else {
            throw new NegocioException("Senha incorreta.");
        }
    }
    public Usuario buscarUsuarioPorCpf(String cpf) throws NegocioException {
        return this.usuarios.stream()
            .filter(u -> u.getCpf().equals(cpf))
            .findFirst()
            .orElseThrow(() -> new NegocioException("Usuário com CPF " + cpf + " não encontrado."));
    }

    /**
     * Exclui um usuário do sistema, com regras de negócio.
     * (Necessário para o botão Excluir)
     */
    public void excluirUsuario(Usuario ator, Usuario usuarioParaExcluir) throws NegocioException, IOException {
        
        // Regra 1: Ator deve ser admin
        if (!ator.getPerfis().contains(Perfil.ADMINISTRADOR)) {
            throw new NegocioException("Acesso negado: Somente administradores podem excluir usuários.");
        }
        
        // Regra 2: Não pode excluir a si mesmo
        if (usuarioParaExcluir.getCpf().equals(ator.getCpf())) {
            throw new NegocioException("Erro: Você não pode excluir a sua própria conta.");
        }

        // Regra 3: Não pode excluir o último administrador
        if (usuarioParaExcluir.getPerfis().contains(Perfil.ADMINISTRADOR)) {
            long contagemAdmins = usuarios.stream()
                .filter(u -> u.getPerfis().contains(Perfil.ADMINISTRADOR))
                .count();
            
            if (contagemAdmins <= 1) {
                throw new NegocioException("Erro: Não é possível excluir o último administrador do sistema.");
            }
        }

        // Se passou em tudo, remove e salva
        usuarios.remove(usuarioParaExcluir);
        repoUsuarios.salvar(usuarios, ARQUIVO_USUARIOS);
        System.out.println("Usuário excluído: " + usuarioParaExcluir.getNome());
    }
    
    /**
     * Edita os dados de um usuário existente.
     * (Necessário para o botão Editar/Salvar)
     */
    public void editarUsuario(Usuario ator, Usuario usuarioParaEditar, String novoNome, String novoEmail, String novoLogin) throws NegocioException, IOException {
        
        if (!ator.getPerfis().contains(Perfil.ADMINISTRADOR)) {
            throw new NegocioException("Acesso negado: Somente administradores podem editar usuários.");
        }
        
        // Validação de dados únicos (Email e Login)
        for (Usuario u : usuarios) {
            // Se o email novo JÁ EXISTE em outro usuário
            if (u.getEmail().equalsIgnoreCase(novoEmail) && !u.getCpf().equals(usuarioParaEditar.getCpf())) {
                throw new NegocioException("Erro: O novo email '" + novoEmail + "' já pertence a outro usuário.");
            }
            // Se o login novo JÁ EXISTE em outro usuário
            if (u.getLogin().equalsIgnoreCase(novoLogin) && !u.getCpf().equals(usuarioParaEditar.getCpf())) {
                throw new NegocioException("Erro: O novo login '" + novoLogin + "' já pertence a outro usuário.");
            }
        }
        
        // Atualiza os dados no objeto (que está na lista "this.usuarios")
        usuarioParaEditar.setNome(novoNome);
        usuarioParaEditar.setEmail(novoEmail);
        usuarioParaEditar.setLogin(novoLogin); 
        
        // Salva a lista inteira (com o usuário atualizado) no arquivo
        repoUsuarios.salvar(usuarios, ARQUIVO_USUARIOS);
        System.out.println("Usuário editado: " + usuarioParaEditar.getNome());
    }
}

