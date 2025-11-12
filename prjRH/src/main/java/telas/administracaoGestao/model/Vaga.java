package telas.administracaoGestao.model;

import java.io.Serializable;

public class Vaga implements Serializable {
    private String cargo;
    private double salario;
    private String status;
    
    
    private Usuario recrutadorResponsavel;

    public Vaga(String cargo, double salario) {
        this.cargo = cargo;
        this.salario = salario;
        this.status = "aberta"; // Ótimo valor padrão!
    }
    
    // GETTERS E SETTERS 

    public String getCargo() { 
        return cargo; 
    }
    
    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Usuario getRecrutadorResponsavel() {
        return recrutadorResponsavel;
    }

    public void setRecrutadorResponsavel(Usuario recrutadorResponsavel) {
        this.recrutadorResponsavel = recrutadorResponsavel;
    }
}