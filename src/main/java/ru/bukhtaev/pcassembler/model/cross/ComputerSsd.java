package ru.bukhtaev.pcassembler.model.cross;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bukhtaev.pcassembler.model.ComputerBuild;
import ru.bukhtaev.pcassembler.model.Ssd;

@Entity
@Table(
        name = "computer_to_ssd",
        uniqueConstraints = @UniqueConstraint(columnNames = {"computer_id", "ssd_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ComputerSsd {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne
    @JoinColumn(name = "computer_id", referencedColumnName = "id", nullable = false)
    protected ComputerBuild computer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ssd_id", referencedColumnName = "id", nullable = false)
    protected Ssd ssd;

    @Min(1)
    @NotNull
    @Column(name = "amount", nullable = false)
    protected Integer amount;
}
