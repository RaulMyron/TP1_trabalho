package telas.candidatura.Model;

import java.io.Serializable;

/**
 * ESTA É UMA CLASSE TEMPORÁRIA (PLACEHOLDER)
 * Sua única função é permitir que a classe Candidatura compile enquanto a
 * versão final da classe Vaga não é implementada por outro membro da equipe.
 */
public class Vaga implements Serializable {

    // Adicionamos um atributo mínimo para a classe não ser completamente vazia.
    private String cargo;
    private double salario;

    // Construtor mínimo para criar objetos de teste.
    public Vaga(String cargo, double salario) {
        this.cargo = cargo;
        this.salario = salario;
    }

    // Um getter mínimo, caso seja necessário para algum teste.
    public String getCargo() {
        return cargo;
    }
    
    @Override
    public String toString() {
        return this.cargo; // Facilita a exibição em listas e comboboxes na View.
    }
}