package exam.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "laptops")
public class Laptop extends BaseEntity {

    @Column(name = "cpu_speed", nullable = false)
    private Double cpuSpeed;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "mac_address", unique = true, nullable = false)
    private String macAddress;

    @Column(columnDefinition = "DECIMAL(19,2)", nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer ram;

    @Column(nullable = false)
    private Integer storage;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "warranty_type", nullable = false)
    private WarrantyType warrantyType;

    @ManyToOne
    private Shop shop;

    @Override
    public String toString() {
        Locale locale = Locale.US;
        DecimalFormat df = new DecimalFormat("####.00", DecimalFormatSymbols.getInstance(locale));
        return String.format("""
                        Laptop %s
                        *Cpu speed - %s
                        **Ram - %d
                        ***Storage - %d
                        ****Price - %s
                        #Shop name - %s
                        ##Town - %s
                        """,
                this.getMacAddress(),
                df.format(this.getCpuSpeed()),
                this.getRam(),
                this.getStorage(),
                df.format(this.getPrice()),
                this.getShop().getName(),
                this.getShop().getTown().getName());
    }
}
