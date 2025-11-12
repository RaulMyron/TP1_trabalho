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

    private final Candidato candidato;
    private final Vaga vaga;

    public Candidatura(Candidato candidato, Vaga vaga) {
        this.candidato = candidato;
        this.vaga = vaga;
        this.dataCandidatura = new Date();
        this.status = "Pendente";
    }

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


    public void setStatus(String status) {
        this.status = status;
    }
}