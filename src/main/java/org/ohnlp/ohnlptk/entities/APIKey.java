package org.ohnlp.ohnlptk.entities;

import javax.persistence.*;

@Entity
@Table(name = "API_KEYS")
public class APIKey {
    @Id
    @Column
    private String token;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    protected APIKey() {}

    public APIKey(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
