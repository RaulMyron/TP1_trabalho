/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.projeto.prestacaoservico.main;
import br.com.projeto.prestacaoservico.login.LoginView;
/**
 *
 * @author fmdol
 */
public class App {
    public static void main(String[] args) {
            java.awt.EventQueue.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
