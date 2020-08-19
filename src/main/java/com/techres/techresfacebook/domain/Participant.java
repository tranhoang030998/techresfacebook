package com.techres.techresfacebook.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "participants")
public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Instant timeJoin;

    @ManyToOne
    private Conversation conversation;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimeJoin() {
        return timeJoin;
    }

    public void setTimeJoin(Instant timeJoin) {
        this.timeJoin = timeJoin;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", timeJoin=" + timeJoin +
                ", conversation=" + conversation +
                ", user=" + user +
                '}';
    }
}
