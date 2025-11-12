package telas.candidatura.Model;

import telas.candidatura.Model.Candidato;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para a entidade Candidato.
 * Responsável por salvar e carregar a lista de candidatos em um arquivo binário
 * usando a serialização de objetos.
 */
public class CandidatoDAO {

    // O nome do arquivo onde os dados serão armazenados.
    private static final String NOME_ARQUIVO = "candidatos.dat";

    /**
     * Salva uma lista de candidatos no arquivo.
     * A lista inteira é salva, sobrescrevendo qualquer conteúdo anterior.
     * @param candidatos A lista de objetos Candidato a ser salva.
     */
    public void salvar(List<Candidato> candidatos) {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(candidatos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de candidatos a partir do arquivo.
     * @return Uma List<Candidato> com os dados do arquivo, ou uma lista vazia se o arquivo não existir.
     */
    public List<Candidato> carregar() {
        // Primeiro, verifica se o arquivo de dados já existe.
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            // Se o arquivo não existe (primeira vez que o programa roda),
            // retorna uma lista nova e vazia.
            return new ArrayList<>();
        }

        // Se o arquivo existe, tenta ler os dados dele.
        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            // Lê o objeto do arquivo e faz o "cast" (conversão) para List<Candidato>.
            return (List<Candidato>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            // Se ocorrer um erro na leitura, imprime o erro e retorna uma lista vazia
            // para evitar que o programa quebre.
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}