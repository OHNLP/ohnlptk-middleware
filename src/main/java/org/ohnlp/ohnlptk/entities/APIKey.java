package org.ohnlp.ohnlptk.entities;

import javax.persistence.*;

@Entity
@Table(name = "API_KEYS")
public class APIKey {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @Column
    private String token;

    @Column
    private String name;

    protected APIKey() {}

    public APIKey(User user, String name, String token) {
        this.token = token;
        this.name = name;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }
}
