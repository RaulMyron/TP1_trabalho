package telas.prestacaoservico.controller;

import telas.prestacaoservico.model.PrestadorServico;
import telas.prestacaoservico.model.ContratoServico;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GerenciadorDados {
    private static GerenciadorDados instancia;
    private List<PrestadorServico> prestadores;
    private List<ContratoServico> contratos;
    private int proximoIdPrestador = 1;
    private int proximoIdContrato = 1;
    
    private GerenciadorDados() {
        prestadores = new ArrayList<>();
        contratos = new ArrayList<>();
    }
    
    public static GerenciadorDados getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorDados();
        }
        return instancia;
    }
    
    public boolean adicionarPrestador(PrestadorServico prestador) {
        for (PrestadorServico p : prestadores) {
            if (p.getCpfCnpj().equals(prestador.getCpfCnpj())) {
                return false;
            }
        }
        prestador.setId(proximoIdPrestador++);
        prestadores.add(prestador);
        return true;
    }
    
    public boolean editarPrestador(PrestadorServico prestador) {
        for (int i = 0; i < prestadores.size(); i++) {
            if (prestadores.get(i).getId() == prestador.getId()) {
                prestadores.set(i, prestador);
                return true;
            }
        }
        return false;
    }
    
    public boolean excluirPrestador(int id) {
        for (ContratoServico contrato : contratos) {
            if (contrato.getPrestador().getId() == id && contrato.getStatus().equals("Ativo")) {
                return false;
            }
        }
        
        return prestadores.removeIf(p -> p.getId() == id);
    }
    
    public List<PrestadorServico> listarPrestadores() {
        return new ArrayList<>(prestadores);
    }
    
    public PrestadorServico buscarPrestadorPorId(int id) {
        return prestadores.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public List<PrestadorServico> filtrarPrestadoresPorCategoria(String categoria) {
        return prestadores.stream()
                .filter(p -> p.getCategoria().equals(categoria))
                .collect(Collectors.toList());
    }
    
    public List<PrestadorServico> filtrarPrestadoresPorStatus(boolean apenasAtivos) {
        if (apenasAtivos) {
            return prestadores.stream()
                    .filter(PrestadorServico::temContratoAtivo)
                    .collect(Collectors.toList());
        } else {
            return prestadores.stream()
                    .filter(p -> !p.temContratoAtivo())
                    .collect(Collectors.toList());
        }
    }
    
    
    public boolean adicionarContrato(ContratoServico contrato) {
        if (!contrato.validarDatas()) {
            return false;
        }
        
        if (contrato.getPrestador().temContratoAtivoParaServico(contrato.getTipoServico())) {
            return false;
        }
        
        contrato.setId(proximoIdContrato++);
        contrato.atualizarStatus();
        contratos.add(contrato);
        contrato.getPrestador().addContrato(contrato);
        return true;
    }
    
    public boolean editarContrato(ContratoServico contrato) {
        if (!contrato.validarDatas()) {
            return false;
        }
        
        for (int i = 0; i < contratos.size(); i++) {
            if (contratos.get(i).getId() == contrato.getId()) {
                contrato.atualizarStatus();
                contratos.set(i, contrato);
                return true;
            }
        }
        return false;
    }
    
    public boolean excluirContrato(int id) {
        return contratos.removeIf(c -> c.getId() == id);
    }
    
    public List<ContratoServico> listarContratos() {
        for (ContratoServico contrato : contratos) {
            contrato.atualizarStatus();
        }
        return new ArrayList<>(contratos);
    }
    
    public ContratoServico buscarContratoPorId(int id) {
        return contratos.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public List<ContratoServico> filtrarContratosPorStatus(String status) {
        return contratos.stream()
                .filter(c -> {
                    c.atualizarStatus();
                    return c.getStatus().equals(status);
                })
                .collect(Collectors.toList());
    }
    
    public List<ContratoServico> filtrarContratosPorTipoServico(String tipoServico) {
        return contratos.stream()
                .filter(c -> c.getTipoServico().equals(tipoServico))
                .collect(Collectors.toList());
    }
    
    public List<ContratoServico> filtrarContratosProximosVencimento() {
        return contratos.stream()
                .filter(ContratoServico::proximoDoVencimento)
                .collect(Collectors.toList());
    }
    
    public List<ContratoServico> buscarContratosPorPrestador(int prestadorId) {
        return contratos.stream()
                .filter(c -> c.getPrestador().getId() == prestadorId)
                .collect(Collectors.toList());
    }
}