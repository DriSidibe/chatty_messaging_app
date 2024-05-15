/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dsidi
 */
@Entity
@Table(name = "CLIENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c"),
    @NamedQuery(name = "Client.findByFirstname", query = "SELECT c FROM Client c WHERE c.firstname = :firstname"),
    @NamedQuery(name = "Client.findByLastname", query = "SELECT c FROM Client c WHERE c.lastname = :lastname"),
    @NamedQuery(name = "Client.findByUsername", query = "SELECT c FROM Client c WHERE c.username = :username"),
    @NamedQuery(name = "Client.findByPassword", query = "SELECT c FROM Client c WHERE c.password = :password"),
    @NamedQuery(name = "Client.findById", query = "SELECT c FROM Client c WHERE c.id = :id")})
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Basic(optional = false)
    @Column(name = "LASTNAME")
    private String lastname;
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postFor")
    private List<Message> messageList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postBy")
    private List<Message> messageList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner2")
    private List<LastMessage> lastMessageList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner1")
    private List<LastMessage> lastMessageList1;

    public Client() {
    }

    public Client(Integer id) {
        this.id = id;
    }

    public Client(Integer id, String firstname, String lastname, String username, String password) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
    }

    public Client(String firstname, String lastname, String username, String password, List<Message> messageList, List<Message> messageList1, List<LastMessage> lastMessageList, List<LastMessage> lastMessageList1) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.messageList = messageList;
        this.messageList1 = messageList1;
        this.lastMessageList = lastMessageList;
        this.lastMessageList1 = lastMessageList1;
    }
    
    

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    @XmlTransient
    public List<Message> getMessageList1() {
        return messageList1;
    }

    public void setMessageList1(List<Message> messageList1) {
        this.messageList1 = messageList1;
    }

    @XmlTransient
    public List<LastMessage> getLastMessageList() {
        return lastMessageList;
    }

    public void setLastMessageList(List<LastMessage> lastMessageList) {
        this.lastMessageList = lastMessageList;
    }

    @XmlTransient
    public List<LastMessage> getLastMessageList1() {
        return lastMessageList1;
    }

    public void setLastMessageList1(List<LastMessage> lastMessageList1) {
        this.lastMessageList1 = lastMessageList1;
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
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chatty.models.Client[ id=" + id + " ]";
    }
    
}
