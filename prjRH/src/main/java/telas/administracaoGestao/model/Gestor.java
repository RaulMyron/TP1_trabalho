<<<<<<< HEAD
package telas.administracaoGestao.model;

public class Gestor extends Usuario {
    public Gestor(String nome, String cpf, String email, String login, String senha) {
        super(nome, cpf, email, login, senha);
        addPerfil(Perfil.GESTOR); // Define o perfil no construtor
    }
=======
package telas.administracaoGestao.model;

import java.io.Serializable;

public class Gestor extends Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    public Gestor(String nome, String cpf, String email, String login, String senha) {
        super(nome, cpf, email, login, senha);
        addPerfil(Perfil.GESTOR); // Define o perfil no construtor
    }
>>>>>>> 2e686e0 (nati mudanças)
}