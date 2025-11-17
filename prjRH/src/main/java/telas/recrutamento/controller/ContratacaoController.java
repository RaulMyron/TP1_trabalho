package telas.recrutamento.controller;

import telas.recrutamento.dao.ContratacaoDAO;
import telas.recrutamento.dao.RecrutadorDAO;
import telas.recrutamento.model.Contratacao;
import telas.recrutamento.model.Recrutador;
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
    
    public void solicitar(String candidaturaId, String cpfRecrutador, 
                        String regime, double salario, Date dataInicio, 
                        String justificativa) throws Exception {
        
        System.out.println("=== DEBUG CONTROLLER ===");
        System.out.println("CPF Recrutador: " + cpfRecrutador);
        
        Recrutador recrutador = recrutadorDAO.buscarPorCpf(cpfRecrutador);
        
        if (recrutador == null) {
            System.out.println("DEBUG: Buscando no GestaoService...");
            try {
                telas.administracaoGestao.controller.GestaoService gestao = 
                    telas.administracaoGestao.controller.GestaoService.getInstance();
                
                telas.administracaoGestao.model.Usuario usuario = gestao.buscarUsuario(cpfRecrutador);
                
                if (usuario != null) {
                    System.out.println("DEBUG: Usuário encontrado: " + usuario.getNome());
                    
                    // Cria Recrutador a partir do Usuario
                    recrutador = new Recrutador(
                        usuario.getNome(),
                        usuario.getCpf(),
                        usuario.getEmail(),
                        cpfRecrutador,  // matricula
                        "senha_temp"    // senha temporária
                    );
                    System.out.println("DEBUG: Recrutador criado a partir do Usuario!");
                }
            } catch (Exception e) {
                System.out.println("DEBUG: Erro no GestaoService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        if (recrutador == null) {
            throw new Exception("Recrutador não encontrado! CPF: " + cpfRecrutador);
        }
        
        if (regime == null || regime.isEmpty()) {
            throw new Exception("Regime de contratação deve ser informado!");
        }
        
        if (salario <= 0) {
            throw new Exception("Salário deve ser maior que zero!");
        }
        
        if (dataInicio == null) {
            throw new Exception("Data de início deve ser informada!");
        }
        
        if (justificativa == null || justificativa.trim().isEmpty()) {
            throw new Exception("Justificativa deve ser informada!");
        }
        
        System.out.println("DEBUG: Criando contratação...");
        Contratacao contratacao = new Contratacao(candidaturaId, recrutador);
        
        contratacao.setRegimeContratacao(regime);
        contratacao.setSalarioProposto(salario);
        contratacao.setDataInicioProposta(dataInicio);
        contratacao.setJustificativa(justificativa);
        
        dao.salvar(contratacao);
        System.out.println("DEBUG: Contratação salva com sucesso!");
        
        JOptionPane.showMessageDialog(null, 
            "Solicitação de contratação enviada com sucesso!\n" +
            "ID: " + contratacao.getId() + "\n" +
            "Aguardando autorização do Gestor.",
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void autorizar(int id, String gestor, String parecer) {
        try {
            Contratacao contratacao = dao.buscarPorId(id);
            if (contratacao == null) {
                throw new Exception("Contratação não encontrada!");
            }
            
            if (!contratacao.isPendente()) {
                throw new Exception("Apenas contratações pendentes podem ser autorizadas!");
            }
            
            contratacao.autorizar(gestor, parecer);
            dao.atualizar(contratacao);
            JOptionPane.showMessageDialog(null, "Contratação autorizada com sucesso!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void negar(int id, String gestor, String parecer) {
        try {
            Contratacao contratacao = dao.buscarPorId(id);
            if (contratacao == null) {
                throw new Exception("Contratação não encontrada!");
            }
            
            contratacao.negar(gestor, parecer);
            dao.atualizar(contratacao);
            JOptionPane.showMessageDialog(null, "Contratação negada!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void efetivar(int id) {
        try {
            Contratacao contratacao = dao.buscarPorId(id);
            if (contratacao == null) {
                throw new Exception("Contratação não encontrada!");
            }
            
            if (!contratacao.isAutorizada()) {
                throw new Exception("Apenas contratações autorizadas podem ser efetivadas!");
            }
            
            contratacao.efetivar();
            dao.atualizar(contratacao);
            JOptionPane.showMessageDialog(null, "Contratação efetivada com sucesso!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Contratacao buscar(int id) {
        try {
            return dao.buscarPorId(id);
        } catch (Exception e) {
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