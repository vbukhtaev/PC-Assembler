package ru.bukhtaev.pcassembler.model.cross;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bukhtaev.pcassembler.model.Cpu;
import ru.bukhtaev.pcassembler.model.dictionary.RamType;

@Entity
@Table(
        name = "cpu_to_ram_type",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cpu_id", "ram_type_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CpuRamType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne
    @JoinColumn(name = "cpu_id", referencedColumnName = "id", nullable = false)
    protected Cpu cpu;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ram_type_id", referencedColumnName = "id", nullable = false)
    protected RamType ramType;

    @Min(1333)
    @NotNull
    @Column(name = "max_memory_clock", nullable = false)
    protected Integer maxMemoryClock;
}
