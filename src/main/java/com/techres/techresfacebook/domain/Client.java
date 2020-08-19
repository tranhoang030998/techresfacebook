package com.techres.techresfacebook.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "clients")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String credentialPlatform;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCredentialPlatform() {
        return credentialPlatform;
    }

    public void setCredentialPlatform(String credentialPlatform) {
        this.credentialPlatform = credentialPlatform;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", credentialPlatform='" + credentialPlatform + '\'' +
                ", user=" + user +
                '}';
    }
}
