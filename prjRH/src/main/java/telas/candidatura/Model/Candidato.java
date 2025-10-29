package telas.candidatura.Model;

import telas.candidatura.Model.Pessoa;
import java.util.Date;
// Importe a classe List se for usar o histórico de candidaturas
// import java.util.List;
// import java.util.ArrayList;

/**
 * Representa um candidato no processo seletivo.
 * Herda os dados básicos da classe Pessoa.
 */
public class Candidato extends Pessoa {

    private String formacao;
    private String experiencia;
    private double pretensaoSalarial;
    private String disponibilidadeHorario;
    private final Date dataCadastro; // final, pois a data de cadastro não deve mudar

    // Construtor da classe Candidato
    public Candidato(String nome, String cpf, String email, String formacao, String experiencia, double pretensaoSalarial, String disponibilidade) {
        // 1. Chama o construtor da superclasse Pessoa para inicializar os dados herdados.
        super(nome, cpf, email);

        // 2. Inicializa os atributos específicos desta classe.
        this.formacao = formacao;
        this.experiencia = experiencia;
        this.pretensaoSalarial = pretensaoSalarial;
        this.disponibilidadeHorario = disponibilidade;

        // 3. Define a data de cadastro como o momento exato da criação do objeto.
        this.dataCadastro = new Date();
    }

    // Getters e Setters para os atributos específicos de Candidato
    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public double getPretensaoSalarial() {
        return pretensaoSalarial;
    }

    public void setPretensaoSalarial(double pretensaoSalarial) {
        this.pretensaoSalarial = pretensaoSalarial;
    }

    public String getDisponibilidadeHorario() {
        return disponibilidadeHorario;
    }

    public void setDisponibilidadeHorario(String disponibilidadeHorario) {
        this.disponibilidadeHorario = disponibilidadeHorario;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }
}