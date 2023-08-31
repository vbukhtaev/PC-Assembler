package ru.bukhtaev.pcassembler.model.cross;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bukhtaev.pcassembler.model.ComputerCase;
import ru.bukhtaev.pcassembler.model.dictionary.ExpansionBay;

@Entity
@Table(
        name = "computer_case_to_expansion_bay",
        uniqueConstraints = @UniqueConstraint(columnNames = {"computer_case_id", "expansion_bay_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ComputerCaseExpansionBay {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne
    @JoinColumn(name = "computer_case_id", referencedColumnName = "id", nullable = false)
    protected ComputerCase computerCase;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "expansion_bay_id", referencedColumnName = "id", nullable = false)
    protected ExpansionBay expansionBay;

    @Min(1)
    @NotNull
    @Column(name = "amount", nullable = false)
    protected Integer amount;
}
