package telas.administracaoGestao.model;

import java.io.Serializable;

public class Vaga implements Serializable {
    private String cargo;
    private double salario;
    private String status; 

    public Vaga(String cargo, double salario) {
        this.cargo = cargo;
        this.salario = salario;
        this.status = "aberta";
    }
    
    public String getCargo() { return cargo; }
}