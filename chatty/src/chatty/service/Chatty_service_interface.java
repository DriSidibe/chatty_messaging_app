/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chatty.service;

import chatty.models.Client;
import chatty.models.LastMessage;
import chatty.models.Message;
import chatty.models.Status;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dsidi
 */
public interface Chatty_service_interface extends Remote {
    
     // Functions
    void createClient(Client client) throws RemoteException;
    public boolean logClient(Client client) throws RemoteException;
    public boolean sendMsgClientToClient(Message message) throws RemoteException;
    public boolean broadcastMsgToAllClientsByClient(Client client) throws RemoteException;
    public boolean broadcastMsgToAllClientsByServer(Client client) throws RemoteException;
    public Client getClient(String username, String password) throws RemoteException;
    public List<Client> getClients() throws RemoteException;
    public Client getClient(String username) throws RemoteException;
    public List<Message> getMessages() throws RemoteException;
    public Message getMessage(int id) throws RemoteException;
    public Message getMessageByPoster(Client client) throws RemoteException;
    public Message getMessageByDate(Date date) throws RemoteException;
    public void createNewStatus(Status status) throws RemoteException;
    public void editNewStatus(Status status) throws RemoteException;
    public Status getStatus(Client client) throws RemoteException;
    public List<Status> getStatus() throws RemoteException;
    public void createNewLastMessage(LastMessage lastMessage) throws RemoteException;
    public void editLastMessage(LastMessage lastMessage) throws RemoteException;
    public LastMessage getLastMessage(Client client1, Client client2) throws RemoteException;
    public List<LastMessage> getLastMessage() throws RemoteException;
    public List<Message> getMessagesAfter(int id) throws RemoteException;
}
