package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.model.dictionary.GraphicsCardPowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.PciExpressConnectorVersion;

import java.util.Date;

@Entity
@Table(name = "graphics_card")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GraphicsCard extends BaseEntity {

    @Min(40)
    @NotNull
    @Column(name = "length", nullable = false)
    protected Integer length;

    @ManyToOne
    @JoinColumn(name = "gpu_id", referencedColumnName = "id", nullable = false)
    protected Gpu gpu;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "id", nullable = false)
    protected Design design;

    @ManyToOne
    @JoinColumn(name = "power_connector_id", referencedColumnName = "id", nullable = false)
    protected GraphicsCardPowerConnector powerConnector;

    @ManyToOne
    @JoinColumn(name = "pci_express_connector_version_id", referencedColumnName = "id", nullable = false)
    protected PciExpressConnectorVersion pciExpressConnectorVersion;

    @Builder
    public GraphicsCard(
            final @UUID String id,
            final Date createdAt,
            final Date modifiedAt,
            final @NotNull Integer length,
            final Gpu gpu,
            final Design design,
            final GraphicsCardPowerConnector powerConnector,
            final PciExpressConnectorVersion pciExpressConnectorVersion
    ) {
        super(id, createdAt, modifiedAt);
        this.length = length;
        this.gpu = gpu;
        this.design = design;
        this.powerConnector = powerConnector;
        this.pciExpressConnectorVersion = pciExpressConnectorVersion;
    }
}
