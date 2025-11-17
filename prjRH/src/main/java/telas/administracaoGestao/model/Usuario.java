package telas.administracaoGestao.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Usuario extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    private String login;
    private String senha;
    private List<Perfil> perfil;
    private boolean ativo;

    public Usuario(String nome, String cpf, String email, String login, String senha) {
        super(nome, cpf, email);
        this.login = login;
        this.senha = senha;
        this.perfil= new ArrayList<>();
        this.ativo = true;
    }
    
    public String getLogin() {
        return login;
    }
    public void setLogin(String login){
        this.login = login;
    }
    public List<Perfil> getPerfis() {
        return perfil; 
    }
    public void setSenha(String senha){
        this.senha = senha;
    }
    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    public void addPerfil(Perfil p) {
        if (!this.perfil.contains(p)) {
            this.perfil.add(p);
        }
    }
    public void removePerfil(Perfil p) {
        this.perfil.remove(p);
    }
    
    public boolean autenticar(String senhaFornecida) {
        return this.senha.equals(senhaFornecida);
    }
}