package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.bukhtaev.pcassembler.model.cross.ComputerFan;
import ru.bukhtaev.pcassembler.model.cross.ComputerHdd;
import ru.bukhtaev.pcassembler.model.cross.ComputerRamModule;
import ru.bukhtaev.pcassembler.model.cross.ComputerSsd;

import java.util.Date;
import java.util.Set;

@Entity
@Table(
        name = "computer_build",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ComputerBuild extends NameableEntity {

    @ManyToOne
    @JoinColumn(name = "motherboard_id", referencedColumnName = "id", nullable = false)
    protected Motherboard motherboard;

    @ManyToOne
    @JoinColumn(name = "cpu_id", referencedColumnName = "id", nullable = false)
    protected Cpu cpu;

    @ManyToOne
    @JoinColumn(name = "cooler_id", referencedColumnName = "id", nullable = false)
    protected Cooler cooler;

    @ManyToOne
    @JoinColumn(name = "graphics_card_id", referencedColumnName = "id", nullable = false)
    protected GraphicsCard graphicsCard;

    @ManyToOne
    @JoinColumn(name = "psu_id", referencedColumnName = "id", nullable = false)
    protected Psu psu;

    @ManyToOne
    @JoinColumn(name = "computer_case_id", referencedColumnName = "id", nullable = false)
    protected ComputerCase computerCase;

    @NotNull
    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "computer", cascade = CascadeType.ALL)
    protected Set<ComputerRamModule> ramModules;

    @NotNull
    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "computer", cascade = CascadeType.ALL)
    protected Set<ComputerHdd> hdds;

    @NotNull
    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "computer", cascade = CascadeType.ALL)
    protected Set<ComputerSsd> ssds;

    @NotNull
    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "computer", cascade = CascadeType.ALL)
    protected Set<ComputerFan> fans;

    public ComputerBuild(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final Motherboard motherboard,
            final Cpu cpu,
            final Cooler cooler,
            final GraphicsCard graphicsCard,
            final Psu psu,
            final ComputerCase computerCase,
            final @NotNull Set<ComputerRamModule> ramModules,
            final @NotNull Set<ComputerHdd> hdds,
            final @NotNull Set<ComputerSsd> ssds,
            final @NotNull Set<ComputerFan> fans
    ) {
        super(id, createdAt, modifiedAt, name);
        this.motherboard = motherboard;
        this.cpu = cpu;
        this.cooler = cooler;
        this.graphicsCard = graphicsCard;
        this.psu = psu;
        this.computerCase = computerCase;
        this.ramModules = ramModules;
        this.hdds = hdds;
        this.ssds = ssds;
        this.fans = fans;
    }
}
