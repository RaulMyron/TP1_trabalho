package telas.financeiro.model;

import java.io.Serializable;
import java.time.LocalDate;


public class Funcionario implements Serializable {
    
    
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String endereco;
    
    
    private String matricula;
    private String cargo;
    private String departamento;
    private double salarioBase;
    private LocalDate dataAdmissao;
    private String regimeContratacao; 
    private boolean ativo;
    
    
    public Funcionario() {
        this.ativo = true;
    }
    
    
    public Funcionario(String nome, String cpf, String email, String telefone,
                       String matricula, String cargo, String departamento,
                       double salarioBase, LocalDate dataAdmissao, 
                       String regimeContratacao) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.matricula = matricula;
        this.cargo = cargo;
        this.departamento = departamento;
        this.salarioBase = salarioBase;
        this.dataAdmissao = dataAdmissao;
        this.regimeContratacao = regimeContratacao;
        this.ativo = true;
    }
    
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String getMatricula() {
        return matricula;
    }
    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    public String getCargo() {
        return cargo;
    }
    
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public double getSalarioBase() {
        return salarioBase;
    }
    
    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }
    
    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }
    
    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }
    
    public String getRegimeContratacao() {
        return regimeContratacao;
    }
    
    public void setRegimeContratacao(String regimeContratacao) {
        this.regimeContratacao = regimeContratacao;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    @Override
    public String toString() {
        return "Funcionario{" +
                "matricula='" + matricula + '\'' +
                ", nome='" + nome + '\'' +
                ", cargo='" + cargo + '\'' +
                ", departamento='" + departamento + '\'' +
                ", salarioBase=" + salarioBase +
                ", regime='" + regimeContratacao + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}