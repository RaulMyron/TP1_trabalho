/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package telas.recrutamento.dao;

import telas.recrutamento.model.Recrutador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RecrutadorDAO {
    private static final String ARQUIVO = "recrutadores.dat";
    private List<Recrutador> recrutadores;
    
    public RecrutadorDAO() {
        recrutadores = new ArrayList<>();
        carregarDados();
    }
    
    public void salvar(Recrutador recrutador) {
        recrutadores.add(recrutador);
        salvarDados();
    }
    
    public Recrutador buscarPorCpf(String cpf) {
        for (Recrutador r : recrutadores) {
            if (r.getCpf().equals(cpf)) {
                return r;
            }
        }
        return null;
    }
    
    public List<Recrutador> listarTodos() {
        return new ArrayList<>(recrutadores);
    }
    
    public void atualizar(Recrutador recrutador) {
        for (int i = 0; i < recrutadores.size(); i++) {
            if (recrutadores.get(i).getCpf().equals(recrutador.getCpf())) {
                recrutadores.set(i, recrutador);
                salvarDados();
                return;
            }
        }
    }
    
    public void deletar(String cpf) {
        recrutadores.removeIf(r -> r.getCpf().equals(cpf));
        salvarDados();
    }
    
    @SuppressWarnings("unchecked")
    private void carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            recrutadores = (List<Recrutador>) ois.readObject();
        } catch (FileNotFoundException e) {
            recrutadores = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            recrutadores = new ArrayList<>();
        }
    }
    
    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(recrutadores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}