package softuni.exam.models.entity;

import softuni.exam.models.enums.VolcanoType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "volcanoes")
public class Volcano extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer elevation;

    @Column(name = "volcano_type")
    @Enumerated(EnumType.STRING)
    private VolcanoType volcanoType;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "last_eruption")
    private LocalDate lastEruption;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @OneToMany(mappedBy = "exploringVolcano", fetch = FetchType.EAGER)
    private Set<Volcanologist> volcanologists = new HashSet<>();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getElevation() {
        return this.elevation;
    }

    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    public VolcanoType getVolcanoType() {
        return this.volcanoType;
    }

    public void setVolcanoType(VolcanoType volcanoType) {
        this.volcanoType = volcanoType;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDate getLastEruption() {
        return this.lastEruption;
    }

    public void setLastEruption(LocalDate lastEruption) {
        this.lastEruption = lastEruption;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Volcanologist> getVolcanologists() {
        return this.volcanologists;
    }

    public void setVolcanologists(Set<Volcanologist> volcanologists) {
        this.volcanologists = volcanologists;
    }


}
