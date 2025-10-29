/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package telas.recrutamento.controller;

import telas.recrutamento.dao.VagaDAO;
import telas.recrutamento.dao.RecrutadorDAO;
import telas.recrutamento.model.Vaga;
import telas.recrutamento.model.Recrutador;
import telas.recrutamento.exception.RecrutamentoException;
import javax.swing.JOptionPane;
import java.util.List;

public class VagaController {
    private VagaDAO dao;
    private RecrutadorDAO recrutadorDAO;
    
    public VagaController() {
        this.dao = new VagaDAO();
        this.recrutadorDAO = new RecrutadorDAO();
    }
    
    public void cadastrar(String cargo, double salario, String departamento,
                         String requisitos, String regime, String cpfRecrutador) {
        try {
            if (cargo == null || cargo.trim().isEmpty()) {
                throw new RecrutamentoException("Cargo não pode ser vazio!");
            }
            
            if (salario <= 0) {
                throw new RecrutamentoException("Salário deve ser maior que zero!");
            }
            
            Vaga vaga = new Vaga(cargo, salario, departamento, requisitos, regime);
            
            if (cpfRecrutador != null && !cpfRecrutador.trim().isEmpty()) {
                Recrutador recrutador = recrutadorDAO.buscarPorCpf(cpfRecrutador);
                if (recrutador != null) {
                    vaga.setRecrutadorResponsavel(recrutador);
                    recrutador.adicionarVaga(vaga);
                    recrutadorDAO.atualizar(recrutador);
                }
            }
            
            dao.salvar(vaga);
            JOptionPane.showMessageDialog(null, "Vaga cadastrada com sucesso! ID: " + vaga.getIdVaga());
            
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void atualizar(int id, String cargo, double salario, String departamento,
                         String requisitos, String regime) {
        try {
            Vaga vaga = dao.buscarPorId(id);
            if (vaga == null) {
                throw new RecrutamentoException("Vaga não encontrada!");
            }
            
            vaga.setCargo(cargo);
            vaga.setSalario(salario);
            vaga.setDepartamento(departamento);
            vaga.setRequisitos(requisitos);
            vaga.setRegimeContratacao(regime);
            
            dao.atualizar(vaga);
            JOptionPane.showMessageDialog(null, "Vaga atualizada com sucesso!");
            
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Vaga buscar(int id) {
        try {
            Vaga vaga = dao.buscarPorId(id);
            if (vaga == null) {
                throw new RecrutamentoException("Vaga não encontrada!");
            }
            return vaga;
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public List<Vaga> listarTodas() {
        return dao.listarTodas();
    }
    
    public List<Vaga> listarAbertas() {
        return dao.listarPorStatus("Aberta");
    }
    
    public List<Vaga> listarPorRecrutador(String cpfRecrutador) {
        Recrutador rec = recrutadorDAO.buscarPorCpf(cpfRecrutador);
        if (rec != null) {
            return dao.listarPorRecrutador(rec);
        }
        return List.of();
    }
    
    public void fechar(int id) {
        try {
            Vaga vaga = dao.buscarPorId(id);
            if (vaga == null) {
                throw new RecrutamentoException("Vaga não encontrada!");
            }
            vaga.fecharVaga();
            dao.atualizar(vaga);
            JOptionPane.showMessageDialog(null, "Vaga fechada com sucesso!");
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void abrir(int id) {
        try {
            Vaga vaga = dao.buscarPorId(id);
            if (vaga == null) {
                throw new RecrutamentoException("Vaga não encontrada!");
            }
            vaga.abrirVaga();
            dao.atualizar(vaga);
            JOptionPane.showMessageDialog(null, "Vaga aberta com sucesso!");
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
