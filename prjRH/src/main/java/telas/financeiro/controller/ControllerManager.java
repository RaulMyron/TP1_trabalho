package telas.financeiro.controller;


public class ControllerManager {
    
    private static FinanceiroController financeiroController;
    
    
    private ControllerManager() {}
    
    
    public static FinanceiroController getFinanceiroController() {
        if (financeiroController == null) {
            financeiroController = new FinanceiroController();
        }
        return financeiroController;
    }
}