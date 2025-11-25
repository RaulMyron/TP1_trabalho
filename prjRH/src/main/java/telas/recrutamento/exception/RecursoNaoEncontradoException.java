package telas.recrutamento.exception;

/**
 * Exceção lançada quando um recurso não é encontrado.
 */
public class RecursoNaoEncontradoException extends RecrutamentoException {

    private static final long serialVersionUID = 1L;
    private String recurso;
    private String identificador;

    public RecursoNaoEncontradoException(String mensagem) {
        super("NAO_ENCONTRADO", mensagem);
    }

    public RecursoNaoEncontradoException(String recurso, String identificador) {
        super("NAO_ENCONTRADO", String.format("%s não encontrado(a): %s", recurso, identificador));
        this.recurso = recurso;
        this.identificador = identificador;
    }

    public String getRecurso() {
        return recurso;
    }

    public String getIdentificador() {
        return identificador;
    }
}
