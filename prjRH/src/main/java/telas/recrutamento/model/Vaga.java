/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package telas.recrutamento.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Vaga implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int idVaga;
    private String cargo;
    private double salario;
    private String departamento;
    private String requisitos;
    private String status;
    private String regimeContratacao;
    private Date dataAbertura;
    private Date dataFechamento;
    private Recrutador recrutadorResponsavel;
    private List<String> candidaturasIds;
    
    private static int contadorId = 1;
    
    public Vaga(String cargo, double salario, String departamento, 
                String requisitos, String regimeContratacao) {
        this.idVaga = contadorId++;
        this.cargo = cargo;
        this.salario = salario;
        this.departamento = departamento;
        this.requisitos = requisitos;
        this.regimeContratacao = regimeContratacao;
        this.status = "Aberta";
        this.dataAbertura = new Date();
        this.candidaturasIds = new ArrayList<>();
    }
    
    public boolean isAberta() {
        return "Aberta".equals(status);
    }
    
    public void fecharVaga() {
        this.status = "Fechada";
        this.dataFechamento = new Date();
    }
    
    public void abrirVaga() {
        this.status = "Aberta";
        this.dataFechamento = null;
    }
    
    public void adicionarCandidatura(String candidaturaId) {
        if (!candidaturasIds.contains(candidaturaId)) {
            candidaturasIds.add(candidaturaId);
        }
    }
    
    // GETTERS E SETTERS - COPIE TODOS ABAIXO
    public int getIdVaga() {
        return idVaga;
    }
    
    public void setIdVaga(int idVaga) {
        this.idVaga = idVaga;
    }
    
    public String getCargo() {
        return cargo;
    }
    
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    public double getSalario() {
        return salario;
    }
    
    public void setSalario(double salario) {
        this.salario = salario;
    }
    
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public String getRequisitos() {
        return requisitos;
    }
    
    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRegimeContratacao() {
        return regimeContratacao;
    }
    
    public void setRegimeContratacao(String regimeContratacao) {
        this.regimeContratacao = regimeContratacao;
    }
    
    public Date getDataAbertura() {
        return dataAbertura;
    }
    
    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }
    
    public Date getDataFechamento() {
        return dataFechamento;
    }
    
    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }
    
    public Recrutador getRecrutadorResponsavel() {
        return recrutadorResponsavel;
    }
    
    public void setRecrutadorResponsavel(Recrutador recrutadorResponsavel) {
        this.recrutadorResponsavel = recrutadorResponsavel;
    }
    
    public List<String> getCandidaturasIds() {
        return candidaturasIds;
    }
    
    public void setCandidaturasIds(List<String> candidaturasIds) {
        this.candidaturasIds = candidaturasIds;
    }
    
    public static int getContadorId() {
        return contadorId;
    }
    
    public static void setContadorId(int contadorId) {
        Vaga.contadorId = contadorId;
    }
    
    @Override
    public String toString() {
        return "ID: " + idVaga + " - " + cargo + " (" + status + ")";
    }
}