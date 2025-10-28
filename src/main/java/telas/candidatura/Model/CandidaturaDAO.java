package telas.candidatura.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para a entidade Candidatura.
 * Responsável por salvar e carregar a lista de candidaturas em um arquivo.
 */
public class CandidaturaDAO {

    private static final String NOME_ARQUIVO = "candidaturas.dat";

    public void salvar(List<Candidatura> candidaturas) {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(candidaturas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Candidatura> carregar() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<Candidatura>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}