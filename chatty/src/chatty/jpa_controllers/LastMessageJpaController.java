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
            Client owner2 = lastMessage.getOwner2();
            if (owner2 != null) {
                owner2 = em.getReference(owner2.getClass(), owner2.getId());
                lastMessage.setOwner2(owner2);
            }
            Client owner1 = lastMessage.getOwner1();
            if (owner1 != null) {
                owner1 = em.getReference(owner1.getClass(), owner1.getId());
                lastMessage.setOwner1(owner1);
            }
            Message message = lastMessage.getMessage();
            if (message != null) {
                message = em.getReference(message.getClass(), message.getId());
                lastMessage.setMessage(message);
            }
            em.persist(lastMessage);
            if (owner2 != null) {
                owner2.getLastMessageList().add(lastMessage);
                owner2 = em.merge(owner2);
            }
            if (owner1 != null) {
                owner1.getLastMessageList().add(lastMessage);
                owner1 = em.merge(owner1);
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
            Client owner2Old = persistentLastMessage.getOwner2();
            Client owner2New = lastMessage.getOwner2();
            Client owner1Old = persistentLastMessage.getOwner1();
            Client owner1New = lastMessage.getOwner1();
            Message messageOld = persistentLastMessage.getMessage();
            Message messageNew = lastMessage.getMessage();
            if (owner2New != null) {
                owner2New = em.getReference(owner2New.getClass(), owner2New.getId());
                lastMessage.setOwner2(owner2New);
            }
            if (owner1New != null) {
                owner1New = em.getReference(owner1New.getClass(), owner1New.getId());
                lastMessage.setOwner1(owner1New);
            }
            if (messageNew != null) {
                messageNew = em.getReference(messageNew.getClass(), messageNew.getId());
                lastMessage.setMessage(messageNew);
            }
            lastMessage = em.merge(lastMessage);
            if (owner2Old != null && !owner2Old.equals(owner2New)) {
                owner2Old.getLastMessageList().remove(lastMessage);
                owner2Old = em.merge(owner2Old);
            }
            if (owner2New != null && !owner2New.equals(owner2Old)) {
                owner2New.getLastMessageList().add(lastMessage);
                owner2New = em.merge(owner2New);
            }
            if (owner1Old != null && !owner1Old.equals(owner1New)) {
                owner1Old.getLastMessageList().remove(lastMessage);
                owner1Old = em.merge(owner1Old);
            }
            if (owner1New != null && !owner1New.equals(owner1Old)) {
                owner1New.getLastMessageList().add(lastMessage);
                owner1New = em.merge(owner1New);
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
            Client owner2 = lastMessage.getOwner2();
            if (owner2 != null) {
                owner2.getLastMessageList().remove(lastMessage);
                owner2 = em.merge(owner2);
            }
            Client owner1 = lastMessage.getOwner1();
            if (owner1 != null) {
                owner1.getLastMessageList().remove(lastMessage);
                owner1 = em.merge(owner1);
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
    
    public LastMessage findLastMessage(Client client1, Client client2) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("chattyPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        // Création et exécution d'une requête SQL native personnalisée
        Query query = em.createNativeQuery("SELECT * FROM LAST_MESSAGE WHERE (OWNER_1 = ? AND owner_2 = ?) OR (OWNER_2 = ? AND owner_1 = ?)", LastMessage.class);
        query.setParameter(1, client1.getId());
        query.setParameter(2, client2.getId());
        query.setParameter(3, client1.getId());
        query.setParameter(4, client2.getId());

        // Récupération des résultats de la requête
        List<LastMessage> lastMessage = query.getResultList();

        em.getTransaction().commit();

        em.close();
        emf.close();
        return (!lastMessage.isEmpty() ? lastMessage.get(0) : null);
    }
}
