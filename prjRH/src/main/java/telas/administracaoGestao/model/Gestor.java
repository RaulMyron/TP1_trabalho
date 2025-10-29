package telas.administracaoGestao.model;

public class Gestor extends Usuario {
    public Gestor(String nome, String cpf, String email, String login, String senha) {
        super(nome, cpf, email, login, senha);
        addPerfil(Perfil.GESTOR); // Define o perfil no construtor
    }
}