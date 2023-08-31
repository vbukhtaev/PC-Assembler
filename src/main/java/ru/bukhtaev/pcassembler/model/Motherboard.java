package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.model.cross.MotherboardStorageConnector;
import ru.bukhtaev.pcassembler.model.dictionary.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "motherboard")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Motherboard extends BaseEntity {

    @Min(1333)
    @NotNull
    @Column(name = "max_memory_clock", nullable = false)
    protected Integer maxMemoryClock;

    @Min(1333)
    @NotNull
    @Column(name = "max_memory_over_clock", nullable = false)
    protected Integer maxMemoryOverClock;

    @Min(1)
    @NotNull
    @Column(name = "memory_slots_count", nullable = false)
    protected Integer slotsCount;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "id", nullable = false)
    protected Design design;

    @ManyToOne
    @JoinColumn(name = "chipset_id", referencedColumnName = "id", nullable = false)
    protected Chipset chipset;

    @ManyToOne
    @JoinColumn(name = "ram_type_id", referencedColumnName = "id", nullable = false)
    protected RamType ramType;

    @ManyToOne
    @JoinColumn(name = "form_factor_id", referencedColumnName = "id", nullable = false)
    protected MotherboardFormFactor formFactor;

    @ManyToOne
    @JoinColumn(name = "cpu_power_connector_id", referencedColumnName = "id", nullable = false)
    protected CpuPowerConnector cpuPowerConnector;

    @ManyToOne
    @JoinColumn(name = "main_power_connector_id", referencedColumnName = "id", nullable = false)
    protected MainPowerConnector mainPowerConnector;

    @ManyToOne
    @JoinColumn(name = "fan_power_connector_id", referencedColumnName = "id", nullable = false)
    protected FanPowerConnector fanPowerConnector;

    @ManyToOne
    @JoinColumn(name = "pci_express_connector_version_id", referencedColumnName = "id", nullable = false)
    protected PciExpressConnectorVersion pciExpressConnectorVersion;

    @NotNull
    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "motherboard", cascade = CascadeType.ALL)
    protected Set<@Valid @NotNull MotherboardStorageConnector> storageConnectors = new HashSet<>();

    @Builder
    public Motherboard(
            final @UUID String id,
            final Date createdAt,
            final Date modifiedAt,
            final @NotNull Integer maxMemoryClock,
            final @NotNull Integer maxMemoryOverClock,
            final @NotNull Integer slotsCount,
            final Design design,
            final Chipset chipset,
            final RamType ramType,
            final MotherboardFormFactor formFactor,
            final CpuPowerConnector cpuPowerConnector,
            final MainPowerConnector mainPowerConnector,
            final FanPowerConnector fanPowerConnector,
            final PciExpressConnectorVersion pciExpressConnectorVersion,
            final @NotNull Set<@Valid @NotNull MotherboardStorageConnector> storageConnectors
    ) {
        super(id, createdAt, modifiedAt);
        this.maxMemoryClock = maxMemoryClock;
        this.maxMemoryOverClock = maxMemoryOverClock;
        this.slotsCount = slotsCount;
        this.design = design;
        this.chipset = chipset;
        this.ramType = ramType;
        this.formFactor = formFactor;
        this.cpuPowerConnector = cpuPowerConnector;
        this.mainPowerConnector = mainPowerConnector;
        this.fanPowerConnector = fanPowerConnector;
        this.pciExpressConnectorVersion = pciExpressConnectorVersion;
        this.storageConnectors = storageConnectors;
    }
}
