package telas.recrutamento.model;

import telas.administracaoGestao.model.Usuario;
import telas.administracaoGestao.model.Perfil;

public class Recrutador extends Usuario {

    private String departamento; // Campo específico para recrutador, se necessário

    public Recrutador(String cpf, String nome, String email, String login, String senha) {
        super(nome, cpf, email, login, senha);
        addPerfil(Perfil.RECRUTADOR); // Adiciona o perfil de recrutador
    }
    
    // --- Getters e Setters para campos específicos (opcional) ---

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}