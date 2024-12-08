package softuni.exam.models.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "jobs")
public class Job extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false)
    private Double hoursAWeek;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToOne
    private Company company;

    @ManyToMany
    @JoinTable(name = "jobs_companies",
            joinColumns = @JoinColumn(name = "jobs_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"))
    private List<Company> companies = new ArrayList<>();

    @Override
    public String toString() {
        Locale locale = Locale.US;
        DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));
        return String.format("""
                        Job title %s
                        -Salary: %s$
                        --Hours a week: %sh.
                        """,
                this.getTitle(), df.format(this.getSalary()), df.format(this.getHoursAWeek()));
    }
}
