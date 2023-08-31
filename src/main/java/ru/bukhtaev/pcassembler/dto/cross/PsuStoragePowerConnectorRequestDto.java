package ru.bukhtaev.pcassembler.dto.cross;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Количество поддерживаемых блоком питания разъемов питания накопителей")
@Getter
public class PsuStoragePowerConnectorRequestDto {

    @UUID
    @NotBlank
    protected String storagePowerConnectorId;

    @Schema(description = "Количество разъемов накопителей")
    @Min(1)
    @NotNull
    protected Integer amount;

    @Builder
    public PsuStoragePowerConnectorRequestDto(
            final String storagePowerConnectorId,
            final @NotNull Integer amount
    ) {
        this.storagePowerConnectorId = storagePowerConnectorId;
        this.amount = amount;
    }
}
