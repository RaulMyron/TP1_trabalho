/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package telas.recrutamento.dao;

import telas.recrutamento.model.Vaga;
import telas.recrutamento.model.Recrutador;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VagaDAO {
    private static final String ARQUIVO = "vagas.dat";
    private List<Vaga> vagas;
    
    public VagaDAO() {
        vagas = new ArrayList<>();
        carregarDados();
    }
    
    public void salvar(Vaga vaga) {
        vagas.add(vaga);
        salvarDados();
    }
    
    public Vaga buscarPorId(int id) {
        for (Vaga v : vagas) {
            if (v.getIdVaga() == id) {
                return v;
            }
        }
        return null;
    }
    
    public List<Vaga> listarTodas() {
        return new ArrayList<>(vagas);
    }
    
    public List<Vaga> listarPorStatus(String status) {
        return vagas.stream()
                .filter(v -> v.getStatus().equals(status))
                .collect(Collectors.toList());
    }
    
    public List<Vaga> listarPorRecrutador(Recrutador recrutador) {
        return vagas.stream()
                .filter(v -> v.getRecrutadorResponsavel() != null && 
                            v.getRecrutadorResponsavel().getCpf().equals(recrutador.getCpf()))
                .collect(Collectors.toList());
    }
    
    public void atualizar(Vaga vaga) {
        for (int i = 0; i < vagas.size(); i++) {
            if (vagas.get(i).getIdVaga() == vaga.getIdVaga()) {
                vagas.set(i, vaga);
                salvarDados();
                return;
            }
        }
    }
    
    public void deletar(int id) {
        vagas.removeIf(v -> v.getIdVaga() == id);
        salvarDados();
    }
    
    @SuppressWarnings("unchecked")
    private void carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            vagas = (List<Vaga>) ois.readObject();
            // Restaurar contador de IDs
            if (!vagas.isEmpty()) {
                int maxId = vagas.stream().mapToInt(Vaga::getIdVaga).max().orElse(0);
                Vaga.setContadorId(maxId + 1);
            }
        } catch (FileNotFoundException e) {
            vagas = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            vagas = new ArrayList<>();
        }
    }
    
    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(vagas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
