/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.dao;

import chatty.jpa_controllers.MessageJpaController;
import chatty.models.Client;
import chatty.models.Message;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author dsidi
 */
public class MessageDao {
    private final MessageJpaController messageJpaController;
    private final EntityManagerFactory emf;

    public MessageDao() {
        emf = Persistence.createEntityManagerFactory("chattyPU");
        messageJpaController = new MessageJpaController(emf);
    }
    
    public void create_message_for_user_to_user(Message m) throws Exception{
        messageJpaController.create(m);
    }

    public List<Message> get_Messages() {
        return messageJpaController.findMessageEntities();
    }
    
    public Message get_Messages(Date date) {
        return messageJpaController.findMessage(date);
    }
    
    public Message get_Message(int id) {
        return messageJpaController.findMessage(id);
    }
    
    public Message get_Messages(Client client) {
        return messageJpaController.findMessage(client);
    }
    
    public List<Message> get_Messages_after(Date date) {
        return messageJpaController.findMessageEntities(date);
    }
}
