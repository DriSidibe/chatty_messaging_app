/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package chatty;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author dsidi
 */
public class Chatty extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/Splash.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        FileHandler logFile = new FileHandler(System.getenv("USERPROFILE")+"\\chatty.log");
        Logger myLogger = Logger.getLogger(Chatty.class.getName());
        myLogger.addHandler(logFile);
        try {
            launch(args);
        } catch (Exception e) {
            myLogger.info(e.toString());
            System.exit(1);
        }
    }
    
}
