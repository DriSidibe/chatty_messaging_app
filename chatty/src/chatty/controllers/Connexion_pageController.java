/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package chatty.controllers;

import chatty.models.Client;
import chatty.my_classes.GlobalState;
import chatty.my_classes.helpers.dialogs;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author dsidi
 */
public class Connexion_pageController implements Initializable {
    
    

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label register_link;
    @FXML
    private CheckBox stay_connected_button;
    @FXML
    private ImageView loading_img;
    
    private final Preferences prefs = Preferences.userRoot().node("chatty");
    boolean stayConnected = false;
    
    @FXML
    void connect_user() throws BackingStoreException, RemoteException{
        Platform.runLater(() -> loading_img.setVisible(true));
        if (!"".equals(username.getText()) || !"".equals(password.getText())) {
            Client newConnectedUser = GlobalState.Chatty_service.getClient(username.getText(), password.getText());
            if (GlobalState.Chatty_service.logClient(newConnectedUser)) {
                if (stayConnected) {
                    prefs.put("userIsConnected", "true");
                    prefs.flush();
                }
                //boolean alreadyAsStatus = false;
                GlobalState.newConnectedUser = newConnectedUser;
//                for (Status status : GlobalState.Chatty_service.getStatus()) {
//                    if (Objects.equals(status.getOwner().getId(), GlobalState.newConnectedUser.getId())) {
//                        alreadyAsStatus = true;
//                    }
//                }
//                if (!alreadyAsStatus) {
//                    GlobalState.Chatty_service.createNewStatus(new Status(1, GlobalState.newConnectedUser));
//                }
                swicth_scene("/chatty/views/Home.fxml");
                return;
            }
        }
        Platform.runLater(() -> loading_img.setVisible(false));
        dialogs.error("Info utilisateur", "Existence utilisateur", "Les donnés entrées sont incorrectent !").showAndWait();
    }
    
    @FXML
    void swicth_scene_to_signup(){
        Scene signupScene;
        Stage currentStage = (Stage)username.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatty/views/Signup_page.fxml"));
        Parent root = null;
        try {
            root = (Parent)loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Connexion_pageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        signupScene = new Scene(root);
        currentStage.setScene(signupScene);
    }
    
    @FXML
    void checkbox_click(){
        stayConnected = stay_connected_button.isSelected();
    }
    
    void swicth_scene(String scn){
        Stage currentStage = (Stage)username.getScene().getWindow();
        GlobalState.currentStage = currentStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(scn));
        Parent root = null;
        try {
            root = (Parent)loader.load();
            System.out.println(root);
        } catch (IOException ex) {
            Logger.getLogger(Connexion_pageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentStage.setScene(new Scene(root));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    
}
