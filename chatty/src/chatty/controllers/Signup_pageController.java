/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package chatty.controllers;

import chatty.models.Client;
import chatty.models.Message;
import chatty.models.Status;
import chatty.my_classes.GlobalState;
import chatty.my_classes.helpers.dialogs;
import chatty.service.Chatty_service_interface;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dsidi
 */
public class Signup_pageController implements Initializable {

    @FXML
    private TextField ins_lastnae;
    @FXML
    private TextField ins_username;
    @FXML
    private TextField ins_firstname;
    @FXML
    private TextField ins_passeword;
    @FXML
    private ImageView loading_gif;
    
    Registry registry;
    Chatty_service_interface Chatty_service = null;
    private final Preferences prefs = Preferences.userRoot().node("chatty");
    
    @FXML
    void signup_user() throws RemoteException{
        Platform.runLater(() -> loading_gif.setVisible(true));
        if (!"".equals(ins_username.getText())) {
            if (GlobalState.Chatty_service.getClient(ins_username.getText()) != null) {
                Platform.runLater(() -> loading_gif.setVisible(false));
                dialogs.error("Erreur", "Enregistrement utilisateur", "cet utilisateur exist deja !").showAndWait();
                return;
            }
            if (GlobalState.Chatty_service.getClient("admin") == null) {
                GlobalState.Chatty_service.createClient(new Client("", "", "admin", ""));
            }
            Message msg = null;
            if (GlobalState.Chatty_service.getMessageByPoster(GlobalState.Chatty_service.getClient("admin")) == null) {
                Client admin = GlobalState.Chatty_service.getClient("admin");
                msg = new Message("", new Date(), admin, admin);
                GlobalState.Chatty_service.sendMsgClientToClient(msg);
            }
            Client nc = new Client(ins_firstname.getText(), ins_lastnae.getText(), ins_username.getText(), ins_passeword.getText());
            GlobalState.Chatty_service.createClient(nc);
            if (GlobalState.Chatty_service.getStatus(GlobalState.Chatty_service.getClient(ins_username.getText())) == null) {
                Status ns = new Status(0, GlobalState.Chatty_service.getClient(ins_username.getText()));
                GlobalState.Chatty_service.createNewStatus(ns);
            }
            if (GlobalState.Chatty_service.getClient(ins_username.getText()) != null) {
                Platform.runLater(() -> loading_gif.setVisible(false));
                dialogs.information("Info", "Info utilisateur", "compte cré avec succès !").showAndWait();
            }else{
                Platform.runLater(() -> loading_gif.setVisible(false));
                dialogs.information("Info", "Info utilisateur", "erreur peandant la création !").showAndWait();
                return;
            }
            swicth_scene("/chatty/views/Connexion_page.fxml");
        }else{
            Platform.runLater(() -> loading_gif.setVisible(false));
            dialogs.error("Erreur", "Enregistrement utilisateur", "le nom d'utilisateur ne peut pas etre vide !").showAndWait();
        }
    }
    
    @FXML
    void swicth_scene_to_connection(){
        Scene signupScene;
        Stage currentStage = (Stage)ins_lastnae.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatty/views/Connexion_page.fxml"));
        Parent root = null;
        try {
            root = (Parent)loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Connexion_pageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        signupScene = new Scene(root);
        currentStage.setScene(signupScene);
    }
    
    void swicth_scene(String scn){
        Scene signupScene;
        Stage currentStage = (Stage)ins_username.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(scn));
        Parent root = null;
        try {
            root = (Parent)loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Connexion_pageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        signupScene = new Scene(root);
        currentStage.setScene(signupScene);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void login(ActionEvent event) {
    }
    
}
