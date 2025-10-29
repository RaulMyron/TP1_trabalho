/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package telas.recrutamento.controller;


import telas.recrutamento.dao.ContratacaoDAO;
import telas.recrutamento.dao.RecrutadorDAO;
import telas.recrutamento.model.Contratacao;
import telas.recrutamento.model.Recrutador;
import telas.recrutamento.exception.RecrutamentoException;
import javax.swing.JOptionPane;
import java.util.Date;
import java.util.List;

public class ContratacaoController {
    private ContratacaoDAO dao;
    private RecrutadorDAO recrutadorDAO;
    
    public ContratacaoController() {
        this.dao = new ContratacaoDAO();
        this.recrutadorDAO = new RecrutadorDAO();
    }
    
    public void solicitar(String candidaturaId, String cpfRecrutador, String regime,
                         double salario, Date dataInicio, String justificativa) {
        try {
            if (candidaturaId == null || candidaturaId.trim().isEmpty()) {
                throw new RecrutamentoException("ID da candidatura não pode ser vazio!");
            }
            
            Recrutador recrutador = recrutadorDAO.buscarPorCpf(cpfRecrutador);
            if (recrutador == null) {
                throw new RecrutamentoException("Recrutador não encontrado!");
            }
            
            if (salario <= 0) {
                throw new RecrutamentoException("Salário deve ser maior que zero!");
            }
            
            Contratacao contratacao = new Contratacao(candidaturaId, recrutador);
            contratacao.setRegimeContratacao(regime);
            contratacao.setSalarioProposto(salario);
            contratacao.setDataInicioProposta(dataInicio);
            contratacao.setJustificativa(justificativa);
            
            dao.salvar(contratacao);
            JOptionPane.showMessageDialog(null, "Contratação solicitada com sucesso! ID: " + contratacao.getId());
            
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void autorizar(int id, String gestor, String parecer) {
        try {
            Contratacao contratacao = dao.buscarPorId(id);
            if (contratacao == null) {
                throw new RecrutamentoException("Contratação não encontrada!");
            }
            
            if (!contratacao.isPendente()) {
                throw new RecrutamentoException("Apenas contratações pendentes podem ser autorizadas!");
            }
            
            contratacao.autorizar(gestor, parecer);
            dao.atualizar(contratacao);
            JOptionPane.showMessageDialog(null, "Contratação autorizada com sucesso!");
            
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void negar(int id, String gestor, String parecer) {
        try {
            Contratacao contratacao = dao.buscarPorId(id);
            if (contratacao == null) {
                throw new RecrutamentoException("Contratação não encontrada!");
            }
            
            contratacao.negar(gestor, parecer);
            dao.atualizar(contratacao);
            JOptionPane.showMessageDialog(null, "Contratação negada!");
            
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void efetivar(int id) {
        try {
            Contratacao contratacao = dao.buscarPorId(id);
            if (contratacao == null) {
                throw new RecrutamentoException("Contratação não encontrada!");
            }
            
            if (!contratacao.isAutorizada()) {
                throw new RecrutamentoException("Apenas contratações autorizadas podem ser efetivadas!");
            }
            
            contratacao.efetivar();
            dao.atualizar(contratacao);
            JOptionPane.showMessageDialog(null, "Contratação efetivada com sucesso!");
            
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Contratacao buscar(int id) {
        try {
            Contratacao contratacao = dao.buscarPorId(id);
            if (contratacao == null) {
                throw new RecrutamentoException("Contratação não encontrada!");
            }
            return contratacao;
        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public List<Contratacao> listarTodas() {
        return dao.listarTodas();
    }
    
    public List<Contratacao> listarPendentes() {
        return dao.listarPorStatus("Pendente");
    }
    
    public List<Contratacao> listarAutorizadas() {
        return dao.listarPorStatus("Autorizada");
    }
    
    public List<Contratacao> listarPorRecrutador(String cpfRecrutador) {
        Recrutador rec = recrutadorDAO.buscarPorCpf(cpfRecrutador);
        if (rec != null) {
            return dao.listarPorRecrutador(rec);
        }
        return List.of();
    }
}
