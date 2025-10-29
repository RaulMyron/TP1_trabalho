/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package telas.recrutamento.dao;

import telas.recrutamento.model.Entrevista;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntrevistaDAO {
    private static final String ARQUIVO = "entrevistas.dat";
    private List<Entrevista> entrevistas;
    
    public EntrevistaDAO() {
        entrevistas = new ArrayList<>();
        carregarDados();
    }
    
    public void salvar(Entrevista entrevista) {
        entrevistas.add(entrevista);
        salvarDados();
    }
    
    public Entrevista buscarPorId(int id) {
        for (Entrevista e : entrevistas) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }
    
    public List<Entrevista> listarTodas() {
        return new ArrayList<>(entrevistas);
    }
    
    public List<Entrevista> listarPorCandidatura(String candidaturaId) {
        return entrevistas.stream()
                .filter(e -> e.getCandidaturaId().equals(candidaturaId))
                .collect(Collectors.toList());
    }
    
    public void atualizar(Entrevista entrevista) {
        for (int i = 0; i < entrevistas.size(); i++) {
            if (entrevistas.get(i).getId() == entrevista.getId()) {
                entrevistas.set(i, entrevista);
                salvarDados();
                return;
            }
        }
    }
    
    public void deletar(int id) {
        entrevistas.removeIf(e -> e.getId() == id);
        salvarDados();
    }
    
    @SuppressWarnings("unchecked")
    private void carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            entrevistas = (List<Entrevista>) ois.readObject();
            if (!entrevistas.isEmpty()) {
                int maxId = entrevistas.stream().mapToInt(Entrevista::getId).max().orElse(0);
                Entrevista.setContadorId(maxId + 1);
            }
        } catch (FileNotFoundException e) {
            entrevistas = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            entrevistas = new ArrayList<>();
        }
    }
    
    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(entrevistas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}