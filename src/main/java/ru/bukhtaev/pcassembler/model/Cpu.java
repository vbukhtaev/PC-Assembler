package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.bukhtaev.pcassembler.model.cross.CpuRamType;
import ru.bukhtaev.pcassembler.model.dictionary.Manufacturer;
import ru.bukhtaev.pcassembler.model.dictionary.Socket;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "cpu",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cpu extends NameableEntity {

    @Min(1)
    @NotNull
    @Column(name = "core_count", nullable = false)
    protected Integer coreCount;

    @Min(1)
    @NotNull
    @Column(name = "thread_count", nullable = false)
    protected Integer threadCount;

    @Min(100)
    @NotNull
    @Column(name = "base_clock", nullable = false)
    protected Integer baseClock;

    @Min(100)
    @NotNull
    @Column(name = "max_clock", nullable = false)
    protected Integer maxClock;

    @Min(0)
    @NotNull
    @Column(name = "l3cache_size", nullable = false)
    protected Integer l3CacheSize;

    @Min(1)
    @NotNull
    @Column(name = "max_tdp", nullable = false)
    protected Integer maxTdp;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", referencedColumnName = "id", nullable = false)
    protected Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "socket_id", referencedColumnName = "id", nullable = false)
    protected Socket socket;

    @NotNull
    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "cpu", cascade = CascadeType.ALL)
    protected Set<@Valid @NotNull CpuRamType> supportedRamTypes = new HashSet<>();

    @Builder
    public Cpu(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final @NotNull Integer coreCount,
            final @NotNull Integer threadCount,
            final @NotNull Integer baseClock,
            final @NotNull Integer maxClock,
            final @NotNull Integer l3CacheSize,
            final @NotNull Integer maxTdp,
            final Manufacturer manufacturer,
            final Socket socket,
            final Set<@Valid @NotNull CpuRamType> supportedRamTypes
    ) {
        super(id, createdAt, modifiedAt, name);
        this.coreCount = coreCount;
        this.threadCount = threadCount;
        this.baseClock = baseClock;
        this.maxClock = maxClock;
        this.l3CacheSize = l3CacheSize;
        this.maxTdp = maxTdp;
        this.manufacturer = manufacturer;
        this.socket = socket;
        this.supportedRamTypes = supportedRamTypes;
    }
}
