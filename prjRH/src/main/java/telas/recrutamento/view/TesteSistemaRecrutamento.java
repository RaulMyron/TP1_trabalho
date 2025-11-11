package telas.recrutamento.view;

import telas.recrutamento.controller.RecrutadorController;
import telas.recrutamento.controller.VagaController;
import telas.recrutamento.model.Recrutador;

public class TesteSistemaRecrutamento {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  SISTEMA DE RECRUTAMENTO - TESTE      ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Criar dados de teste
        criarDadosTeste();
        
        // Abrir tela principal
        java.awt.EventQueue.invokeLater(() -> {
            Main menu = new Main();
            menu.carregarRecrutador("12345678900");
            menu.setVisible(true);
            System.out.println("✅ Sistema iniciado com sucesso!");
        });
    }
    
    private static void criarDadosTeste() {
        RecrutadorController recController = new RecrutadorController();
        VagaController vagaController = new VagaController();
        
        try {
            System.out.println("📋 Criando Recrutadores de Teste...");
            recController.cadastrar("12345678900", "João Silva", "joao@empresa.com", "RH");
            System.out.println("   ✅ João Silva criado");
            
            recController.cadastrar("98765432100", "Maria Santos", "maria@empresa.com", "TI");
            System.out.println("   ✅ Maria Santos criada");
            
        } catch (Exception e) {
            System.out.println("   ℹ️ Recrutadores já existem");
        }
        
        try {
            System.out.println("\n📋 Criando Vagas de Teste...");
            
            vagaController.cadastrar(
                "Desenvolvedor Java Sênior", 
                8000.0, 
                "TI", 
                "Java, Spring Boot, MySQL, 5+ anos", 
                "CLT", 
                "12345678900"
            );
            System.out.println("   ✅ Vaga 1: Desenvolvedor Java Sênior");
            
            vagaController.cadastrar(
                "Analista de RH", 
                5000.0, 
                "RH", 
                "Superior completo em Psicologia ou RH", 
                "CLT", 
                "12345678900"
            );
            System.out.println("   ✅ Vaga 2: Analista de RH");
            
            vagaController.cadastrar(
                "Estagiário Desenvolvimento", 
                1500.0, 
                "TI", 
                "Cursando Ciência da Computação", 
                "Estágio", 
                "12345678900"
            );
            System.out.println("   ✅ Vaga 3: Estagiário Desenvolvimento");
            
            vagaController.cadastrar(
                "Gerente de Projetos", 
                12000.0, 
                "TI", 
                "MBA, PMP, 10+ anos experiência", 
                "CLT", 
                "98765432100"
            );
            System.out.println("   ✅ Vaga 4: Gerente de Projetos");
            
        } catch (Exception e) {
            System.out.println("   ℹ️ Vagas já existem ou erro: " + e.getMessage());
        }
        
        System.out.println("\n✅ Dados de teste criados!");
        System.out.println("════════════════════════════════════════\n");
    }
}