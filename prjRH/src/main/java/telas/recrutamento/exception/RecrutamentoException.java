package telas.recrutamento.exception;

/**
 * Exceção base para operações relacionadas ao módulo de recrutamento.
 */
public class RecrutamentoException extends Exception {

    private static final long serialVersionUID = 1L;
    private String codigoErro;

    public RecrutamentoException(String mensagem) {
        super(mensagem);
    }

    public RecrutamentoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public RecrutamentoException(String codigoErro, String mensagem) {
        super(mensagem);
        this.codigoErro = codigoErro;
    }

    public RecrutamentoException(String codigoErro, String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.codigoErro = codigoErro;
    }

    public String getCodigoErro() {
        return codigoErro;
    }

    @Override
    public String toString() {
        if (codigoErro != null) {
            return String.format("[%s] %s", codigoErro, getMessage());
        }
        return super.toString();
    }
}