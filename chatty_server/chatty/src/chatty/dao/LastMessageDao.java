/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.dao;

import chatty.jpa_controllers.LastMessageJpaController;
import chatty.models.Client;
import chatty.models.LastMessage;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author dsidi
 */
public class LastMessageDao {
    private final LastMessageJpaController lastMessageJpaController;
    private final EntityManagerFactory emf;

    public LastMessageDao() {
        emf = Persistence.createEntityManagerFactory("chattyPU");
        lastMessageJpaController = new LastMessageJpaController(emf);
    }
    
    public void createNewLastMessage(LastMessage lastMessage){
        lastMessageJpaController.create(lastMessage);
    }

    public void editLastMessage(LastMessage lastMessage) throws Exception{
        lastMessageJpaController.edit(lastMessage);
    }

    public LastMessage getLastMessage(Client client){
        return lastMessageJpaController.findLastMessage(client);
    }

    public List<LastMessage> getLastMessage(){
        return lastMessageJpaController.findLastMessageEntities();
    }
}
