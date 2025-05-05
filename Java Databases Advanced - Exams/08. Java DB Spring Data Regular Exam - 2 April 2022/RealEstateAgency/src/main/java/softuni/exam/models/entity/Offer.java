package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {

    @Column(nullable = false, columnDefinition = "DECIMAL(19,2)")
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDate publishedOn;

    @ManyToOne
    private Apartment apartment;

    @ManyToOne
    private Agent agent;

    @Override
    public String toString() {
        Locale locale = Locale.US;
        DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));
        return String.format("""
                            Agent %s %s with offer №%d
                               -Apartment area: %s
                               --Town: %s
                               ---Price: %s$
                            """,
                this.agent.getFirstName(), this.agent.getLastName(), this.getId(),
                df.format(this.getApartment().getArea()),
                this.getApartment().getTown().getTownName(),
                df.format(this.getPrice()));
    }
}
