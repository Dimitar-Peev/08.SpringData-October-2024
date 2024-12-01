package softuni.exam.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "personal_datas")
public class PersonalData extends BaseEntity {

    @Column
    private int age;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;

    private Character gender;

    @OneToOne(mappedBy = "personalData")
    private Visitor visitor;
}
