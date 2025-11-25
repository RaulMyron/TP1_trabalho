package telas.recrutamento.exception;

/**
 * Exceção lançada quando há tentativa de cadastrar dados duplicados.
 */
public class DuplicidadeException extends RecrutamentoException {

    private static final long serialVersionUID = 1L;
    private String entidade;
    private String identificador;

    public DuplicidadeException(String mensagem) {
        super("DUPLICIDADE", mensagem);
    }

    public DuplicidadeException(String entidade, String identificador, String mensagem) {
        super("DUPLICIDADE", mensagem);
        this.entidade = entidade;
        this.identificador = identificador;
    }

    public String getEntidade() {
        return entidade;
    }

    public String getIdentificador() {
        return identificador;
    }

    @Override
    public String toString() {
        if (entidade != null && identificador != null) {
            return String.format("[DUPLICIDADE - %s: %s] %s", entidade, identificador, getMessage());
        }
        return super.toString();
    }
}
