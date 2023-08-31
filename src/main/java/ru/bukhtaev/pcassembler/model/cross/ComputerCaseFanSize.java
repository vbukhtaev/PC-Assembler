package ru.bukhtaev.pcassembler.model.cross;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bukhtaev.pcassembler.model.ComputerCase;
import ru.bukhtaev.pcassembler.model.dictionary.FanSize;

@Entity
@Table(
        name = "computer_case_to_fan_size",
        uniqueConstraints = @UniqueConstraint(columnNames = {"computer_case_id", "fan_size_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ComputerCaseFanSize {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne
    @JoinColumn(name = "computer_case_id", referencedColumnName = "id", nullable = false)
    protected ComputerCase computerCase;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fan_size_id", referencedColumnName = "id", nullable = false)
    protected FanSize fanSize;

    @Min(1)
    @NotNull
    @Column(name = "amount", nullable = false)
    protected Integer amount;
}
