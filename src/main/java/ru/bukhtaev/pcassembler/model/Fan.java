package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import lombok.*;
import ru.bukhtaev.pcassembler.model.dictionary.FanPowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.FanSize;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;

import java.util.Date;

@Entity
@Table(
        name = "fan",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Fan extends NameableEntity {

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    protected Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "size_id", referencedColumnName = "id", nullable = false)
    protected FanSize size;

    @ManyToOne
    @JoinColumn(name = "power_connector_id", referencedColumnName = "id", nullable = false)
    protected FanPowerConnector powerConnector;

    @Builder
    public Fan(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final Vendor vendor,
            final FanSize size,
            final FanPowerConnector powerConnector
    ) {
        super(id, createdAt, modifiedAt, name);
        this.vendor = vendor;
        this.size = size;
        this.powerConnector = powerConnector;
    }
}
