/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.jpa_controllers;

import chatty.jpa_controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import chatty.models.Client;
import chatty.models.LastMessage;
import chatty.models.Message;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author dsidi
 */
public class LastMessageJpaController implements Serializable {

    public LastMessageJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LastMessage lastMessage) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client ownerLastMessage = lastMessage.getOwnerLastMessage();
            if (ownerLastMessage != null) {
                ownerLastMessage = em.getReference(ownerLastMessage.getClass(), ownerLastMessage.getId());
                lastMessage.setOwnerLastMessage(ownerLastMessage);
            }
            Message message = lastMessage.getMessage();
            if (message != null) {
                message = em.getReference(message.getClass(), message.getId());
                lastMessage.setMessage(message);
            }
            em.persist(lastMessage);
            if (ownerLastMessage != null) {
                ownerLastMessage.getLastMessageList().add(lastMessage);
                ownerLastMessage = em.merge(ownerLastMessage);
            }
            if (message != null) {
                message.getLastMessageList().add(lastMessage);
                message = em.merge(message);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LastMessage lastMessage) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LastMessage persistentLastMessage = em.find(LastMessage.class, lastMessage.getId());
            Client ownerLastMessageOld = persistentLastMessage.getOwnerLastMessage();
            Client ownerLastMessageNew = lastMessage.getOwnerLastMessage();
            Message messageOld = persistentLastMessage.getMessage();
            Message messageNew = lastMessage.getMessage();
            if (ownerLastMessageNew != null) {
                ownerLastMessageNew = em.getReference(ownerLastMessageNew.getClass(), ownerLastMessageNew.getId());
                lastMessage.setOwnerLastMessage(ownerLastMessageNew);
            }
            if (messageNew != null) {
                messageNew = em.getReference(messageNew.getClass(), messageNew.getId());
                lastMessage.setMessage(messageNew);
            }
            lastMessage = em.merge(lastMessage);
            if (ownerLastMessageOld != null && !ownerLastMessageOld.equals(ownerLastMessageNew)) {
                ownerLastMessageOld.getLastMessageList().remove(lastMessage);
                ownerLastMessageOld = em.merge(ownerLastMessageOld);
            }
            if (ownerLastMessageNew != null && !ownerLastMessageNew.equals(ownerLastMessageOld)) {
                ownerLastMessageNew.getLastMessageList().add(lastMessage);
                ownerLastMessageNew = em.merge(ownerLastMessageNew);
            }
            if (messageOld != null && !messageOld.equals(messageNew)) {
                messageOld.getLastMessageList().remove(lastMessage);
                messageOld = em.merge(messageOld);
            }
            if (messageNew != null && !messageNew.equals(messageOld)) {
                messageNew.getLastMessageList().add(lastMessage);
                messageNew = em.merge(messageNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = lastMessage.getId();
                if (findLastMessage(id) == null) {
                    throw new NonexistentEntityException("The lastMessage with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LastMessage lastMessage;
            try {
                lastMessage = em.getReference(LastMessage.class, id);
                lastMessage.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lastMessage with id " + id + " no longer exists.", enfe);
            }
            Client ownerLastMessage = lastMessage.getOwnerLastMessage();
            if (ownerLastMessage != null) {
                ownerLastMessage.getLastMessageList().remove(lastMessage);
                ownerLastMessage = em.merge(ownerLastMessage);
            }
            Message message = lastMessage.getMessage();
            if (message != null) {
                message.getLastMessageList().remove(lastMessage);
                message = em.merge(message);
            }
            em.remove(lastMessage);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LastMessage> findLastMessageEntities() {
        return findLastMessageEntities(true, -1, -1);
    }

    public List<LastMessage> findLastMessageEntities(int maxResults, int firstResult) {
        return findLastMessageEntities(false, maxResults, firstResult);
    }

    private List<LastMessage> findLastMessageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LastMessage.class));
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

    public LastMessage findLastMessage(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LastMessage.class, id);
        } finally {
            em.close();
        }
    }

    public int getLastMessageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LastMessage> rt = cq.from(LastMessage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public LastMessage findLastMessage(Client client) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("chattyPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        // Création et exécution d'une requête SQL native personnalisée
        Query query = em.createNativeQuery("SELECT * FROM LAST_MESSAGE WHERE OWNER_LAST_MESSAGE = ?", LastMessage.class);
        query.setParameter(1, client.getId());

        // Récupération des résultats de la requête
        List<LastMessage> lastMessage = query.getResultList();

        em.getTransaction().commit();

        em.close();
        emf.close();
        return (!lastMessage.isEmpty() ? lastMessage.get(0) : null);
    }
}
