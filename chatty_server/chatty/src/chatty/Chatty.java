/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty;

import chatty.service.Chatty_service_implementation;
import chatty.service.Chatty_service_interface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author dsidi
 */
public class Chatty {
    public static void main(String[] args) {

        int port = 1099;
        
        try {
            Chatty_service_interface Chatty_service = new Chatty_service_implementation();
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("Chatty_service", Chatty_service);
            System.out.println("Serveur prêt sur le port " + port + ".");
        } catch (Exception e) {
            System.err.println("Erreur sur le serveur : " + e.toString());
            e.printStackTrace();
        }
    }
}
