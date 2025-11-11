/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package telas.recrutamento.controller;

import telas.recrutamento.dao.EntrevistaDAO;
import telas.recrutamento.model.Entrevista;
import telas.recrutamento.exception.RecrutamentoException;
import javax.swing.JOptionPane;
import java.util.Date;
import java.util.List;

public class EntrevistaController {
    private EntrevistaDAO dao;
    
    public EntrevistaController() {
        this.dao = new EntrevistaDAO();
    }
    
    public void agendar(String candidaturaId, Date dataHora, String local, String avaliador) {
        try {
            if (candidaturaId == null || candidaturaId.trim().isEmpty()) {
                throw new RecrutamentoException("ID da candidatura não pode ser vazio!");
            }
            
            if (dataHora == null) {
                throw new RecrutamentoException("Data/hora não pode ser vazia!");
            }
            
            Entrevista entrevista = new Entrevista(candidaturaId, dataHora);
            entrevista.setLocal(local);
            entrevista.setAvaliador(avaliador);
            
            dao.salvar(entrevista);
            JOptionPane.showMessageDialog(null, "Entrevista agendada com sucesso! ID: " + entrevista.getId());
            
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void registrarNota(int id, double nota, String feedback) {
        try {
            Entrevista entrevista = dao.buscarPorId(id);
            if (entrevista == null) {
                throw new RecrutamentoException("Entrevista não encontrada!");
            }
            
            if (nota < 0 || nota > 10) {
                throw new RecrutamentoException("Nota deve estar entre 0 e 10!");
            }
            
            entrevista.setNota(nota);
            entrevista.setFeedback(feedback);
            entrevista.marcarComoRealizada();
            
            dao.atualizar(entrevista);
            JOptionPane.showMessageDialog(null, "Nota registrada com sucesso!");
            
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Entrevista buscar(int id) {
        try {
            Entrevista entrevista = dao.buscarPorId(id);
            if (entrevista == null) {
                throw new RecrutamentoException("Entrevista não encontrada!");
            }
            return entrevista;
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public List<Entrevista> listarTodas() {
        return dao.listarTodas();
    }
    
    public List<Entrevista> listarPorCandidatura(String candidaturaId) {
        return dao.listarPorCandidatura(candidaturaId);
    }
    
    public void atualizar(int id, Date novaData, String novoLocal, String novoAvaliador) {
        try {
            Entrevista entrevista = dao.buscarPorId(id);
            if (entrevista == null) {
                throw new RecrutamentoException("Entrevista não encontrada!");
            }
            
            entrevista.setDataHora(novaData);
            entrevista.setLocal(novoLocal);
            entrevista.setAvaliador(novoAvaliador);
            
            dao.atualizar(entrevista);
            JOptionPane.showMessageDialog(null, "Entrevista atualizada com sucesso!");
            
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}