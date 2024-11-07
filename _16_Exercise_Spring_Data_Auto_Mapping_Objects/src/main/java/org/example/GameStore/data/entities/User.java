package org.example.GameStore.data.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column
    private String email;

    @Column
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_games",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"))
    private Set<Game> games;

    public User() {
        this.games = new HashSet<>();
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Set<Game> getGames() {
        return this.games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
