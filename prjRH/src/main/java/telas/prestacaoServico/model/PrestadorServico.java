package telas.prestacaoservico.model;

import java.util.ArrayList;
import java.util.List;

public class PrestadorServico {
    private int id;
    private String nome;
    private String tipo;
    private String cpfCnpj;
    private String contato;
    private String email;
    private String endereco;
    private String categoria;
    private List<ContratoServico> contratos;
    
    public PrestadorServico() {
        this.contratos = new ArrayList<>();
    }
    
    public PrestadorServico(int id, String nome, String tipo, String cpfCnpj, 
                           String contato, String email, String endereco, String categoria) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.cpfCnpj = cpfCnpj;
        this.contato = contato;
        this.email = email;
        this.endereco = endereco;
        this.categoria = categoria;
        this.contratos = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getCpfCnpj() {
        return cpfCnpj;
    }
    
    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }
    
    public String getContato() {
        return contato;
    }
    
    public void setContato(String contato) {
        this.contato = contato;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public List<ContratoServico> getContratos() {
        return contratos;
    }
    
    public void setContratos(List<ContratoServico> contratos) {
        this.contratos = contratos;
    }
    
    public void addContrato(ContratoServico contrato) {
        this.contratos.add(contrato);
    }
    
    public boolean temContratoAtivo() {
        for (ContratoServico contrato : contratos) {
            if (contrato.getStatus().equals("Ativo")) {
                return true;
            }
        }
        return false;
    }
    public boolean temContratoAtivoParaServico(String tipoServico) {
        for (ContratoServico contrato : contratos) {
            if (contrato.getStatus().equals("Ativo") && 
                contrato.getTipoServico().equals(tipoServico)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return id + " - " + nome + " (" + cpfCnpj + ")";
    }
    
    public static boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        
        if (cpf.length() != 11) return false;
        
        if (cpf.matches("(\\d)\\1{10}")) return false;
        
        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) primeiroDigito = 0;
            
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) segundoDigito = 0;
            
            return Character.getNumericValue(cpf.charAt(9)) == primeiroDigito &&
                   Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean validarCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");
        
        if (cnpj.length() != 14) return false;
        
        if (cnpj.matches("(\\d)\\1{13}")) return false;
        
        try {
            int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso1[i];
            }
            int primeiroDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);
            
            int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            soma = 0;
            for (int i = 0; i < 13; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso2[i];
            }
            int segundoDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);
            
            return Character.getNumericValue(cnpj.charAt(12)) == primeiroDigito &&
                   Character.getNumericValue(cnpj.charAt(13)) == segundoDigito;
        } catch (Exception e) {
            return false;
        }
    }
}