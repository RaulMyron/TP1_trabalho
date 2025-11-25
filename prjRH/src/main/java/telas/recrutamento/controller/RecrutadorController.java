package telas.recrutamento.controller;

import telas.recrutamento.dao.RecrutadorDAO;
import telas.recrutamento.model.Recrutador;
import telas.recrutamento.exception.RecrutamentoException;
import telas.recrutamento.util.ValidadorRecrutamento;
import telas.administracaoGestao.model.Vaga;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsável por gerenciar operações relacionadas a Recrutadores.
 * Implementa validações e lógica de negócio.
 */
public class RecrutadorController {
    private RecrutadorDAO dao;

    public RecrutadorController() {
        this.dao = new RecrutadorDAO();
    }

    /**
     * Cadastra um novo recrutador com validações completas.
     */
    public boolean cadastrar(String cpf, String nome, String email, String login, String senha, String departamento) {
        try {
            // Validações
            ValidadorRecrutamento.validarCPF(cpf);
            ValidadorRecrutamento.validarNome(nome);
            ValidadorRecrutamento.validarEmail(email);
            ValidadorRecrutamento.validarLogin(login);
            ValidadorRecrutamento.validarSenha(senha);
            ValidadorRecrutamento.validarDepartamento(departamento);

            // Normaliza CPF
            String cpfNormalizado = ValidadorRecrutamento.normalizarCPF(cpf);

            // Verifica duplicidade
            if (dao.buscarPorCpf(cpfNormalizado) != null) {
                throw new RecrutamentoException("CPF já cadastrado!");
            }

            if (dao.buscarPorLogin(login) != null) {
                throw new RecrutamentoException("Login já existe! Escolha outro.");
            }

            // Cria e salva recrutador
            Recrutador recrutador = new Recrutador(cpfNormalizado, nome, email, login, senha);
            recrutador.setDepartamento(departamento);
            dao.salvar(recrutador);

            JOptionPane.showMessageDialog(null,
                "Recrutador cadastrado com sucesso!\nLogin: " + login,
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null,
                "Erro: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Erro inesperado: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Atualiza dados de um recrutador existente.
     */
    public boolean atualizar(String cpf, String novoNome, String novoEmail, String novoDepartamento) {
        try {
            ValidadorRecrutamento.validarNome(novoNome);
            ValidadorRecrutamento.validarEmail(novoEmail);
            ValidadorRecrutamento.validarDepartamento(novoDepartamento);

            String cpfNormalizado = ValidadorRecrutamento.normalizarCPF(cpf);
            Recrutador recrutador = dao.buscarPorCpf(cpfNormalizado);

            if (recrutador == null) {
                throw new RecrutamentoException("Recrutador não encontrado!");
            }

            recrutador.setNome(novoNome);
            recrutador.setEmail(novoEmail);
            recrutador.setDepartamento(novoDepartamento);
            dao.atualizar(recrutador);

            JOptionPane.showMessageDialog(null,
                "Recrutador atualizado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null,
                "Erro: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Erro inesperado: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Atualiza senha do recrutador com validação.
     */
    public boolean atualizarSenha(String cpf, String senhaAtual, String novaSenha) {
        try {
            ValidadorRecrutamento.validarSenha(novaSenha);

            String cpfNormalizado = ValidadorRecrutamento.normalizarCPF(cpf);
            Recrutador recrutador = dao.buscarPorCpf(cpfNormalizado);

            if (recrutador == null) {
                throw new RecrutamentoException("Recrutador não encontrado!");
            }

            if (!recrutador.autenticar(senhaAtual)) {
                throw new RecrutamentoException("Senha atual incorreta!");
            }

            recrutador.setSenha(novaSenha);
            dao.atualizar(recrutador);

            JOptionPane.showMessageDialog(null,
                "Senha atualizada com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null,
                "Erro: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Busca recrutador por CPF.
     */
    public Recrutador buscarPorCpf(String cpf) {
        try {
            String cpfNormalizado = ValidadorRecrutamento.normalizarCPF(cpf);
            Recrutador recrutador = dao.buscarPorCpf(cpfNormalizado);

            if (recrutador == null) {
                throw new RecrutamentoException("Recrutador não encontrado!");
            }
            return recrutador;

        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null,
                "Erro: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Busca recrutador por login (sem exibir mensagem de erro).
     */
    public Recrutador buscarPorLoginSilencioso(String login) {
        return dao.buscarPorLogin(login);
    }

    /**
     * Busca recrutadores por departamento.
     */
    public List<Recrutador> buscarPorDepartamento(String departamento) {
        return dao.buscarPorDepartamento(departamento);
    }

    /**
     * Busca recrutadores por nome (busca parcial).
     */
    public List<Recrutador> buscarPorNome(String nome) {
        return listarTodos().stream()
            .filter(r -> r.getNome().toLowerCase().contains(nome.toLowerCase()))
            .collect(Collectors.toList());
    }

    /**
     * Lista todos os recrutadores.
     */
    public List<Recrutador> listarTodos() {
        return dao.listarTodos();
    }

    /**
     * Lista apenas recrutadores ativos.
     */
    public List<Recrutador> listarAtivos() {
        return dao.listarTodos().stream()
            .filter(r -> r.isAtivo())
            .collect(Collectors.toList());
    }

    /**
     * Adiciona uma vaga ao gerenciamento do recrutador.
     */
    public boolean adicionarVagaAoRecrutador(String cpf, Vaga vaga) {
        try {
            String cpfNormalizado = ValidadorRecrutamento.normalizarCPF(cpf);
            Recrutador recrutador = dao.buscarPorCpf(cpfNormalizado);

            if (recrutador == null) {
                throw new RecrutamentoException("Recrutador não encontrado!");
            }

            if (!recrutador.isAptoParaNovasVagas()) {
                throw new RecrutamentoException("Recrutador não está apto para gerenciar novas vagas!");
            }

            recrutador.adicionarVagaGerenciada(vaga);
            dao.atualizar(recrutador);

            return true;

        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null,
                "Erro: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Adiciona especialidade ao recrutador.
     */
    public boolean adicionarEspecialidade(String cpf, String especialidade) {
        try {
            String cpfNormalizado = ValidadorRecrutamento.normalizarCPF(cpf);
            Recrutador recrutador = dao.buscarPorCpf(cpfNormalizado);

            if (recrutador == null) {
                throw new RecrutamentoException("Recrutador não encontrado!");
            }

            recrutador.adicionarEspecialidade(especialidade);
            dao.atualizar(recrutador);

            return true;

        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null,
                "Erro: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Deleta um recrutador do sistema.
     */
    public boolean deletar(String cpf) {
        try {
            String cpfNormalizado = ValidadorRecrutamento.normalizarCPF(cpf);
            Recrutador recrutador = dao.buscarPorCpf(cpfNormalizado);

            if (recrutador == null) {
                throw new RecrutamentoException("Recrutador não encontrado!");
            }

            // Validação de negócio: não permitir deletar se houver vagas ativas
            if (recrutador.getQuantidadeVagasAbertas() > 0) {
                throw new RecrutamentoException(
                    "Não é possível deletar recrutador com vagas abertas! " +
                    "Feche ou transfira as vagas primeiro."
                );
            }

            dao.deletar(cpfNormalizado);

            JOptionPane.showMessageDialog(null,
                "Recrutador deletado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (RecrutamentoException e) {
            JOptionPane.showMessageDialog(null,
                "Erro: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Gera relatório de desempenho de um recrutador.
     */
    public String gerarRelatorioDesempenho(String cpf) {
        try {
            String cpfNormalizado = ValidadorRecrutamento.normalizarCPF(cpf);
            Recrutador recrutador = dao.buscarPorCpf(cpfNormalizado);

            if (recrutador == null) {
                throw new RecrutamentoException("Recrutador não encontrado!");
            }

            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO DE DESEMPENHO ===\n\n");
            relatorio.append("Nome: ").append(recrutador.getNome()).append("\n");
            relatorio.append("Departamento: ").append(recrutador.getDepartamento()).append("\n");
            relatorio.append("Email: ").append(recrutador.getEmail()).append("\n\n");

            relatorio.append("--- Vagas Gerenciadas ---\n");
            relatorio.append("Total de vagas: ").append(recrutador.getTotalVagasGerenciadas()).append("\n");
            relatorio.append("Vagas abertas: ").append(recrutador.getQuantidadeVagasAbertas()).append("\n\n");

            relatorio.append("--- Contratações ---\n");
            relatorio.append("Solicitações: ").append(recrutador.getTotalContratacoesSolicitadas()).append("\n");
            relatorio.append("Aprovadas: ").append(recrutador.getTotalContratacaoAprovadas()).append("\n");
            relatorio.append(String.format("Taxa de aprovação: %.2f%%\n\n", recrutador.getTaxaAprovacao()));

            relatorio.append("--- Especialidades ---\n");
            if (recrutador.getEspecialidades().isEmpty()) {
                relatorio.append("Nenhuma especialidade cadastrada\n");
            } else {
                recrutador.getEspecialidades().forEach(e -> relatorio.append("- ").append(e).append("\n"));
            }

            return relatorio.toString();

        } catch (RecrutamentoException e) {
            return "Erro ao gerar relatório: " + e.getMessage();
        }
    }
}
