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
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author dsidi
 */
public class HomeController implements Initializable {

    @FXML
    private ListView<HBox> friends_list;
    @FXML
    private ImageView profile_img2;
    @FXML
    private Label profile_name;
    @FXML
    private ListView<HBox> msg_conversation;
    @FXML
    private TextField msg_field;
    @FXML
    private Circle active_status;
    
    private final Preferences prefs = Preferences.userRoot().node("chatty");
    List<Client> allUsers;
    ObservableList<HBox> usersViewData = FXCollections.observableArrayList();
    List<Message> allMessages;
    ObservableList<HBox> messagesViewData = FXCollections.observableArrayList();
    Map<HBox, Client> friendsHbox = new HashMap<>();
    Map<Client, List<Message>> messagesByFrien = new HashMap<>();
    Client oldFriendSelected = new Client("firstname", "lastname", "username", "password");
    Timer timer = new Timer();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            GlobalState.lastMessage = GlobalState.Chatty_service.getLastMessage(GlobalState.newConnectedUser);
            if (GlobalState.newConnectedUser != null) {
                Status s = GlobalState.Chatty_service.getStatus(GlobalState.newConnectedUser);
                s.setActive(1);
                GlobalState.Chatty_service.editNewStatus(s);
            }
            allMessages = GlobalState.Chatty_service.getMessages();
            
            friends_list.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) { try {
                    GlobalState.lastMessage.setMessage(allMessages.get(allMessages.size()-1));
                    GlobalState.Chatty_service.editLastMessage(GlobalState.lastMessage);
                    active_status.setVisible(true);
                    // Un clic
                    ObservableList<HBox> selectedItems = friends_list.getSelectionModel().getSelectedItems();
                    GlobalState.currentDiscussingFriend = friendsHbox.get(selectedItems.get(0));
                    GlobalState.lastMessage = GlobalState.Chatty_service.getLastMessage(GlobalState.currentDiscussingFriend);
                    profile_img2.setImage(((ImageView) selectedItems.get(0).getChildren().get(0)).getImage());
                    profile_name.setText(((Label) selectedItems.get(0).getChildren().get(1)).getText());
                    if (!friendsHbox.containsKey(selectedItems.get(0))) {
                        friendsHbox.put(selectedItems.get(0), GlobalState.currentDiscussingFriend);
                    }
                    if (oldFriendSelected != null && !Objects.equals(GlobalState.currentDiscussingFriend.getId(), oldFriendSelected.getId())) {
                        load_messages();
                    }
                    oldFriendSelected = GlobalState.currentDiscussingFriend;
                } catch (RemoteException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            });
            
            load_friends();
            friends_list.setItems(usersViewData);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (is_current_discussing_friend_connected()) {
                            Platform.runLater(()->active_status.setFill(Paint.valueOf("green")));
                        }else{
                            Platform.runLater(()->active_status.setFill(Paint.valueOf("red")));
                        }
                        add_last_message_to_conv();
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }, 0, 1000);
            
            GlobalState.currentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        Status s = GlobalState.Chatty_service.getStatus(GlobalState.newConnectedUser);
                        s.setActive(0);
                        GlobalState.Chatty_service.editNewStatus(s);
                        GlobalState.lastMessage.setMessage(allMessages.get(allMessages.size()-1));
                        GlobalState.Chatty_service.editLastMessage(GlobalState.lastMessage);
                        timer.cancel();
                    } catch (RemoteException ex) {
                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            } catch (RemoteException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }    

    @FXML
    private void logout(MouseEvent event) throws BackingStoreException {
        Alert confirmation = dialogs.confirmation("Confirmation", "Deconnection", "Etes vous sure de vouloir vous déconnecter ?");
        confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                prefs.put("userIsConnected", "false");
                try {
                    prefs.flush();
                    Status s = GlobalState.Chatty_service.getStatus(GlobalState.newConnectedUser);
                    s.setActive(0);
                    GlobalState.Chatty_service.editNewStatus(s);
                    GlobalState.lastMessage.setMessage(allMessages.get(allMessages.size()-1));
                    GlobalState.Chatty_service.editLastMessage(GlobalState.lastMessage);
                    swicth_scene("/chatty/views/Connexion_page.fxml");
                } catch (BackingStoreException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @FXML
    private void show_profile(MouseEvent event) {
    }

    @FXML
    private void search_discussion(KeyEvent event) {
    }

    @FXML
    private void send_msg(ActionEvent event) throws RemoteException {
        Date currentDate = new Date();
        Message message = new Message(msg_field.getText(), currentDate, GlobalState.currentDiscussingFriend, GlobalState.newConnectedUser);
        if (GlobalState.Chatty_service.sendMsgClientToClient(message)) {
            allMessages.add(GlobalState.Chatty_service.getMessageByDate(currentDate));
            messagesByFrien.get(GlobalState.currentDiscussingFriend).add(message);
            add_new_message_to_conversation(message);
            GlobalState.lastMessage.setMessage(allMessages.get(allMessages.size()-1));
            System.out.println("message sent !");
            return;
        }
        System.out.println("message not sent !");
        msg_field.clear();
    }
    
    void load_friends(){
        try {
            allUsers = GlobalState.Chatty_service.getClients();
        } catch (RemoteException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (Client user : allUsers) {
            if (!Objects.equals(user.getId(), GlobalState.newConnectedUser.getId()) && !"admin".equals(user.getUsername())) {
                HBox hb = new HBox();
                friendsHbox.put(hb, user);
                hb.getChildren().add(new ImageView("/chatty/static/images/profile.png"));
                hb.getChildren().add(new Label(user.getFirstname()+" "+user.getLastname()));
                usersViewData.add(hb);
            }
        }
    }
    
    void load_messages() throws RemoteException{
        if (!messagesByFrien.containsKey(GlobalState.currentDiscussingFriend)) {
            messagesByFrien.put(GlobalState.currentDiscussingFriend, get_messages_between_me_and_firend(allMessages));
        }
        
        messagesViewData.clear();
        for (Message message : messagesByFrien.get(GlobalState.currentDiscussingFriend)) {
            add_new_message_to_conversation(message);
        };
        msg_conversation.setItems(messagesViewData);
    }
    
    void add_new_message_to_conversation(Message message){
            HBox bhb = new HBox();
            HBox hb = new HBox();
            Label msg = new Label(message.getContent());
            Label date = new Label(message.getPostAt().toString());
            msg.setFont(Font.font("Arial", 15));
            date.setFont(Font.font("Arial", 12));
            msg.setStyle("-fx-text-fill: white;");
            date.setStyle("-fx-text-fill: black;");
            msg.setWrapText(true);
            hb.getChildren().add(msg);
            HBox.setHgrow(hb, Priority.NEVER);
            hb.setPadding(new Insets(20, 20, 20, 20));
            hb.setMaxWidth(300);
            hb.setStyle("-fx-border-raius: 20;");
            if(Objects.equals(message.getPostBy().getId(), GlobalState.newConnectedUser.getId())){
                bhb.getChildren().add(date);
                bhb.getChildren().add(hb);
                HBox.setMargin(hb, new Insets(50, 0, 0, 20));
            }else{
                bhb.getChildren().add(hb);
                bhb.getChildren().add(date);
                HBox.setMargin(hb, new Insets(50, 20, 0, 20));
            }
            if (Objects.equals(message.getPostBy().getId(), GlobalState.currentDiscussingFriend.getId())) {
                bhb.setAlignment(Pos.CENTER_LEFT);
                hb.setStyle("-fx-background-color: blue;");
            }else{
                bhb.setAlignment(Pos.CENTER_RIGHT);
                hb.setStyle("-fx-background-color: green;");
            }
            messagesViewData.add(bhb);
    }
    
    boolean is_current_discussing_friend_connected() throws RemoteException{
        if (GlobalState.currentDiscussingFriend != null) {
            if (GlobalState.Chatty_service.getStatus(GlobalState.currentDiscussingFriend).getActive() == 1) {
                return true;
            }
        }
        return false;
    }
    
    void add_last_message_to_conv() throws RemoteException{
        List<Message> messages = GlobalState.Chatty_service.getMessagesAfter(GlobalState.lastMessage.getMessage().getPostAt());
        List<Message> messages_betwee_me_and_friend = get_messages_between_me_and_firend(messages);
        for (Message message : messages_betwee_me_and_friend) {
            add_new_message_to_conversation(message);
        }
        if (messagesByFrien.containsKey(GlobalState.currentDiscussingFriend)) {
            messagesByFrien.get(GlobalState.currentDiscussingFriend).addAll(messages_betwee_me_and_friend);
        }
        allMessages.addAll(messages_betwee_me_and_friend);
        GlobalState.lastMessage.setMessage(allMessages.get(allMessages.size()-1));
    }
    
    List<Message> get_messages_between_me_and_firend(List<Message> messages){
        List<Message> l = new ArrayList<>();
        for (Message message : messages) {
            if ((Objects.equals(message.getPostFor().getId(), GlobalState.currentDiscussingFriend.getId()) && Objects.equals(message.getPostBy().getId(), GlobalState.newConnectedUser.getId())) || (Objects.equals(message.getPostBy().getId(), GlobalState.currentDiscussingFriend.getId()) && Objects.equals(message.getPostFor().getId(), GlobalState.newConnectedUser.getId()))) {
                l.add(message);
            }
        }
        return l;
    }
    
    void swicth_scene(String scn){
        Scene signupScene;
        Stage currentStage = (Stage)msg_conversation.getScene().getWindow();
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
    
}
