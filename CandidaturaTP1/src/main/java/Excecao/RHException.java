package Excecao;

/**
 * Exceção personalizada para representar erros de regras de negócio
 * no sistema de RH.
 */
public class RHException extends Exception {

    public RHException(String message) {
        super(message);
    }
}