package ru.bukhtaev.pcassembler.model.cross;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bukhtaev.pcassembler.model.ComputerBuild;
import ru.bukhtaev.pcassembler.model.Fan;

@Entity
@Table(
        name = "computer_to_fan",
        uniqueConstraints = @UniqueConstraint(columnNames = {"computer_id", "fan_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ComputerFan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne
    @JoinColumn(name = "computer_id", referencedColumnName = "id", nullable = false)
    protected ComputerBuild computer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fan_id", referencedColumnName = "id", nullable = false)
    protected Fan fan;

    @Min(1)
    @NotNull
    @Column(name = "amount", nullable = false)
    protected Integer amount;
}
