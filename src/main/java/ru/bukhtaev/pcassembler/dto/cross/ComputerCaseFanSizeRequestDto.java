package ru.bukhtaev.pcassembler.dto.cross;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Количество поддерживаемых корпусом размеров вентиляторов")
@Getter
public class ComputerCaseFanSizeRequestDto {

    @UUID
    @NotBlank
    protected String fanSizeId;

    @Schema(description = "Количество размеров вентиляторов")
    @Min(1)
    @NotNull
    protected Integer amount;

    @Builder
    public ComputerCaseFanSizeRequestDto(
            final String fanSizeId,
            final @NotNull Integer amount
    ) {
        this.fanSizeId = fanSizeId;
        this.amount = amount;
    }
}
