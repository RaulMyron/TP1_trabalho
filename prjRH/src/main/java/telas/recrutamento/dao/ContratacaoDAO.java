/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package telas.recrutamento.dao;

import telas.recrutamento.model.Contratacao;
import telas.recrutamento.model.Recrutador;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContratacaoDAO {
    private static final String ARQUIVO = "contratacoes.dat";
    private List<Contratacao> contratacoes;
    
    public ContratacaoDAO() {
        contratacoes = new ArrayList<>();
        carregarDados();
    }
    
    public void salvar(Contratacao contratacao) {
        contratacoes.add(contratacao);
        salvarDados();
    }
    
    public Contratacao buscarPorId(int id) {
        for (Contratacao c : contratacoes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }
    
    public List<Contratacao> listarTodas() {
        return new ArrayList<>(contratacoes);
    }
    
    public List<Contratacao> listarPorStatus(String status) {
        return contratacoes.stream()
                .filter(c -> c.getStatusSolicitacao().equals(status))
                .collect(Collectors.toList());
    }
    
    public List<Contratacao> listarPorRecrutador(Recrutador recrutador) {
        return contratacoes.stream()
                .filter(c -> c.getRecrutadorSolicitante().getCpf().equals(recrutador.getCpf()))
                .collect(Collectors.toList());
    }
    
    public void atualizar(Contratacao contratacao) {
        for (int i = 0; i < contratacoes.size(); i++) {
            if (contratacoes.get(i).getId() == contratacao.getId()) {
                contratacoes.set(i, contratacao);
                salvarDados();
                return;
            }
        }
    }
    
    public void atualizarStatus(int id, String status) {
        Contratacao c = buscarPorId(id);
        if (c != null) {
            c.setStatusSolicitacao(status);
            atualizar(c);
        }
    }
    
    public void deletar(int id) {
        contratacoes.removeIf(c -> c.getId() == id);
        salvarDados();
    }
    
    @SuppressWarnings("unchecked")
    private void carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            contratacoes = (List<Contratacao>) ois.readObject();
            if (!contratacoes.isEmpty()) {
                int maxId = contratacoes.stream().mapToInt(Contratacao::getId).max().orElse(0);
                Contratacao.setContadorId(maxId + 1);
            }
        } catch (FileNotFoundException e) {
            contratacoes = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            contratacoes = new ArrayList<>();
        }
    }
    
    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(contratacoes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
