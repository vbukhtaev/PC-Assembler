package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.bukhtaev.pcassembler.model.dictionary.FanPowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.FanSize;
import ru.bukhtaev.pcassembler.model.dictionary.Socket;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "cooler",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cooler extends NameableEntity {

    @Min(1)
    @NotNull
    @Column(name = "power_dissipation", nullable = false)
    protected Integer powerDissipation;

    @Min(1)
    @NotNull
    @Column(name = "height", nullable = false)
    protected Integer height;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    protected Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "fan_size_id", referencedColumnName = "id", nullable = false)
    protected FanSize fanSize;

    @ManyToOne
    @JoinColumn(name = "power_connector_id", referencedColumnName = "id", nullable = false)
    protected FanPowerConnector fanPowerConnector;

    @Size(min = 1)
    @ManyToMany
    @JoinTable(
            name = "cooler_to_socket",
            joinColumns = @JoinColumn(name = "cooler_id"),
            inverseJoinColumns = @JoinColumn(name = "socket_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<Socket> supportedSockets = new HashSet<>();

    @Builder
    public Cooler(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final @NotNull Integer powerDissipation,
            final @NotNull Integer height,
            final Vendor vendor,
            final FanSize fanSize,
            final FanPowerConnector fanPowerConnector,
            final Set<Socket> supportedSockets
    ) {
        super(id, createdAt, modifiedAt, name);
        this.powerDissipation = powerDissipation;
        this.height = height;
        this.vendor = vendor;
        this.fanSize = fanSize;
        this.fanPowerConnector = fanPowerConnector;
        this.supportedSockets = supportedSockets;
    }
}
