package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "countries")
public class Country extends BaseEntity {

    @Column(length = 30, unique = true, nullable = false)
    private String name;

    @Column(length = 19, unique = true, nullable = false)
    private String code;

    @Column(length = 19, nullable = false)
    private String currency;

    @OneToMany(mappedBy = "country")
    private List<Person> people;

    @OneToMany(mappedBy = "country")
    private List<Company> companies;
}