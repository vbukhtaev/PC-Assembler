package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Видеокарта")
@Getter
public class GraphicsCardRequestDto {

    @Schema(description = "Длина (в мм)")
    @Min(40)
    @NotNull
    protected Integer length;

    @UUID
    @NotBlank
    protected String gpuId;

    @UUID
    @NotBlank
    protected String designId;

    @UUID
    @NotBlank
    protected String pciExpressConnectorVersionId;

    @UUID
    @NotBlank
    protected String powerConnectorId;

    @Builder
    public GraphicsCardRequestDto(
            final @NotNull Integer length,
            final String gpuId,
            final String designId,
            final String pciExpressConnectorVersionId,
            final String powerConnectorId
    ) {
        this.length = length;
        this.gpuId = gpuId;
        this.designId = designId;
        this.pciExpressConnectorVersionId = pciExpressConnectorVersionId;
        this.powerConnectorId = powerConnectorId;
    }
}
