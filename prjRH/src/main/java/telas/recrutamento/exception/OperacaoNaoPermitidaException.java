package telas.recrutamento.exception;

/**
 * Exceção lançada quando uma operação não é permitida por regras de negócio.
 */
public class OperacaoNaoPermitidaException extends RecrutamentoException {

    private static final long serialVersionUID = 1L;
    private String operacao;
    private String motivo;

    public OperacaoNaoPermitidaException(String mensagem) {
        super("OPERACAO_NAO_PERMITIDA", mensagem);
    }

    public OperacaoNaoPermitidaException(String operacao, String motivo) {
        super("OPERACAO_NAO_PERMITIDA",
            String.format("Operação '%s' não permitida: %s", operacao, motivo));
        this.operacao = operacao;
        this.motivo = motivo;
    }

    public String getOperacao() {
        return operacao;
    }

    public String getMotivo() {
        return motivo;
    }
}
