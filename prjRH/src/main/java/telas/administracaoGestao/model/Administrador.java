package telas.administracaoGestao.model;

public class Administrador extends Usuario {
    public Administrador(String nome, String cpf, String email, String login, String senha) {
        super(nome, cpf, email, login, senha);
        addPerfil(Perfil.ADMINISTRADOR); // Define o perfil no construtor
    }
}