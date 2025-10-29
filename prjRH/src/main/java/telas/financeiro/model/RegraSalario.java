package telas.financeiro.model;

import java.io.Serializable;

/**
 * MODEL - Representa as regras de cálculo salarial
 */
public class RegraSalario implements Serializable {
    
    // ATRIBUTOS - Valores configuráveis
    private double percentualINSS;
    private double percentualIRRF;
    private double valorValeTransporte;
    private double valorValeAlimentacao;
    private double valorAuxilioTransporteEstagio;
    
    // CONSTRUTOR PADRÃO
    public RegraSalario() {
        this.percentualINSS = 11.0;
        this.percentualIRRF = 7.5;
        this.valorValeTransporte = 200.0;
        this.valorValeAlimentacao = 400.0;
        this.valorAuxilioTransporteEstagio = 150.0;
    }
    
    // CONSTRUTOR COMPLETO
    public RegraSalario(double percentualINSS, double percentualIRRF,
                        double valorValeTransporte, double valorValeAlimentacao,
                        double valorAuxilioTransporteEstagio) {
        this.percentualINSS = percentualINSS;
        this.percentualIRRF = percentualIRRF;
        this.valorValeTransporte = valorValeTransporte;
        this.valorValeAlimentacao = valorValeAlimentacao;
        this.valorAuxilioTransporteEstagio = valorAuxilioTransporteEstagio;
    }
    
    // GETTERS E SETTERS
    public double getPercentualINSS() {
        return percentualINSS;
    }
    
    public void setPercentualINSS(double percentualINSS) {
        this.percentualINSS = percentualINSS;
    }
    
    public double getPercentualIRRF() {
        return percentualIRRF;
    }
    
    public void setPercentualIRRF(double percentualIRRF) {
        this.percentualIRRF = percentualIRRF;
    }
    
    public double getValorValeTransporte() {
        return valorValeTransporte;
    }
    
    public void setValorValeTransporte(double valorValeTransporte) {
        this.valorValeTransporte = valorValeTransporte;
    }
    
    public double getValorValeAlimentacao() {
        return valorValeAlimentacao;
    }
    
    public void setValorValeAlimentacao(double valorValeAlimentacao) {
        this.valorValeAlimentacao = valorValeAlimentacao;
    }
    
    public double getValorAuxilioTransporteEstagio() {
        return valorAuxilioTransporteEstagio;
    }
    
    public void setValorAuxilioTransporteEstagio(double valorAuxilioTransporteEstagio) {
        this.valorAuxilioTransporteEstagio = valorAuxilioTransporteEstagio;
    }
}