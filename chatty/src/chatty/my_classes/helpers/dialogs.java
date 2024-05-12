/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatty.my_classes.helpers;

import javafx.scene.control.Alert;

/**
 *
 * @author drissa
 */
public class dialogs {
    private static Alert information = new Alert(Alert.AlertType.INFORMATION);
    private static Alert warning = new Alert(Alert.AlertType.WARNING);
    private static Alert error = new Alert(Alert.AlertType.ERROR);
    private static Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    
    public static Alert confirmation(String title, String header, String message){
        Alert c = dialogs.confirmation;
        c.setTitle(title);
        c.setHeaderText(header);
        c.setContentText(message);
        return c;   
    }
    
    public static Alert warning(String title, String header, String message){
        Alert w = dialogs.warning;
        w.setTitle(title);
        w.setHeaderText(header);
        w.setContentText(message);
        return w;   
    }
    
    public static Alert error(String title, String header, String message){
        Alert e = dialogs.error;
        e.setTitle(title);
        e.setHeaderText(header);
        e.setContentText(message);
        return e;   
    }
    
    public static Alert information(String title, String header, String message){
        Alert i = dialogs.information;
        i.setTitle(title);
        i.setHeaderText(header);
        i.setContentText(message);
        return i;   
    }
}
