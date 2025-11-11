package Model;

import java.io.Serializable;

/**
 * Superclasse abstrata que representa uma pessoa no sistema.
 * Implementa Serializable para permitir que seus objetos sejam salvos em arquivos.
 */
public abstract class Pessoa implements Serializable {

    private String nome;
    private String cpf;
    private String email;

    // Construtor para inicializar os atributos comuns a todas as pessoas.
    public Pessoa(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
    }

    // Métodos Getters e Setters para acesso aos atributos encapsulados.
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}