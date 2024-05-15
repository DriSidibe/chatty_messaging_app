/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dsidi
 */
@Entity
@Table(name = "LAST_MESSAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LastMessage.findAll", query = "SELECT l FROM LastMessage l"),
    @NamedQuery(name = "LastMessage.findById", query = "SELECT l FROM LastMessage l WHERE l.id = :id")})
public class LastMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "OWNER_2", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Client owner2;
    @JoinColumn(name = "OWNER_1", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Client owner1;
    @JoinColumn(name = "MESSAGE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Message message;

    public LastMessage() {
    }

    public LastMessage(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public LastMessage(Client owner2, Client owner1, Message message) {
        this.owner2 = owner2;
        this.owner1 = owner1;
        this.message = message;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getOwner2() {
        return owner2;
    }

    public void setOwner2(Client owner2) {
        this.owner2 = owner2;
    }

    public Client getOwner1() {
        return owner1;
    }

    public void setOwner1(Client owner1) {
        this.owner1 = owner1;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LastMessage)) {
            return false;
        }
        LastMessage other = (LastMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chatty.models.LastMessage[ id=" + id + " ]";
    }
    
}
