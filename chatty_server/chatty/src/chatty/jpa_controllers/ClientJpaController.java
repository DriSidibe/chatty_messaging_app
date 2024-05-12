/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.jpa_controllers;

import chatty.jpa_controllers.exceptions.IllegalOrphanException;
import chatty.jpa_controllers.exceptions.NonexistentEntityException;
import chatty.models.Client;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import chatty.models.Message;
import java.util.ArrayList;
import java.util.List;
import chatty.models.LastMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author dsidi
 */
public class ClientJpaController implements Serializable {

    public ClientJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Client client) {
        if (client.getMessageList() == null) {
            client.setMessageList(new ArrayList<Message>());
        }
        if (client.getMessageList1() == null) {
            client.setMessageList1(new ArrayList<Message>());
        }
        if (client.getLastMessageList() == null) {
            client.setLastMessageList(new ArrayList<LastMessage>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Message> attachedMessageList = new ArrayList<Message>();
            for (Message messageListMessageToAttach : client.getMessageList()) {
                messageListMessageToAttach = em.getReference(messageListMessageToAttach.getClass(), messageListMessageToAttach.getId());
                attachedMessageList.add(messageListMessageToAttach);
            }
            client.setMessageList(attachedMessageList);
            List<Message> attachedMessageList1 = new ArrayList<Message>();
            for (Message messageList1MessageToAttach : client.getMessageList1()) {
                messageList1MessageToAttach = em.getReference(messageList1MessageToAttach.getClass(), messageList1MessageToAttach.getId());
                attachedMessageList1.add(messageList1MessageToAttach);
            }
            client.setMessageList1(attachedMessageList1);
            List<LastMessage> attachedLastMessageList = new ArrayList<LastMessage>();
            for (LastMessage lastMessageListLastMessageToAttach : client.getLastMessageList()) {
                lastMessageListLastMessageToAttach = em.getReference(lastMessageListLastMessageToAttach.getClass(), lastMessageListLastMessageToAttach.getId());
                attachedLastMessageList.add(lastMessageListLastMessageToAttach);
            }
            client.setLastMessageList(attachedLastMessageList);
            em.persist(client);
            for (Message messageListMessage : client.getMessageList()) {
                Client oldPostForOfMessageListMessage = messageListMessage.getPostFor();
                messageListMessage.setPostFor(client);
                messageListMessage = em.merge(messageListMessage);
                if (oldPostForOfMessageListMessage != null) {
                    oldPostForOfMessageListMessage.getMessageList().remove(messageListMessage);
                    oldPostForOfMessageListMessage = em.merge(oldPostForOfMessageListMessage);
                }
            }
            for (Message messageList1Message : client.getMessageList1()) {
                Client oldPostByOfMessageList1Message = messageList1Message.getPostBy();
                messageList1Message.setPostBy(client);
                messageList1Message = em.merge(messageList1Message);
                if (oldPostByOfMessageList1Message != null) {
                    oldPostByOfMessageList1Message.getMessageList1().remove(messageList1Message);
                    oldPostByOfMessageList1Message = em.merge(oldPostByOfMessageList1Message);
                }
            }
            for (LastMessage lastMessageListLastMessage : client.getLastMessageList()) {
                Client oldOwnerLastMessageOfLastMessageListLastMessage = lastMessageListLastMessage.getOwnerLastMessage();
                lastMessageListLastMessage.setOwnerLastMessage(client);
                lastMessageListLastMessage = em.merge(lastMessageListLastMessage);
                if (oldOwnerLastMessageOfLastMessageListLastMessage != null) {
                    oldOwnerLastMessageOfLastMessageListLastMessage.getLastMessageList().remove(lastMessageListLastMessage);
                    oldOwnerLastMessageOfLastMessageListLastMessage = em.merge(oldOwnerLastMessageOfLastMessageListLastMessage);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Client client) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client persistentClient = em.find(Client.class, client.getId());
            List<Message> messageListOld = persistentClient.getMessageList();
            List<Message> messageListNew = client.getMessageList();
            List<Message> messageList1Old = persistentClient.getMessageList1();
            List<Message> messageList1New = client.getMessageList1();
            List<LastMessage> lastMessageListOld = persistentClient.getLastMessageList();
            List<LastMessage> lastMessageListNew = client.getLastMessageList();
            List<String> illegalOrphanMessages = null;
            for (Message messageListOldMessage : messageListOld) {
                if (!messageListNew.contains(messageListOldMessage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Message " + messageListOldMessage + " since its postFor field is not nullable.");
                }
            }
            for (Message messageList1OldMessage : messageList1Old) {
                if (!messageList1New.contains(messageList1OldMessage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Message " + messageList1OldMessage + " since its postBy field is not nullable.");
                }
            }
            for (LastMessage lastMessageListOldLastMessage : lastMessageListOld) {
                if (!lastMessageListNew.contains(lastMessageListOldLastMessage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LastMessage " + lastMessageListOldLastMessage + " since its ownerLastMessage field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Message> attachedMessageListNew = new ArrayList<Message>();
            for (Message messageListNewMessageToAttach : messageListNew) {
                messageListNewMessageToAttach = em.getReference(messageListNewMessageToAttach.getClass(), messageListNewMessageToAttach.getId());
                attachedMessageListNew.add(messageListNewMessageToAttach);
            }
            messageListNew = attachedMessageListNew;
            client.setMessageList(messageListNew);
            List<Message> attachedMessageList1New = new ArrayList<Message>();
            for (Message messageList1NewMessageToAttach : messageList1New) {
                messageList1NewMessageToAttach = em.getReference(messageList1NewMessageToAttach.getClass(), messageList1NewMessageToAttach.getId());
                attachedMessageList1New.add(messageList1NewMessageToAttach);
            }
            messageList1New = attachedMessageList1New;
            client.setMessageList1(messageList1New);
            List<LastMessage> attachedLastMessageListNew = new ArrayList<LastMessage>();
            for (LastMessage lastMessageListNewLastMessageToAttach : lastMessageListNew) {
                lastMessageListNewLastMessageToAttach = em.getReference(lastMessageListNewLastMessageToAttach.getClass(), lastMessageListNewLastMessageToAttach.getId());
                attachedLastMessageListNew.add(lastMessageListNewLastMessageToAttach);
            }
            lastMessageListNew = attachedLastMessageListNew;
            client.setLastMessageList(lastMessageListNew);
            client = em.merge(client);
            for (Message messageListNewMessage : messageListNew) {
                if (!messageListOld.contains(messageListNewMessage)) {
                    Client oldPostForOfMessageListNewMessage = messageListNewMessage.getPostFor();
                    messageListNewMessage.setPostFor(client);
                    messageListNewMessage = em.merge(messageListNewMessage);
                    if (oldPostForOfMessageListNewMessage != null && !oldPostForOfMessageListNewMessage.equals(client)) {
                        oldPostForOfMessageListNewMessage.getMessageList().remove(messageListNewMessage);
                        oldPostForOfMessageListNewMessage = em.merge(oldPostForOfMessageListNewMessage);
                    }
                }
            }
            for (Message messageList1NewMessage : messageList1New) {
                if (!messageList1Old.contains(messageList1NewMessage)) {
                    Client oldPostByOfMessageList1NewMessage = messageList1NewMessage.getPostBy();
                    messageList1NewMessage.setPostBy(client);
                    messageList1NewMessage = em.merge(messageList1NewMessage);
                    if (oldPostByOfMessageList1NewMessage != null && !oldPostByOfMessageList1NewMessage.equals(client)) {
                        oldPostByOfMessageList1NewMessage.getMessageList1().remove(messageList1NewMessage);
                        oldPostByOfMessageList1NewMessage = em.merge(oldPostByOfMessageList1NewMessage);
                    }
                }
            }
            for (LastMessage lastMessageListNewLastMessage : lastMessageListNew) {
                if (!lastMessageListOld.contains(lastMessageListNewLastMessage)) {
                    Client oldOwnerLastMessageOfLastMessageListNewLastMessage = lastMessageListNewLastMessage.getOwnerLastMessage();
                    lastMessageListNewLastMessage.setOwnerLastMessage(client);
                    lastMessageListNewLastMessage = em.merge(lastMessageListNewLastMessage);
                    if (oldOwnerLastMessageOfLastMessageListNewLastMessage != null && !oldOwnerLastMessageOfLastMessageListNewLastMessage.equals(client)) {
                        oldOwnerLastMessageOfLastMessageListNewLastMessage.getLastMessageList().remove(lastMessageListNewLastMessage);
                        oldOwnerLastMessageOfLastMessageListNewLastMessage = em.merge(oldOwnerLastMessageOfLastMessageListNewLastMessage);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = client.getId();
                if (findClient(id) == null) {
                    throw new NonexistentEntityException("The client with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client client;
            try {
                client = em.getReference(Client.class, id);
                client.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The client with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Message> messageListOrphanCheck = client.getMessageList();
            for (Message messageListOrphanCheckMessage : messageListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the Message " + messageListOrphanCheckMessage + " in its messageList field has a non-nullable postFor field.");
            }
            List<Message> messageList1OrphanCheck = client.getMessageList1();
            for (Message messageList1OrphanCheckMessage : messageList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the Message " + messageList1OrphanCheckMessage + " in its messageList1 field has a non-nullable postBy field.");
            }
            List<LastMessage> lastMessageListOrphanCheck = client.getLastMessageList();
            for (LastMessage lastMessageListOrphanCheckLastMessage : lastMessageListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Client (" + client + ") cannot be destroyed since the LastMessage " + lastMessageListOrphanCheckLastMessage + " in its lastMessageList field has a non-nullable ownerLastMessage field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(client);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Client> findClientEntities() {
        return findClientEntities(true, -1, -1);
    }

    public List<Client> findClientEntities(int maxResults, int firstResult) {
        return findClientEntities(false, maxResults, firstResult);
    }

    private List<Client> findClientEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Client.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Client findClient(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Client> rt = cq.from(Client.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Client findClient(String username) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("chattyPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        // Création et exécution d'une requête SQL native personnalisée
        Query query = em.createNativeQuery("SELECT * FROM client WHERE username = ?", Client.class);
        query.setParameter(1, username);

        // Récupération des résultats de la requête
        List<Client> client = query.getResultList();

        em.getTransaction().commit();

        em.close();
        emf.close();
        return (!client.isEmpty() ? client.get(0) : null);
    }
}
