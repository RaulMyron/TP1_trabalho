package telas.administracaoGestao.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioBase<T> {

    public void salvar(List<T> dados, String nomeArquivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(dados);
        }
    }

    public List<T> carregar(String nomeArquivo) throws IOException, ClassNotFoundException {
        File arquivo = new File(nomeArquivo);
        if (!arquivo.exists()) {
            return new ArrayList<>(); 
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<T>) ois.readObject();
        }
    }
}