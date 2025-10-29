package telas.financeiro.controller;

/**
 * Gerenciador de Controllers - padrão Singleton
 * Garante que todas as telas usem a mesma instância do Controller
 */
public class ControllerManager {
    
    private static FinanceiroController financeiroController;
    
    // Construtor privado (ninguém pode criar diretamente)
    private ControllerManager() {}
    
    /**
     * Retorna a instância única do FinanceiroController
     */
    public static FinanceiroController getFinanceiroController() {
        if (financeiroController == null) {
            financeiroController = new FinanceiroController();
        }
        return financeiroController;
    }
}