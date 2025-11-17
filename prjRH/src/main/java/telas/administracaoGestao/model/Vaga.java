package telas.administracaoGestao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Vaga implements Serializable { 
    
    private static final long serialVersionUID = 1L;
    private String cargo;
    private double salario;
    private String status;
    
    
    private Usuario recrutadorResponsavel;

    public Vaga(String cargo, double salario) {
        this.cargo = cargo;
        this.salario = salario;
        this.status = "aberta"; 
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