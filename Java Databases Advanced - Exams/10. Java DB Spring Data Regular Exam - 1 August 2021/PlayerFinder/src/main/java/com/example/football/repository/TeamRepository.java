package com.example.football.repository;

import com.example.football.models.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.Size;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(@Size(min = 3) String name);

    Team findByName(String name);
}
