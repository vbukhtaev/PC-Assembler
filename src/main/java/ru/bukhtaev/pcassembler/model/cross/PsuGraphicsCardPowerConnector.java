package ru.bukhtaev.pcassembler.model.cross;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bukhtaev.pcassembler.model.Psu;
import ru.bukhtaev.pcassembler.model.dictionary.GraphicsCardPowerConnector;

@Entity
@Table(
        name = "psu_to_graphics_card_power_connector",
        uniqueConstraints = @UniqueConstraint(columnNames = {"psu_id", "graphics_card_power_connector_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PsuGraphicsCardPowerConnector {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne
    @JoinColumn(name = "psu_id", referencedColumnName = "id", nullable = false)
    protected Psu psu;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "graphics_card_power_connector_id", referencedColumnName = "id", nullable = false)
    protected GraphicsCardPowerConnector graphicsCardPowerConnector;

    @Min(1)
    @NotNull
    @Column(name = "amount", nullable = false)
    protected Integer amount;
}
