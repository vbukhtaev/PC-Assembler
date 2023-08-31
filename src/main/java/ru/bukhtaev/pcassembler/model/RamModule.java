package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.model.dictionary.RamType;

import java.util.Date;

@Entity
@Table(name = "ram_module")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RamModule extends BaseEntity {

    @Min(1333)
    @NotNull
    @Column(name = "clock", nullable = false)
    protected Integer clock;

    @Min(512)
    @NotNull
    @Column(name = "capacity", nullable = false)
    protected Integer capacity;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false)
    protected RamType type;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "id", nullable = false)
    protected Design design;

    @Builder
    public RamModule(
            final @UUID String id,
            final Date createdAt,
            final Date modifiedAt,
            final @NotNull Integer clock,
            final @NotNull Integer capacity,
            final RamType type,
            final Design design
    ) {
        super(id, createdAt, modifiedAt);
        this.clock = clock;
        this.capacity = capacity;
        this.type = type;
        this.design = design;
    }
}
