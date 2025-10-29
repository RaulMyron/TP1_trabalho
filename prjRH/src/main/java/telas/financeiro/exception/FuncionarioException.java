package telas.financeiro.exception;

/**
 * Exceção personalizada para erros relacionados a Funcionários
 */
public class FuncionarioException extends Exception {
    
    public FuncionarioException(String mensagem) {
        super(mensagem);
    }
    
    public FuncionarioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}