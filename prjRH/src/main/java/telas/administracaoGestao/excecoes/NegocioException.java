package telas.administracaoGestao.excecoes;

// Exceção para erros de regras de negócio (ex: "CPF já cadastrado")
public class NegocioException extends Exception {
    public NegocioException(String message) {
        super(message);
    }
}