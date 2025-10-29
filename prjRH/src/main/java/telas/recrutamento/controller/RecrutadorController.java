/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package telas.recrutamento.controller;

import telas.recrutamento.dao.RecrutadorDAO;
import telas.recrutamento.model.Recrutador;
import telas.recrutamento.exception.RecrutamentoException;
import javax.swing.JOptionPane;
import java.util.List;

public class RecrutadorController {
    private RecrutadorDAO dao;
    
    public RecrutadorController() {
        this.dao = new RecrutadorDAO();
    }
    
    public void cadastrar(String cpf, String nome, String email, String departamento) {
        try {
            if (cpf == null || cpf.trim().isEmpty()) {
                throw new RecrutamentoException("CPF não pode ser vazio!");
            }
            
            if (dao.buscarPorCpf(cpf) != null) {
                throw new RecrutamentoException("CPF já cadastrado!");
            }
            
            Recrutador recrutador = new Recrutador(cpf, nome, email);
            recrutador.setDepartamento(departamento);
            dao.salvar(recrutador);
            
            JOptionPane.showMessageDialog(null, "Recrutador cadastrado com sucesso!");
            
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void atualizar(String cpf, String novoNome, String novoEmail, String novoDepartamento) {
        try {
            Recrutador recrutador = dao.buscarPorCpf(cpf);
            if (recrutador == null) {
                throw new RecrutamentoException("Recrutador não encontrado!");
            }
            
            recrutador.setNome(novoNome);
            recrutador.setEmail(novoEmail);
            recrutador.setDepartamento(novoDepartamento);
            dao.atualizar(recrutador);
            
            JOptionPane.showMessageDialog(null, "Recrutador atualizado com sucesso!");
            
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Recrutador buscar(String cpf) {
        try {
            Recrutador recrutador = dao.buscarPorCpf(cpf);
            if (recrutador == null) {
                throw new RecrutamentoException("Recrutador não encontrado!");
            }
            return recrutador;
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public List<Recrutador> listarTodos() {
        return dao.listarTodos();
    }
    
    public void deletar(String cpf) {
        try {
            if (dao.buscarPorCpf(cpf) == null) {
                throw new RecrutamentoException("Recrutador não encontrado!");
            }
            dao.deletar(cpf);
            JOptionPane.showMessageDialog(null, "Recrutador deletado com sucesso!");
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
