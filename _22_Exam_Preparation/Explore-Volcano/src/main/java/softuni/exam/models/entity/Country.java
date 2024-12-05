package softuni.exam.models.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "countries")
public class Country extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String capital;

    @OneToMany(mappedBy = "country", fetch = FetchType.EAGER)
    private Set<Volcano> volcanoes = new HashSet<>();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return this.capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public Set<Volcano> getVolcanoes() {
        return this.volcanoes;
    }

    public void setVolcanoes(Set<Volcano> volcanoes) {
        this.volcanoes = volcanoes;
    }
}
