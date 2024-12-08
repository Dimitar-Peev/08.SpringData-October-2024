package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "people")
public class Person extends BaseEntity {

    @Column(name = "first_name", unique = true, nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, length = 13)
    private String phone;

    @Column(name = "status_type")
    @Enumerated(EnumType.ORDINAL)
    private StatusType statusType;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;
}