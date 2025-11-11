package telas.financeiro.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;


public class FolhaPagamento implements Serializable {
    
    private YearMonth mesReferencia;
    private LocalDate dataGeracao;
    private List<ItemFolha> itens;
    private double totalFolha;
    
    
    public FolhaPagamento(YearMonth mesReferencia) {
        this.mesReferencia = mesReferencia;
        this.dataGeracao = LocalDate.now();
        this.itens = new ArrayList<>();
        this.totalFolha = 0.0;
    }
    
    
    public void adicionarItem(ItemFolha item) {
        this.itens.add(item);
        this.totalFolha += item.getSalarioLiquido();
    }
    
    
    public YearMonth getMesReferencia() {
        return mesReferencia;
    }
    
    public LocalDate getDataGeracao() {
        return dataGeracao;
    }
    
    public List<ItemFolha> getItens() {
        return itens;
    }
    
    public double getTotalFolha() {
        return totalFolha;
    }
    
    public int getQuantidadeFuncionarios() {
        return itens.size();
    }
    
    
    public static class ItemFolha implements Serializable {
        private String matricula;
        private String nomeFuncionario;
        private String cargo;
        private String departamento;
        private String regimeContratacao;
        private double salarioBase;
        private double descontos;
        private double beneficios;
        private double salarioLiquido;
        
        public ItemFolha(Funcionario funcionario, double descontos, 
                         double beneficios, double salarioLiquido) {
            this.matricula = funcionario.getMatricula();
            this.nomeFuncionario = funcionario.getNome();
            this.cargo = funcionario.getCargo();
            this.departamento = funcionario.getDepartamento();
            this.regimeContratacao = funcionario.getRegimeContratacao();
            this.salarioBase = funcionario.getSalarioBase();
            this.descontos = descontos;
            this.beneficios = beneficios;
            this.salarioLiquido = salarioLiquido;
        }
        
        
        public String getMatricula() {
            return matricula;
        }
        
        public String getNomeFuncionario() {
            return nomeFuncionario;
        }
        
        public String getCargo() {
            return cargo;
        }
        
        public String getDepartamento() {
            return departamento;
        }
        
        public String getRegimeContratacao() {
            return regimeContratacao;
        }
        
        public double getSalarioBase() {
            return salarioBase;
        }
        
        public double getDescontos() {
            return descontos;
        }
        
        public double getBeneficios() {
            return beneficios;
        }
        
        public double getSalarioLiquido() {
            return salarioLiquido;
        }
    }
}