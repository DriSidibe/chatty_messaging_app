/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.service;

import chatty.dao.ClientDao;
import chatty.dao.LastMessageDao;
import chatty.dao.MessageDao;
import chatty.dao.StatusDao;
import chatty.models.Client;
import chatty.models.LastMessage;
import chatty.models.Message;
import chatty.models.Status;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dsidi
 */
public class Chatty_service_implementation extends UnicastRemoteObject implements Chatty_service_interface {
    
    public Chatty_service_implementation() throws RemoteException {
        super();
    }
    
    @Override
    public void createClient(Client client) throws RemoteException {
        ClientDao ud = new ClientDao();
        try {
            ud.create_new_client(client);
        } catch (Exception ex) {
            Logger.getLogger(Chatty_service_implementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean logClient(Client client) throws RemoteException {
        if (client == null) {
            return false;
        }
        if (getClient(client.getUsername(), client.getPassword()) != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean sendMsgClientToClient(Message message) throws RemoteException {
        try {
            MessageDao messageDao = new MessageDao();
            messageDao.create_message_for_user_to_user(message);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Chatty_service_implementation.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean broadcastMsgToAllClientsByClient(Client client) throws RemoteException {
        return true;
    }

    @Override
    public boolean broadcastMsgToAllClientsByServer(Client client) throws RemoteException {
        return true;
    }

    @Override
    public Client getClient(String username, String password) throws RemoteException {
        ClientDao ud = new ClientDao();
        return ud.get_Client_by_clientname_and_password(username, password);
    }

    @Override
    public List<Client> getClients() throws RemoteException {
        ClientDao ud = new ClientDao();
        return ud.get_Clients();
    }

    @Override
    public List<Message> getMessages() throws RemoteException {
        MessageDao ud = new MessageDao();
        return ud.get_Messages();
    }

    @Override
    public Message getMessageByDate(Date date) throws RemoteException {
        MessageDao ud = new MessageDao();
        return ud.get_Messages(date);
    }
    
    @Override
    public void createNewStatus(Status status) throws RemoteException {
        StatusDao sd = new StatusDao();
        sd.createNewStatus(status);
    }

    @Override
    public void editNewStatus(Status status) throws RemoteException {
        StatusDao sd = new StatusDao();
        try {
            sd.editStatus(status);
        } catch (Exception ex) {
            Logger.getLogger(Chatty_service_implementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Status getStatus(Client client) throws RemoteException {
        StatusDao sd = new StatusDao();
        return sd.getStatus(client);
    }

    @Override
    public List<Status> getStatus() throws RemoteException {
        StatusDao sd = new StatusDao();
        return sd.getStatus();
    }

    @Override
    public void createNewLastMessage(LastMessage lastMessage) throws RemoteException {
        LastMessageDao sd = new LastMessageDao();
        sd.createNewLastMessage(lastMessage);
    }

    @Override
    public void editLastMessage(LastMessage lastMessage) throws RemoteException {
        LastMessageDao sd = new LastMessageDao();
        try {
            sd.editLastMessage(lastMessage);
        } catch (Exception ex) {
            Logger.getLogger(Chatty_service_implementation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public LastMessage getLastMessage(Client client1, Client client2) throws RemoteException {
        LastMessageDao sd = new LastMessageDao();
        return sd.getLastMessage(client1, client2);
    }

    @Override
    public List<LastMessage> getLastMessage() throws RemoteException {
        LastMessageDao sd = new LastMessageDao();
        return sd.getLastMessage();
    }

    @Override
    public List<Message> getMessagesAfter(int id) throws RemoteException {
        MessageDao ud = new MessageDao();
        return ud.get_Messages_after(id);
    }

    @Override
    public Client getClient(String username) throws RemoteException {
        ClientDao ud = new ClientDao();
        return ud.get_Client(username);
    }

    @Override
    public Message getMessageByPoster(Client client) throws RemoteException {
        MessageDao ud = new MessageDao();
        return ud.get_Messages(client);
    }

    @Override
    public Message getMessage(int id) throws RemoteException {
        MessageDao ud = new MessageDao();
        return ud.get_Message(id);
    }
}