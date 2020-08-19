package com.techres.techresfacebook.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "conversations")
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String nameConversation;

    @Column
    private Instant dateCreated;

    @ManyToOne
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameConversation() {
        return nameConversation;
    }

    public void setNameConversation(String nameConversation) {
        this.nameConversation = nameConversation;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", nameConversation='" + nameConversation + '\'' +
                ", dateCreated=" + dateCreated +
                ", client=" + client +
                '}';
    }
}
