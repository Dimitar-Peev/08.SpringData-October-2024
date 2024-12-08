package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String website;

    @Column(name = "date_established", nullable = false)
    private LocalDate dateEstablished;

    @ManyToOne
    private Country country;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "companies")
    private List<Job> jobsList = new ArrayList<>();
}
