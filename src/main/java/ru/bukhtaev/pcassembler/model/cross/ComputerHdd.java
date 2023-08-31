package ru.bukhtaev.pcassembler.model.cross;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bukhtaev.pcassembler.model.ComputerBuild;
import ru.bukhtaev.pcassembler.model.Hdd;

@Entity
@Table(
        name = "computer_to_hdd",
        uniqueConstraints = @UniqueConstraint(columnNames = {"computer_id", "hdd_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ComputerHdd {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne
    @JoinColumn(name = "computer_id", referencedColumnName = "id", nullable = false)
    protected ComputerBuild computer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "hdd_id", referencedColumnName = "id", nullable = false)
    protected Hdd hdd;

    @Min(1)
    @NotNull
    @Column(name = "amount", nullable = false)
    protected Integer amount;
}
