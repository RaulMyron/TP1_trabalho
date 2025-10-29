/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TesteRecrutamento;

import telas.recrutamento.controller.RecrutadorController;
import telas.recrutamento.controller.VagaController;
import telas.recrutamento.model.Recrutador;
import telas.recrutamento.model.Vaga;
import telas.recrutamento.view.Main;

public class TesteRecrutamento {
    
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA DE RECRUTAMENTO ===\n");
        
        // Criar dados de teste
        criarDadosTeste();
        
        // Abrir tela principal
        java.awt.EventQueue.invokeLater(() -> {
            Main menu = new Main();
            menu.carregarRecrutador("12345678900");
            menu.setVisible(true);
        });
    }
    
    private static void criarDadosTeste() {
        RecrutadorController recController = new RecrutadorController();
        VagaController vagaController = new VagaController();
        
        try {
            System.out.println("📋 Criando Recrutadores...");
            
            // Criar recrutador 1
            recController.cadastrar("12345678900", "João Silva", "joao@empresa.com", "RH");
            System.out.println("✅ Recrutador criado: João Silva");
            
            // Criar recrutador 2
            recController.cadastrar("98765432100", "Maria Santos", "maria@empresa.com", "TI");
            System.out.println("✅ Recrutador criado: Maria Santos");
            
        } catch (Exception e) {
            System.out.println("⚠️ Recrutadores já existem no sistema");
        }
        
        try {
            System.out.println("\n📋 Criando Vagas...");
            
            // Criar vagas de teste
            vagaController.cadastrar(
                "Desenvolvedor Java Sênior", 
                8000.0, 
                "TI", 
                "Java, Spring Boot, MySQL, 5 anos experiência", 
                "CLT", 
                "12345678900"
            );
            System.out.println("✅ Vaga criada: Desenvolvedor Java Sênior");
            
            vagaController.cadastrar(
                "Analista de RH", 
                5000.0, 
                "RH", 
                "Superior completo, experiência com recrutamento", 
                "CLT", 
                "12345678900"
            );
            System.out.println("✅ Vaga criada: Analista de RH");
            
            vagaController.cadastrar(
                "Estagiário Desenvolvimento", 
                1500.0, 
                "TI", 
                "Cursando Ciência da Computação", 
                "Estágio", 
                "12345678900"
            );
            System.out.println("✅ Vaga criada: Estagiário Desenvolvimento");
            
        } catch (Exception e) {
            System.out.println("⚠️ Vagas já existem ou erro: " + e.getMessage());
        }
        
        System.out.println("\n✅ Sistema pronto para uso!");
        System.out.println("==================================\n");
    }
}