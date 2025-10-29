/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package telas.recrutamento.model;

import java.io.Serializable;
import java.util.Date;

public class Contratacao implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String candidaturaId;
    private Recrutador recrutadorSolicitante;
    private String regimeContratacao;
    private double salarioProposto;
    private Date dataInicioProposta;
    private Date dataSolicitacao;
    private String statusSolicitacao; // "Pendente", "Autorizada", "Negada", "Efetivada"
    private String gestorAutorizador;
    private Date dataAutorizacao;
    private String justificativa;
    private String parecerGestor;
    private Date dataContratacaoEfetiva;
    
    private static int contadorId = 1;
    
    public Contratacao(String candidaturaId, Recrutador recrutador) {
        this.id = contadorId++;
        this.candidaturaId = candidaturaId;
        this.recrutadorSolicitante = recrutador;
        this.dataSolicitacao = new Date();
        this.statusSolicitacao = "Pendente";
    }
    
    public boolean isPendente() {
        return "Pendente".equals(statusSolicitacao);
    }
    
    public boolean isAutorizada() {
        return "Autorizada".equals(statusSolicitacao);
    }
    
    public boolean isNegada() {
        return "Negada".equals(statusSolicitacao);
    }
    
    public void autorizar(String gestor, String parecer) {
        this.statusSolicitacao = "Autorizada";
        this.gestorAutorizador = gestor;
        this.parecerGestor = parecer;
        this.dataAutorizacao = new Date();
    }
    
    public void negar(String gestor, String parecer) {
        this.statusSolicitacao = "Negada";
        this.gestorAutorizador = gestor;
        this.parecerGestor = parecer;
        this.dataAutorizacao = new Date();
    }
    
    public void efetivar() {
        if (isAutorizada()) {
            this.statusSolicitacao = "Efetivada";
            this.dataContratacaoEfetiva = new Date();
        }
    }
    
    public boolean validarSolicitacao() {
        return salarioProposto > 0 && 
               regimeContratacao != null && 
               !regimeContratacao.isEmpty();
    }
    
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
    
    public Recrutador getRecrutadorSolicitante() {
        return recrutadorSolicitante;
    }
    
    public void setRecrutadorSolicitante(Recrutador recrutadorSolicitante) {
        this.recrutadorSolicitante = recrutadorSolicitante;
    }
    
    public String getRegimeContratacao() {
        return regimeContratacao;
    }
    
    public void setRegimeContratacao(String regimeContratacao) {
        this.regimeContratacao = regimeContratacao;
    }
    
    public double getSalarioProposto() {
        return salarioProposto;
    }
    
    public void setSalarioProposto(double salarioProposto) {
        this.salarioProposto = salarioProposto;
    }
    
    public Date getDataInicioProposta() {
        return dataInicioProposta;
    }
    
    public void setDataInicioProposta(Date dataInicioProposta) {
        this.dataInicioProposta = dataInicioProposta;
    }
    
    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }
    
    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }
    
    public String getStatusSolicitacao() {
        return statusSolicitacao;
    }
    
    public void setStatusSolicitacao(String statusSolicitacao) {
        this.statusSolicitacao = statusSolicitacao;
    }
    
    public String getGestorAutorizador() {
        return gestorAutorizador;
    }
    
    public void setGestorAutorizador(String gestorAutorizador) {
        this.gestorAutorizador = gestorAutorizador;
    }
    
    public Date getDataAutorizacao() {
        return dataAutorizacao;
    }
    
    public void setDataAutorizacao(Date dataAutorizacao) {
        this.dataAutorizacao = dataAutorizacao;
    }
    
    public String getJustificativa() {
        return justificativa;
    }
    
    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
    
    public String getParecerGestor() {
        return parecerGestor;
    }
    
    public void setParecerGestor(String parecerGestor) {
        this.parecerGestor = parecerGestor;
    }
    
    public Date getDataContratacaoEfetiva() {
        return dataContratacaoEfetiva;
    }
    
    public void setDataContratacaoEfetiva(Date dataContratacaoEfetiva) {
        this.dataContratacaoEfetiva = dataContratacaoEfetiva;
    }
    
    public static int getContadorId() {
        return contadorId;
    }
    
    public static void setContadorId(int contadorId) {
        Contratacao.contadorId = contadorId;
    }
    
    @Override
    public String toString() {
        return "Contratação #" + id + " - " + statusSolicitacao;
    }
}
