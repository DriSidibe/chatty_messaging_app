/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatty.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dsidi
 */
@Entity
@Table(name = "MESSAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m"),
    @NamedQuery(name = "Message.findByContent", query = "SELECT m FROM Message m WHERE m.content = :content"),
    @NamedQuery(name = "Message.findByPostAt", query = "SELECT m FROM Message m WHERE m.postAt = :postAt"),
    @NamedQuery(name = "Message.findById", query = "SELECT m FROM Message m WHERE m.id = :id")})
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "CONTENT")
    private String content;
    @Basic(optional = false)
    @Column(name = "POST_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postAt;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "POST_FOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Client postFor;
    @JoinColumn(name = "POST_BY", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Client postBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "message")
    private List<LastMessage> lastMessageList;

    public Message() {
    }

    public Message(Integer id) {
        this.id = id;
    }

    public Message(Integer id, Date postAt) {
        this.id = id;
        this.postAt = postAt;
    }

    public Message(String content, Date postAt, Client postFor, Client postBy, List<LastMessage> lastMessageList) {
        this.content = content;
        this.postAt = postAt;
        this.postFor = postFor;
        this.postBy = postBy;
        this.lastMessageList = lastMessageList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostAt() {
        return postAt;
    }

    public void setPostAt(Date postAt) {
        this.postAt = postAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getPostFor() {
        return postFor;
    }

    public void setPostFor(Client postFor) {
        this.postFor = postFor;
    }

    public Client getPostBy() {
        return postBy;
    }

    public void setPostBy(Client postBy) {
        this.postBy = postBy;
    }

    @XmlTransient
    public List<LastMessage> getLastMessageList() {
        return lastMessageList;
    }

    public void setLastMessageList(List<LastMessage> lastMessageList) {
        this.lastMessageList = lastMessageList;
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
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "chatty.models.Message[ id=" + id + " ]";
    }
    
}
