/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package chatty.controllers;

import chatty.my_classes.GlobalState;
import chatty.service.Chatty_service_interface;
import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dsidi
 */
public class SplashController implements Initializable {
    
    @FXML
    private Label info_msg;
    private final Preferences prefs = Preferences.userRoot().node("chatty");
    
    String server = "localhost";
    int port = 1099;
    int retryWaitTime = 2000;
    boolean isConnectionToServerOk = false;
    
    // Create a service for running background tasks
    Service<Void> connectionToServerService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(retryWaitTime);
                        connect_to_server();
                        return null;
                    }
                };
            }
        };
    
    void connect_to_server() {
        try {
            // Connexion au serveur
            System.out.println("----------------------\n  --- BIENVENU SUR CHATY ---\n----------------------\n\n");
            System.out.println("--------------------------\n# Connexion aux serveurs #\n--------------------------\n");

            // Update UI indicating that connection is in progress
            Platform.runLater(() -> info_msg.setText("Essais de connexion au serveur ..."));

            try {
                GlobalState.registry = LocateRegistry.getRegistry(server, port);
                GlobalState.Chatty_service = (Chatty_service_interface) GlobalState.registry.lookup("Chatty_service");
            } catch (Exception e) {
                System.out.println(e.toString());
                return; // Exit early if connection fails
            }
            isConnectionToServerOk = true;
        } catch (Exception e) {
            System.err.println("Erreur sur le client : " + e.toString());
            e.printStackTrace();
        }
    }
    
    void change_scene(String nextScene) throws IOException{
        Stage oldStage = (Stage)info_msg.getScene().getWindow();
        oldStage.close();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(nextScene));
        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Add event handler to update UI when the task completes
        connectionToServerService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Platform.runLater(() -> {
                    if (!isConnectionToServerOk) {
                        try {
                            info_msg.setText("Serveur non disponible, veuillez verifier les parametres de connexion");
                            Thread.sleep(retryWaitTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        connectionToServerService.restart();
                    }else{
                        info_msg.setText("Connexion au serveur reussie\n");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            if ("true".equals(prefs.get("userIsConnected", ""))) {
                                change_scene("/chatty/views/Home.fxml");
                                return;
                            }
                            change_scene("/chatty/views/Connexion_page.fxml");
                        } catch (IOException ex) {
                            Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
        
        connectionToServerService.restart();
    }    
    
}
