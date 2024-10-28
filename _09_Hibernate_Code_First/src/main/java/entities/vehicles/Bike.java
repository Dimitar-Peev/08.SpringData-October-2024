package entities.vehicles;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
//@Table(name = "bikes")
@DiscriminatorValue("BIKE")
public class Bike extends Vehicle  {
    @Column(name = "wheel_size")
    private double wheelSize ;

    public Bike() {
        super("BIKE", 1);
    }

    public double getWheelSize() {
        return this.wheelSize;
    }

    public void setWheelSize(double wheelSize) {
        this.wheelSize = wheelSize;
    }
}
