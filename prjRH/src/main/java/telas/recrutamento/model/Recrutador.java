
package telas.recrutamento.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recrutador implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String cpf;
    private String nome;
    private String email;
    private String departamento;
    private Date dataContratacao;
    private List<Vaga> vagasResponsaveis;
    
    public Recrutador(String cpf, String nome, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.vagasResponsaveis = new ArrayList<>();
        this.dataContratacao = new Date();
    }
    
    public void adicionarVaga(Vaga vaga) {
        if (!vagasResponsaveis.contains(vaga)) {
            vagasResponsaveis.add(vaga);
            vaga.setRecrutadorResponsavel(this);
        }
    }
    
    public void removerVaga(Vaga vaga) {
        vagasResponsaveis.remove(vaga);
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public Date getDataContratacao() {
        return dataContratacao;
    }
    
    public void setDataContratacao(Date dataContratacao) {
        this.dataContratacao = dataContratacao;
    }
    
    public List<Vaga> getVagasResponsaveis() {
        return vagasResponsaveis;
    }
    
    public void setVagasResponsaveis(List<Vaga> vagasResponsaveis) {
        this.vagasResponsaveis = vagasResponsaveis;
    }
    
    @Override
    public String toString() {
        return nome + " - " + cpf;
    }
}