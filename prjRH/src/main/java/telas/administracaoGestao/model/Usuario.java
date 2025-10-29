package telas.administracaoGestao.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Usuario extends Pessoa implements Serializable {
    private String login;
    private String senha;
    private List<Perfil> perfis; 

    public Usuario(String nome, String cpf, String email, String login, String senha) {
        super(nome, cpf, email);
        this.login = login;
        this.senha = senha;
        this.perfis = new ArrayList<>();
    }
    
    public String getLogin() { return login; }
    public List<Perfil> getPerfis() { return perfis; }
    
    public void addPerfil(Perfil p) {
        if (!this.perfis.contains(p)) {
            this.perfis.add(p);
        }
    }
    
    public void removePerfil(Perfil p) {
        this.perfis.remove(p);
    }
    
    public boolean autenticar(String senhaFornecida) {
        return this.senha.equals(senhaFornecida);
    }
}