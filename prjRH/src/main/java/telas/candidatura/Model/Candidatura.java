package telas.candidatura.Model;

import telas.candidatura.Model.Candidato;
import java.io.Serializable;
import java.util.Date;
import telas.administracaoGestao.model.Vaga;

/**
 * Classe de associação que representa a aplicação de um Candidato a uma Vaga.
 */
public class Candidatura implements Serializable {

    private final Date dataCandidatura;
    private String status;

    // Atributos de associação
    private final Candidato candidato;
    private final Vaga vaga;

    public Candidatura(Candidato candidato, Vaga vaga) {
        this.candidato = candidato;
        this.vaga = vaga;
        this.dataCandidatura = new Date(); // Define a data no momento da criação
        this.status = "Pendente"; // Status inicial padrão
    }

    // Getters
    public Date getDataCandidatura() {
        return dataCandidatura;
    }

    public String getStatus() {
        return status;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public Vaga getVaga() {
        return vaga;
    }

    // Método para alterar o status, uma regra de negócio desta classe.
    public void setStatus(String status) {
        this.status = status;
    }
}