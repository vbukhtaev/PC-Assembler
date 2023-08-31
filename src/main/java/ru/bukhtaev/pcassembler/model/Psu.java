package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.bukhtaev.pcassembler.model.cross.PsuCpuPowerConnector;
import ru.bukhtaev.pcassembler.model.cross.PsuGraphicsCardPowerConnector;
import ru.bukhtaev.pcassembler.model.cross.PsuStoragePowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.MainPowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.PsuCertificate;
import ru.bukhtaev.pcassembler.model.dictionary.PsuFormFactor;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "psu",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
public class Psu extends NameableEntity {

    @Min(1)
    @NotNull
    @Column(name = "power", nullable = false)
    protected Integer power;

    @Min(1)
    @NotNull
    @Column(name = "power_12v", nullable = false)
    protected Integer power12V;

    @Min(1)
    @NotNull
    @Column(name = "length", nullable = false)
    protected Integer length;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    protected Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "form_factor_id", referencedColumnName = "id", nullable = false)
    protected PsuFormFactor formFactor;

    @ManyToOne
    @JoinColumn(name = "certificate_id", referencedColumnName = "id", nullable = false)
    protected PsuCertificate certificate;

    @ManyToOne
    @JoinColumn(name = "main_power_connector_id", referencedColumnName = "id", nullable = false)
    protected MainPowerConnector mainPowerConnector;

    @NotNull
    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "psu", cascade = CascadeType.ALL)
    protected Set<@Valid @NotNull PsuCpuPowerConnector> cpuPowerConnectors = new HashSet<>();

    @NotNull
    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "psu", cascade = CascadeType.ALL)
    protected Set<@Valid @NotNull PsuGraphicsCardPowerConnector> graphicsCardPowerConnectors = new HashSet<>();

    @NotNull
    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "psu", cascade = CascadeType.ALL)
    protected Set<@Valid @NotNull PsuStoragePowerConnector> storagePowerConnectors = new HashSet<>();

    @Builder
    public Psu(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final @NotNull Integer power,
            final @NotNull Integer power12V,
            final @NotNull Integer length,
            final Vendor vendor,
            final PsuFormFactor formFactor,
            final PsuCertificate certificate,
            final MainPowerConnector mainPowerConnector,
            final Set<@Valid @NotNull PsuCpuPowerConnector> cpuPowerConnectors,
            final Set<@Valid @NotNull PsuGraphicsCardPowerConnector> graphicsCardPowerConnectors,
            final Set<@Valid @NotNull PsuStoragePowerConnector> storagePowerConnectors
    ) {
        super(id, createdAt, modifiedAt, name);
        this.power = power;
        this.power12V = power12V;
        this.length = length;
        this.vendor = vendor;
        this.formFactor = formFactor;
        this.certificate = certificate;
        this.mainPowerConnector = mainPowerConnector;
        this.cpuPowerConnectors = cpuPowerConnectors;
        this.graphicsCardPowerConnectors = graphicsCardPowerConnectors;
        this.storagePowerConnectors = storagePowerConnectors;
    }
}
