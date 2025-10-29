package telas.financeiro.exception;

/**
 * Exceção personalizada para erros na Folha de Pagamento
 */
public class FolhaPagamentoException extends Exception {
    
    public FolhaPagamentoException(String mensagem) {
        super(mensagem);
    }
    
    public FolhaPagamentoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}