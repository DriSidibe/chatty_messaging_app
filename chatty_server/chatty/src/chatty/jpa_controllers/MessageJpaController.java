/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.jpa_controllers;

import chatty.jpa_controllers.exceptions.IllegalOrphanException;
import chatty.jpa_controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import chatty.models.Client;
import chatty.models.LastMessage;
import chatty.models.Message;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author dsidi
 */
public class MessageJpaController implements Serializable {

    public MessageJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Message message) {
        if (message.getLastMessageList() == null) {
            message.setLastMessageList(new ArrayList<LastMessage>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client postFor = message.getPostFor();
            if (postFor != null) {
                postFor = em.getReference(postFor.getClass(), postFor.getId());
                message.setPostFor(postFor);
            }
            Client postBy = message.getPostBy();
            if (postBy != null) {
                postBy = em.getReference(postBy.getClass(), postBy.getId());
                message.setPostBy(postBy);
            }
            List<LastMessage> attachedLastMessageList = new ArrayList<LastMessage>();
            for (LastMessage lastMessageListLastMessageToAttach : message.getLastMessageList()) {
                lastMessageListLastMessageToAttach = em.getReference(lastMessageListLastMessageToAttach.getClass(), lastMessageListLastMessageToAttach.getId());
                attachedLastMessageList.add(lastMessageListLastMessageToAttach);
            }
            message.setLastMessageList(attachedLastMessageList);
            em.persist(message);
            if (postFor != null) {
                postFor.getMessageList().add(message);
                postFor = em.merge(postFor);
            }
            if (postBy != null) {
                postBy.getMessageList().add(message);
                postBy = em.merge(postBy);
            }
            for (LastMessage lastMessageListLastMessage : message.getLastMessageList()) {
                Message oldMessageOfLastMessageListLastMessage = lastMessageListLastMessage.getMessage();
                lastMessageListLastMessage.setMessage(message);
                lastMessageListLastMessage = em.merge(lastMessageListLastMessage);
                if (oldMessageOfLastMessageListLastMessage != null) {
                    oldMessageOfLastMessageListLastMessage.getLastMessageList().remove(lastMessageListLastMessage);
                    oldMessageOfLastMessageListLastMessage = em.merge(oldMessageOfLastMessageListLastMessage);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Message message) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Message persistentMessage = em.find(Message.class, message.getId());
            Client postForOld = persistentMessage.getPostFor();
            Client postForNew = message.getPostFor();
            Client postByOld = persistentMessage.getPostBy();
            Client postByNew = message.getPostBy();
            List<LastMessage> lastMessageListOld = persistentMessage.getLastMessageList();
            List<LastMessage> lastMessageListNew = message.getLastMessageList();
            List<String> illegalOrphanMessages = null;
            for (LastMessage lastMessageListOldLastMessage : lastMessageListOld) {
                if (!lastMessageListNew.contains(lastMessageListOldLastMessage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LastMessage " + lastMessageListOldLastMessage + " since its message field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (postForNew != null) {
                postForNew = em.getReference(postForNew.getClass(), postForNew.getId());
                message.setPostFor(postForNew);
            }
            if (postByNew != null) {
                postByNew = em.getReference(postByNew.getClass(), postByNew.getId());
                message.setPostBy(postByNew);
            }
            List<LastMessage> attachedLastMessageListNew = new ArrayList<LastMessage>();
            for (LastMessage lastMessageListNewLastMessageToAttach : lastMessageListNew) {
                lastMessageListNewLastMessageToAttach = em.getReference(lastMessageListNewLastMessageToAttach.getClass(), lastMessageListNewLastMessageToAttach.getId());
                attachedLastMessageListNew.add(lastMessageListNewLastMessageToAttach);
            }
            lastMessageListNew = attachedLastMessageListNew;
            message.setLastMessageList(lastMessageListNew);
            message = em.merge(message);
            if (postForOld != null && !postForOld.equals(postForNew)) {
                postForOld.getMessageList().remove(message);
                postForOld = em.merge(postForOld);
            }
            if (postForNew != null && !postForNew.equals(postForOld)) {
                postForNew.getMessageList().add(message);
                postForNew = em.merge(postForNew);
            }
            if (postByOld != null && !postByOld.equals(postByNew)) {
                postByOld.getMessageList().remove(message);
                postByOld = em.merge(postByOld);
            }
            if (postByNew != null && !postByNew.equals(postByOld)) {
                postByNew.getMessageList().add(message);
                postByNew = em.merge(postByNew);
            }
            for (LastMessage lastMessageListNewLastMessage : lastMessageListNew) {
                if (!lastMessageListOld.contains(lastMessageListNewLastMessage)) {
                    Message oldMessageOfLastMessageListNewLastMessage = lastMessageListNewLastMessage.getMessage();
                    lastMessageListNewLastMessage.setMessage(message);
                    lastMessageListNewLastMessage = em.merge(lastMessageListNewLastMessage);
                    if (oldMessageOfLastMessageListNewLastMessage != null && !oldMessageOfLastMessageListNewLastMessage.equals(message)) {
                        oldMessageOfLastMessageListNewLastMessage.getLastMessageList().remove(lastMessageListNewLastMessage);
                        oldMessageOfLastMessageListNewLastMessage = em.merge(oldMessageOfLastMessageListNewLastMessage);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = message.getId();
                if (findMessage(id) == null) {
                    throw new NonexistentEntityException("The message with id " + id + " no longer exists.");
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
            Message message;
            try {
                message = em.getReference(Message.class, id);
                message.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The message with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<LastMessage> lastMessageListOrphanCheck = message.getLastMessageList();
            for (LastMessage lastMessageListOrphanCheckLastMessage : lastMessageListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Message (" + message + ") cannot be destroyed since the LastMessage " + lastMessageListOrphanCheckLastMessage + " in its lastMessageList field has a non-nullable message field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Client postFor = message.getPostFor();
            if (postFor != null) {
                postFor.getMessageList().remove(message);
                postFor = em.merge(postFor);
            }
            Client postBy = message.getPostBy();
            if (postBy != null) {
                postBy.getMessageList().remove(message);
                postBy = em.merge(postBy);
            }
            em.remove(message);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Message> findMessageEntities() {
        return findMessageEntities(true, -1, -1);
    }

    public List<Message> findMessageEntities(int maxResults, int firstResult) {
        return findMessageEntities(false, maxResults, firstResult);
    }

    private List<Message> findMessageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Message.class));
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

    public Message findMessage(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Message.class, id);
        } finally {
            em.close();
        }
    }

    public int getMessageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Message> rt = cq.from(Message.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Message findMessage(Date date) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("chattyPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        // Création et exécution d'une requête SQL native personnalisée
        Query query = em.createNativeQuery("SELECT * FROM message WHERE post_at = ?", Message.class);
        query.setParameter(1, date);

        // Récupération des résultats de la requête
        List<Message> messages = query.getResultList();

        em.getTransaction().commit();

        em.close();
        emf.close();
        return (!messages.isEmpty() ? messages.get(0) : null);
    }
    
    public Message findMessage(Client client) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("chattyPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        // Création et exécution d'une requête SQL native personnalisée
        Query query = em.createNativeQuery("SELECT * FROM message WHERE post_by = ?", Message.class);
        query.setParameter(1, client.getId());

        // Récupération des résultats de la requête
        List<Message> messages = query.getResultList();

        em.getTransaction().commit();

        em.close();
        emf.close();
        return (!messages.isEmpty() ? messages.get(0) : null);
    }
    
    public List<Message> findMessageEntities(int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("chattyPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        // Création et exécution d'une requête SQL native personnalisée
        Query query = em.createNativeQuery("SELECT * FROM message WHERE id > ?", Message.class);
        query.setParameter(1, id);

        // Récupération des résultats de la requête
        List<Message> messages = query.getResultList();

        em.getTransaction().commit();

        em.close();
        emf.close();
        return messages;
    }
    
}
