/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.dao;

import chatty.jpa_controllers.ClientJpaController;
import chatty.models.Client;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author dsidi
 */
public class ClientDao {
    private final ClientJpaController clientJpaController;
    private final EntityManagerFactory emf;

    public ClientDao() {
        emf = Persistence.createEntityManagerFactory("chattyPU");
        clientJpaController = new ClientJpaController(emf);
    }
    
    public void create_new_client(Client Client) throws Exception{
        clientJpaController.create(Client);
    }
    
    public Client get_Client_by_clientname_and_password(String username, String password){
        for (Client client : clientJpaController.findClientEntities()) {
            if (client.getUsername().equals(username) && client.getPassword().equals(password)) {
                return client;
            }
        }
        return null;
    }
    
    public Client get_Client_by_clientname(String username){
        for (Client client : clientJpaController.findClientEntities()) {
            if (client.getUsername().equals(username)) {
                return client;
            }
        }
        return null;
    }

    public List<Client> get_Clients() {
        return clientJpaController.findClientEntities();
    }
    
    public Client get_Client(String username) {
        return clientJpaController.findClient(username);
    }
}
