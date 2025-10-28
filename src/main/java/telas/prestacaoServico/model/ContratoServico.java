package telas.prestacaoservico.model;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ContratoServico {
    private int id;
    private PrestadorServico prestador;
    private String tipoServico;
    private double valor;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String status;
    private String observacoes;
    
    public ContratoServico() {
    }
    
    public ContratoServico(int id, PrestadorServico prestador, String tipoServico,
                          double valor, LocalDate dataInicio, LocalDate dataFim,
                          String status, String observacoes) {
        this.id = id;
        this.prestador = prestador;
        this.tipoServico = tipoServico;
        this.valor = valor;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.observacoes = observacoes;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public PrestadorServico getPrestador() {
        return prestador;
    }
    
    public void setPrestador(PrestadorServico prestador) {
        this.prestador = prestador;
    }
    
    public String getTipoServico() {
        return tipoServico;
    }
    
    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }
    
    public double getValor() {
        return valor;
    }
    
    public void setValor(double valor) {
        this.valor = valor;
    }
    
    public LocalDate getDataInicio() {
        return dataInicio;
    }
    
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }
    
    public LocalDate getDataFim() {
        return dataFim;
    }
    
    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public void atualizarStatus() {
        LocalDate hoje = LocalDate.now();
        
        if (hoje.isBefore(dataInicio)) {
            this.status = "Pendente de Início";
        } else if (hoje.isAfter(dataFim)) {
            this.status = "Encerrado";
        } else {
            long diasParaVencimento = ChronoUnit.DAYS.between(hoje, dataFim);
            if (diasParaVencimento <= 30 && diasParaVencimento > 0) {
                this.status = "Pendente de Renovação";
            } else {
                this.status = "Ativo";
            }
        }
    }
    
    public boolean proximoDoVencimento() {
        LocalDate hoje = LocalDate.now();
        long diasParaVencimento = ChronoUnit.DAYS.between(hoje, dataFim);
        return diasParaVencimento <= 30 && diasParaVencimento > 0;
    }
    
    public long diasRestantes() {
        LocalDate hoje = LocalDate.now();
        return ChronoUnit.DAYS.between(hoje, dataFim);
    }
    
    public boolean validarDatas() {
        if (dataInicio == null || dataFim == null) {
            return false;
        }
        return dataFim.isAfter(dataInicio);
    }
    
    @Override
    public String toString() {
        return "Contrato #" + id + " - " + tipoServico + " - " + status;
    }
}