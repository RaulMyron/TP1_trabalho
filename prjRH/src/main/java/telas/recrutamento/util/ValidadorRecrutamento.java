package telas.recrutamento.util;

import java.util.regex.Pattern;
import telas.recrutamento.exception.RecrutamentoException;

/**
 * Classe utilitária para validações relacionadas ao módulo de recrutamento.
 */
public class ValidadorRecrutamento {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Valida se um CPF está no formato correto e é válido.
     */
    public static void validarCPF(String cpf) throws RecrutamentoException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new RecrutamentoException("CPF não pode ser vazio!");
        }

        // Remove caracteres não numéricos
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        // Verifica se tem 11 dígitos
        if (cpfLimpo.length() != 11) {
            throw new RecrutamentoException("CPF deve conter 11 dígitos!");
        }

        // Verifica se todos os dígitos são iguais
        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            throw new RecrutamentoException("CPF inválido!");
        }

        // Valida dígitos verificadores
        if (!validarDigitosVerificadoresCPF(cpfLimpo)) {
            throw new RecrutamentoException("CPF inválido!");
        }
    }

    /**
     * Valida os dígitos verificadores do CPF.
     */
    private static boolean validarDigitosVerificadoresCPF(String cpf) {
        // Calcula primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) primeiroDigito = 0;

        if (Character.getNumericValue(cpf.charAt(9)) != primeiroDigito) {
            return false;
        }

        // Calcula segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) segundoDigito = 0;

        return Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
    }

    /**
     * Valida se um email está no formato correto.
     */
    public static void validarEmail(String email) throws RecrutamentoException {
        if (email == null || email.trim().isEmpty()) {
            throw new RecrutamentoException("Email não pode ser vazio!");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new RecrutamentoException("Email inválido! Use o formato: exemplo@dominio.com");
        }
    }

    /**
     * Valida se um nome é válido.
     */
    public static void validarNome(String nome) throws RecrutamentoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RecrutamentoException("Nome não pode ser vazio!");
        }

        if (nome.trim().length() < 3) {
            throw new RecrutamentoException("Nome deve ter pelo menos 3 caracteres!");
        }

        if (!nome.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new RecrutamentoException("Nome deve conter apenas letras e espaços!");
        }
    }

    /**
     * Valida se um departamento é válido.
     */
    public static void validarDepartamento(String departamento) throws RecrutamentoException {
        if (departamento == null || departamento.trim().isEmpty()) {
            throw new RecrutamentoException("Departamento não pode ser vazio!");
        }

        if (departamento.trim().length() < 2) {
            throw new RecrutamentoException("Departamento deve ter pelo menos 2 caracteres!");
        }
    }

    /**
     * Valida se um login é válido.
     */
    public static void validarLogin(String login) throws RecrutamentoException {
        if (login == null || login.trim().isEmpty()) {
            throw new RecrutamentoException("Login não pode ser vazio!");
        }

        if (login.length() < 4) {
            throw new RecrutamentoException("Login deve ter pelo menos 4 caracteres!");
        }

        if (!login.matches("^[a-zA-Z0-9._-]+$")) {
            throw new RecrutamentoException("Login deve conter apenas letras, números, pontos, underscores e hífens!");
        }
    }

    /**
     * Valida se uma senha é forte o suficiente.
     */
    public static void validarSenha(String senha) throws RecrutamentoException {
        if (senha == null || senha.isEmpty()) {
            throw new RecrutamentoException("Senha não pode ser vazia!");
        }

        if (senha.length() < 6) {
            throw new RecrutamentoException("Senha deve ter pelo menos 6 caracteres!");
        }
    }

    /**
     * Normaliza um CPF removendo caracteres não numéricos.
     */
    public static String normalizarCPF(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("[^0-9]", "");
    }
}
