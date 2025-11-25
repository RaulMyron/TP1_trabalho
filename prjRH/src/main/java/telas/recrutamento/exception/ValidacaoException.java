package telas.recrutamento.exception;

/**
 * Exceção lançada quando há erro de validação de dados.
 */
public class ValidacaoException extends RecrutamentoException {

    private static final long serialVersionUID = 1L;
    private String campo;

    public ValidacaoException(String mensagem) {
        super("VALIDACAO", mensagem);
    }

    public ValidacaoException(String campo, String mensagem) {
        super("VALIDACAO", mensagem);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }

    @Override
    public String toString() {
        if (campo != null) {
            return String.format("[VALIDACAO - %s] %s", campo, getMessage());
        }
        return super.toString();
    }
}
