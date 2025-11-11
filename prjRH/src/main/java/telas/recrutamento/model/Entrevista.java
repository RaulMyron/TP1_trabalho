/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package telas.recrutamento.model;

import java.io.Serializable;
import java.util.Date;

public class Entrevista implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String candidaturaId;
    private Date dataHora;
    private String local;
    private String avaliador;
    private double nota;
    private String feedback;
    private boolean realizada;
    private String observacoes;
    
    private static int contadorId = 1;
    
    public Entrevista(String candidaturaId, Date dataHora) {
        this.id = contadorId++;
        this.candidaturaId = candidaturaId;
        this.dataHora = dataHora;
        this.realizada = false;
        this.nota = 0.0;
    }
    
    public void marcarComoRealizada() {
        this.realizada = true;
    }
    
    public boolean validarNota() {
        return nota >= 0.0 && nota <= 10.0;
    }
    
    // GETTERS E SETTERS - COPIE TODOS ABAIXO
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCandidaturaId() {
        return candidaturaId;
    }
    
    public void setCandidaturaId(String candidaturaId) {
        this.candidaturaId = candidaturaId;
    }
    
    public Date getDataHora() {
        return dataHora;
    }
    
    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }
    
    public String getLocal() {
        return local;
    }
    
    public void setLocal(String local) {
        this.local = local;
    }
    
    public String getAvaliador() {
        return avaliador;
    }
    
    public void setAvaliador(String avaliador) {
        this.avaliador = avaliador;
    }
    
    public double getNota() {
        return nota;
    }
    
    public void setNota(double nota) {
        this.nota = nota;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    public boolean isRealizada() {
        return realizada;
    }
    
    public void setRealizada(boolean realizada) {
        this.realizada = realizada;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public static int getContadorId() {
        return contadorId;
    }
    
    public static void setContadorId(int contadorId) {
        Entrevista.contadorId = contadorId;
    }
    
    @Override
    public String toString() {
        return "Entrevista #" + id + " - " + (realizada ? "Realizada" : "Pendente");
    }
}
