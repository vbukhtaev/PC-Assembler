package ru.bukhtaev.pcassembler.dto.cross;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Количество поддерживаемых блоком питания разъемов питания видеокарты")
@Getter
public class PsuGraphicsCardPowerConnectorRequestDto {

    @UUID
    @NotBlank
    protected String graphicsCardPowerConnectorId;

    @Schema(description = "Количество разъемов питания видеокарты")
    @Min(1)
    @NotNull
    protected Integer amount;

    @Builder
    public PsuGraphicsCardPowerConnectorRequestDto(
            final String graphicsCardPowerConnectorId,
            final @NotNull Integer amount
    ) {
        this.graphicsCardPowerConnectorId = graphicsCardPowerConnectorId;
        this.amount = amount;
    }
}
