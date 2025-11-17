package telas.financeiro.controller;

import telas.financeiro.model.Funcionario;
import telas.financeiro.model.RegraSalario;
import telas.financeiro.model.FolhaPagamento;
import telas.financeiro.model.FolhaPagamento.ItemFolha;
import telas.financeiro.exception.FuncionarioException;
import telas.financeiro.exception.FolhaPagamentoException;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class FinanceiroController {
    
    
    private List<Funcionario> funcionarios;
    private RegraSalario regraSalarialAtual;
    private List<FolhaPagamento> historicoFolhas;
    
    
    public FinanceiroController() {
        this.funcionarios = new ArrayList<>();
        this.regraSalarialAtual = new RegraSalario(); 
        this.historicoFolhas = new ArrayList<>();
    }
    
    
    
    
    public void cadastrarFuncionario(Funcionario funcionario) throws FuncionarioException {
        
        if (funcionario.getCpf() == null || funcionario.getCpf().isEmpty()) {
            throw new FuncionarioException("CPF é obrigatório!");
        }
        
        if (funcionario.getMatricula() == null || funcionario.getMatricula().isEmpty()) {
            throw new FuncionarioException("Matrícula é obrigatória!");
        }
        
        
        if (buscarPorCpf(funcionario.getCpf()) != null) {
            throw new FuncionarioException("Já existe um funcionário com este CPF!");
        }
        
        
        if (buscarPorMatricula(funcionario.getMatricula()) != null) {
            throw new FuncionarioException("Já existe um funcionário com esta matrícula!");
        }
        
        funcionarios.add(funcionario);
    }
    
    
    public void atualizarFuncionario(Funcionario funcionario) throws FuncionarioException {
        Funcionario existente = buscarPorMatricula(funcionario.getMatricula());
        
        if (existente == null) {
            throw new FuncionarioException("Funcionário não encontrado!");
        }
        
        
        int indice = funcionarios.indexOf(existente);
        funcionarios.set(indice, funcionario);
    }
    
    
    public void inativarFuncionario(String matricula) throws FuncionarioException {
        Funcionario func = buscarPorMatricula(matricula);
        
        if (func == null) {
            throw new FuncionarioException("Funcionário não encontrado!");
        }
        
        func.setAtivo(false);
    }
    
    
    public Funcionario buscarPorMatricula(String matricula) {
        return funcionarios.stream()
                .filter(f -> f.getMatricula().equals(matricula))
                .findFirst()
                .orElse(null);
    }
    
    
    public Funcionario buscarPorCpf(String cpf) {
        return funcionarios.stream()
                .filter(f -> f.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }
    
    
    public List<Funcionario> listarTodos() {
        return new ArrayList<>(funcionarios);
    }
    
    
    public List<Funcionario> listarAtivos() {
        return funcionarios.stream()
                .filter(Funcionario::isAtivo)
                .collect(Collectors.toList());
    }
    
    
    public List<Funcionario> filtrarPorCargo(String cargo) {
        return funcionarios.stream()
                .filter(f -> f.getCargo().equalsIgnoreCase(cargo))
                .collect(Collectors.toList());
    }
    
    
    public List<Funcionario> filtrarPorDepartamento(String departamento) {
        return funcionarios.stream()
                .filter(f -> f.getDepartamento().equalsIgnoreCase(departamento))
                .collect(Collectors.toList());
    }
    
    
    public List<Funcionario> filtrarPorRegime(String regime) {
        return funcionarios.stream()
                .filter(f -> f.getRegimeContratacao().equalsIgnoreCase(regime))
                .collect(Collectors.toList());
    }
    
    
    
    
    public void atualizarRegraSalarial(RegraSalario novaRegra) {
        this.regraSalarialAtual = novaRegra;
    }
    
    
    public RegraSalario getRegraSalarialAtual() {
        return regraSalarialAtual;
    }
    
   
    public double calcularSalario(Funcionario func) {
        String regime = func.getRegimeContratacao().toUpperCase();
        
        switch (regime) {
            case "CLT":
                return calcularSalarioCLT(func);
            case "ESTÁGIO":
                return calcularSalarioEstagio(func);
            case "PJ":
                return calcularSalarioPJ(func);
            default:
                return 0.0;
        }
    }
    
    private double calcularSalarioCLT(Funcionario func) {
        double salarioBase = func.getSalarioBase();
        double beneficios = regraSalarialAtual.getValorValeTransporte() + 
                           regraSalarialAtual.getValorValeAlimentacao();
        double descontoINSS = salarioBase * (regraSalarialAtual.getPercentualINSS() / 100.0);
        double descontoIRRF = salarioBase * (regraSalarialAtual.getPercentualIRRF() / 100.0);
        return salarioBase + beneficios - descontoINSS - descontoIRRF;
    }
    
    private double calcularSalarioEstagio(Funcionario func) {
        return func.getSalarioBase() + regraSalarialAtual.getValorAuxilioTransporteEstagio();
    }
    
    private double calcularSalarioPJ(Funcionario func) {
        return func.getSalarioBase();
    }
    
    
    
    
    public FolhaPagamento gerarFolhaPagamento(YearMonth mesReferencia) 
            throws FolhaPagamentoException {
        
        
        for (FolhaPagamento folha : historicoFolhas) {
            if (folha.getMesReferencia().equals(mesReferencia)) {
                throw new FolhaPagamentoException(
                    "Já existe uma folha gerada para " + mesReferencia);
            }
        }
        
        FolhaPagamento folha = new FolhaPagamento(mesReferencia);
        
        
        for (Funcionario func : listarAtivos()) {
            double salarioBase = func.getSalarioBase();
            double beneficios = 0.0;
            double descontos = 0.0;
            double salarioLiquido = 0.0;
            
            String regime = func.getRegimeContratacao().trim().toUpperCase();
            
            switch (regime) {
                case "CLT":
                    beneficios = regraSalarialAtual.getValorValeTransporte() + 
                                regraSalarialAtual.getValorValeAlimentacao();
                    descontos = salarioBase * (regraSalarialAtual.getPercentualINSS() / 100.0) +
                               salarioBase * (regraSalarialAtual.getPercentualIRRF() / 100.0);
                    salarioLiquido = salarioBase + beneficios - descontos;
                    break;
                    
                case "ESTÁGIO":
                    beneficios = regraSalarialAtual.getValorAuxilioTransporteEstagio();
                    salarioLiquido = salarioBase + beneficios;
                    break;
                    
                case "PJ":
                    salarioLiquido = salarioBase;
                    break;
            }
            
            ItemFolha item = new ItemFolha(func, descontos, beneficios, salarioLiquido);
            folha.adicionarItem(item);
        }
        
        historicoFolhas.add(folha);
        return folha;
    }
    
    
    
    public List<FolhaPagamento> getHistoricoFolhas() {
        return new ArrayList<>(historicoFolhas);
    }
    
    
    public FolhaPagamento buscarFolhaPorMes(YearMonth mesReferencia) {
        return historicoFolhas.stream()
                .filter(f -> f.getMesReferencia().equals(mesReferencia))
                .findFirst()
                .orElse(null);
    }   
}