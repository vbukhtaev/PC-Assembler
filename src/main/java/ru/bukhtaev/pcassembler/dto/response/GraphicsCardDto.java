package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;

@Schema(description = "Видеокарта")
@Getter
public class GraphicsCardDto extends BaseDto {

    @Schema(description = "Длина (в мм)")
    @Min(40)
    @NotNull
    protected Integer length;

    protected GpuDto gpu;

    protected DesignDto design;

    protected NameableDto pciExpressConnectorVersion;

    protected NameableDto powerConnector;

    @Builder
    public GraphicsCardDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final @NotNull Integer length,
            final GpuDto gpu,
            final DesignDto design,
            final NameableDto pciExpressConnectorVersion,
            final NameableDto powerConnector
    ) {
        super(id, createdAt, modifiedAt);
        this.length = length;
        this.gpu = gpu;
        this.design = design;
        this.pciExpressConnectorVersion = pciExpressConnectorVersion;
        this.powerConnector = powerConnector;
    }
}
