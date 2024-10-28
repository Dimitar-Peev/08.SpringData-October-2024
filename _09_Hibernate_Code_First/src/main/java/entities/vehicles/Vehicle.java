package entities.vehicles;

import jakarta.persistence.*;

@Entity
//@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    @Column(insertable = false, updatable = false)
    private String type;

    @Column(name = "number_of_seats")
    private int numberOfSeats;

    public Vehicle() {
    }

    public Vehicle(String type, int numberOfSeats) {
        this.type = type;
        this.numberOfSeats = numberOfSeats;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumberOfSeats() {
        return this.numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString() {
        return String.format("Vehicle{id=%d, type='%s', numberOfSeats=%d}", id, type, numberOfSeats);
    }
}
