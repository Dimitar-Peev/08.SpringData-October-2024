package com.example.football.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "players")
public class Player extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private PlayerPosition position;

    @ManyToOne
    private Town town;

    @ManyToOne
    private Team team;

    @OneToOne
    @JoinColumn(name = "stat_id", referencedColumnName = "id")
    private Stat stat;

    @Override
    public String toString() {
        return String.format(String.format("""
                        Player %s %s
                        \t\tPosition - %s
                        \t\tTeam - %s
                        \t\tStadium - %s
                        """,
                this.getFirstName(), this.getLastName(),
                this.getPosition(),
                this.getTeam().getName(),
                this.getTeam().getStadiumName()));
    }
}
