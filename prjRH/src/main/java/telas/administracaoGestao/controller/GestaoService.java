package telas.administracaoGestao.controller;

// Os imports agora usam os pacotes do SEU projeto
import telas.administracaoGestao.model.*;
import telas.administracaoGestao.excecoes.NegocioException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// O CONTROLLER
public class GestaoService {
    
    private List<Usuario> usuarios;
    private List<Vaga> vagas;
    
    private static final String ARQUIVO_USUARIOS = "usuarios.dat";
    private static final String ARQUIVO_VAGAS = "vagas.dat";
    
    private RepositorioBase<Usuario> repoUsuarios;
    private RepositorioBase<Vaga> repoVagas;

    public GestaoService() {
        this.repoUsuarios = new RepositorioBase<>();
        this.repoVagas = new RepositorioBase<>();
        
        try {
            this.usuarios = repoUsuarios.carregar(ARQUIVO_USUARIOS);
            this.vagas = repoVagas.carregar(ARQUIVO_VAGAS);
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados. Iniciando com listas vazias.");
            this.usuarios = new ArrayList<>();
            this.vagas = new ArrayList<>();
        }
        
        if (this.usuarios.isEmpty()) {
            System.out.println("Nenhum usuário encontrado. Criando admin padrão.");
            Usuario adminPadrao = new Administrador("Admin", "00000000000", "admin@rh.com", "admin", "admin123");
            this.usuarios.add(adminPadrao);
        }
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
        if (tipo.equals("Gestor")) {
            novoUsuario = new Gestor(nome, cpf, email, login, senha);
        } else {
            novoUsuario = new Administrador(nome, cpf, email, login, senha);
        }
        
        usuarios.add(novoUsuario);
        repoUsuarios.salvar(usuarios, ARQUIVO_USUARIOS); 
    }
    
    // Método para a TELA DE GESTÃO DE VAGAS (GESTOR)
    public void criarVaga(Usuario ator, String cargo, double salario) throws NegocioException, IOException {
        
        if (!ator.getPerfis().contains(Perfil.GESTOR)) {
            throw new NegocioException("Acesso negado: Somente gestores podem criar vagas.");
        }
        
        Vaga novaVaga = new Vaga(cargo, salario);
        vagas.add(novaVaga);
        repoVagas.salvar(vagas, ARQUIVO_VAGAS);
    }
    
    public List<Usuario> listarTodosUsuarios() {
        return this.usuarios;
    }

    public Usuario login(String login, char[] senha) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
