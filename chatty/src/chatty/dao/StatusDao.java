/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.dao;

import chatty.jpa_controllers.StatusJpaController;
import chatty.models.Client;
import chatty.models.Status;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author dsidi
 */
public class StatusDao {
    private final StatusJpaController statusJpaController;
    private final EntityManagerFactory emf;

    public StatusDao() {
        emf = Persistence.createEntityManagerFactory("chattyPU");
        statusJpaController = new StatusJpaController(emf);
    }
    
    public void createNewStatus(Status status){
        statusJpaController.create(status);
    }
    
    public void editStatus(Status status) throws Exception{
        statusJpaController.edit(status);
    }
    
    public Status getStatus(Client client){
        return statusJpaController.findStatus(client);
    }
    
    public List<Status> getStatus(){
        return statusJpaController.findStatusEntities();
    }
}
