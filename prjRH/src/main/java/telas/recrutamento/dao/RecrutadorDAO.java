package telas.recrutamento.dao;

import telas.recrutamento.model.Recrutador;
import telas.recrutamento.exception.RecrutamentoException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * DAO (Data Access Object) para gerenciar persistência de Recrutadores.
 * Thread-safe e com tratamento robusto de erros.
 */
public class RecrutadorDAO {
    private static final String ARQUIVO = "recrutadores.dat";
    private static final Logger LOGGER = Logger.getLogger(RecrutadorDAO.class.getName());

    private List<Recrutador> recrutadores;
    private final Object lock = new Object();

    public RecrutadorDAO() {
        recrutadores = new ArrayList<>();
        carregarDados();
    }

    /**
     * Salva um novo recrutador.
     * Thread-safe.
     */
    public void salvar(Recrutador recrutador) throws RecrutamentoException {
        if (recrutador == null) {
            throw new RecrutamentoException("Recrutador não pode ser nulo!");
        }

        synchronized (lock) {
            // Verifica duplicidade
            if (buscarPorCpfInterno(recrutador.getCpf()) != null) {
                throw new RecrutamentoException("Recrutador com este CPF já existe!");
            }

            recrutadores.add(recrutador);
            salvarDados();
            LOGGER.log(Level.INFO, "Recrutador salvo: {0}", recrutador.getNome());
        }
    }

    /**
     * Busca recrutador por CPF.
     * Thread-safe.
     */
    public Recrutador buscarPorCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return null;
        }

        synchronized (lock) {
            return buscarPorCpfInterno(cpf);
        }
    }

    /**
     * Busca interna por CPF (sem sincronização).
     */
    private Recrutador buscarPorCpfInterno(String cpf) {
        for (Recrutador r : recrutadores) {
            if (r.getCpf().equals(cpf)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Busca recrutador por login.
     * Thread-safe.
     */
    public Recrutador buscarPorLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            return null;
        }

        synchronized (lock) {
            for (Recrutador r : recrutadores) {
                if (r.getLogin().equals(login)) {
                    return r;
                }
            }
            return null;
        }
    }

    /**
     * Busca recrutadores por departamento.
     * Thread-safe.
     */
    public List<Recrutador> buscarPorDepartamento(String departamento) {
        if (departamento == null || departamento.trim().isEmpty()) {
            return new ArrayList<>();
        }

        synchronized (lock) {
            return recrutadores.stream()
                .filter(r -> departamento.equalsIgnoreCase(r.getDepartamento()))
                .collect(Collectors.toList());
        }
    }

    /**
     * Busca recrutadores por email.
     * Thread-safe.
     */
    public Recrutador buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        synchronized (lock) {
            for (Recrutador r : recrutadores) {
                if (r.getEmail().equalsIgnoreCase(email)) {
                    return r;
                }
            }
            return null;
        }
    }

    /**
     * Lista todos os recrutadores.
     * Retorna cópia para evitar modificações externas.
     * Thread-safe.
     */
    public List<Recrutador> listarTodos() {
        synchronized (lock) {
            return new ArrayList<>(recrutadores);
        }
    }

    /**
     * Lista recrutadores ativos.
     * Thread-safe.
     */
    public List<Recrutador> listarAtivos() {
        synchronized (lock) {
            return recrutadores.stream()
                .filter(r -> r.isAtivo())
                .collect(Collectors.toList());
        }
    }

    /**
     * Lista recrutadores por especialidade.
     * Thread-safe.
     */
    public List<Recrutador> buscarPorEspecialidade(String especialidade) {
        if (especialidade == null || especialidade.trim().isEmpty()) {
            return new ArrayList<>();
        }

        synchronized (lock) {
            return recrutadores.stream()
                .filter(r -> r.temEspecialidade(especialidade))
                .collect(Collectors.toList());
        }
    }

    /**
     * Atualiza um recrutador existente.
     * Thread-safe.
     */
    public void atualizar(Recrutador recrutador) throws RecrutamentoException {
        if (recrutador == null) {
            throw new RecrutamentoException("Recrutador não pode ser nulo!");
        }

        synchronized (lock) {
            boolean encontrado = false;

            for (int i = 0; i < recrutadores.size(); i++) {
                if (recrutadores.get(i).getCpf().equals(recrutador.getCpf())) {
                    recrutadores.set(i, recrutador);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                throw new RecrutamentoException("Recrutador não encontrado para atualização!");
            }

            salvarDados();
            LOGGER.log(Level.INFO, "Recrutador atualizado: {0}", recrutador.getNome());
        }
    }

    /**
     * Deleta um recrutador.
     * Thread-safe.
     */
    public void deletar(String cpf) throws RecrutamentoException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new RecrutamentoException("CPF não pode ser vazio!");
        }

        synchronized (lock) {
            boolean removido = recrutadores.removeIf(r -> r.getCpf().equals(cpf));

            if (!removido) {
                throw new RecrutamentoException("Recrutador não encontrado para deleção!");
            }

            salvarDados();
            LOGGER.log(Level.INFO, "Recrutador deletado: CPF {0}", cpf);
        }
    }

    /**
     * Conta total de recrutadores.
     * Thread-safe.
     */
    public int contarTodos() {
        synchronized (lock) {
            return recrutadores.size();
        }
    }

    /**
     * Conta recrutadores ativos.
     * Thread-safe.
     */
    public int contarAtivos() {
        synchronized (lock) {
            return (int) recrutadores.stream()
                .filter(r -> r.isAtivo())
                .count();
        }
    }

    /**
     * Verifica se existe recrutador com CPF.
     * Thread-safe.
     */
    public boolean existe(String cpf) {
        return buscarPorCpf(cpf) != null;
    }

    /**
     * Carrega dados do arquivo.
     */
    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(ARQUIVO);

        if (!arquivo.exists()) {
            LOGGER.log(Level.INFO, "Arquivo de dados não existe. Criando nova lista.");
            recrutadores = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            recrutadores = (List<Recrutador>) ois.readObject();
            LOGGER.log(Level.INFO, "Dados carregados: {0} recrutadores", recrutadores.size());

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "Arquivo não encontrado: {0}", ARQUIVO);
            recrutadores = new ArrayList<>();

        } catch (EOFException e) {
            LOGGER.log(Level.WARNING, "Arquivo vazio ou corrompido. Criando nova lista.");
            recrutadores = new ArrayList<>();

        } catch (InvalidClassException e) {
            LOGGER.log(Level.SEVERE, "Versão da classe incompatível. Backup necessário!", e);
            backupArquivo();
            recrutadores = new ArrayList<>();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar dados", e);
            recrutadores = new ArrayList<>();
        }
    }

    /**
     * Salva dados no arquivo.
     */
    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(recrutadores);
            LOGGER.log(Level.FINE, "Dados salvos: {0} recrutadores", recrutadores.size());

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar dados!", e);
            throw new RuntimeException("Falha ao salvar dados: " + e.getMessage(), e);
        }
    }

    /**
     * Cria backup do arquivo de dados.
     */
    private void backupArquivo() {
        File arquivo = new File(ARQUIVO);
        if (arquivo.exists()) {
            File backup = new File(ARQUIVO + ".backup." + System.currentTimeMillis());
            if (arquivo.renameTo(backup)) {
                LOGGER.log(Level.INFO, "Backup criado: {0}", backup.getName());
            }
        }
    }

    /**
     * Limpa todos os dados (usar com cuidado!).
     */
    public void limparTodos() {
        synchronized (lock) {
            recrutadores.clear();
            salvarDados();
            LOGGER.log(Level.WARNING, "Todos os recrutadores foram removidos!");
        }
    }
}