package telas.administracaoGestao.model;

import java.io.Serializable;

public class Gestor extends Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    public Gestor(String nome, String cpf, String email, String login, String senha) {
        super(nome, cpf, email, login, senha);
        addPerfil(Perfil.GESTOR); // Define o perfil no construtor
    }

    /**
     * Garante que o perfil GESTOR seja adicionado após deserialização.
     */
    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Adiciona o perfil se não estiver presente
        if (!getPerfis().contains(Perfil.GESTOR)) {
            addPerfil(Perfil.GESTOR);
        }
    }
}