package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.bukhtaev.pcassembler.model.dictionary.Manufacturer;
import ru.bukhtaev.pcassembler.model.dictionary.VideoMemoryType;

import java.util.Date;

@Entity
@Table(
        name = "gpu",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "memory_size"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
public class Gpu extends NameableEntity {

    @Min(64)
    @NotNull
    @Column(name = "memory_size", nullable = false)
    protected Integer memorySize;

    @Min(1)
    @NotNull
    @Column(name = "power_consumption", nullable = false)
    protected Integer powerConsumption;

    @ManyToOne
    @JoinColumn(name = "memory_type_id", referencedColumnName = "id", nullable = false)
    protected VideoMemoryType memoryType;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", referencedColumnName = "id", nullable = false)
    protected Manufacturer manufacturer;

    @Builder
    public Gpu(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final @NotNull Integer memorySize,
            final @NotNull Integer powerConsumption,
            final VideoMemoryType memoryType,
            final Manufacturer manufacturer
    ) {
        super(id, createdAt, modifiedAt, name);
        this.memorySize = memorySize;
        this.powerConsumption = powerConsumption;
        this.memoryType = memoryType;
        this.manufacturer = manufacturer;
    }
}
