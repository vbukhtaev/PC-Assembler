package ru.bukhtaev.pcassembler.dto.cross;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Количество поддерживаемых материнской платой разъемов накопителей")
@Getter
public class MotherboardStorageConnectorRequestDto {

    @UUID
    @NotBlank
    protected String storageConnectorId;

    @Schema(description = "Количество разъемов накопителей")
    @Min(1)
    @NotNull
    protected Integer amount;

    @Builder
    public MotherboardStorageConnectorRequestDto(
            final String storageConnectorId,
            final @NotNull Integer amount
    ) {
        this.storageConnectorId = storageConnectorId;
        this.amount = amount;
    }
}
