package ru.bukhtaev.pcassembler.dto.cross;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Количество устанавливаемых в ПК вентиляторов")
@Getter
public class ComputerFanRequestDto {

    @UUID
    @NotBlank
    protected String fanId;

    @Schema(description = "Количество")
    @Min(1)
    @NotNull
    protected Integer amount;

    @Builder
    public ComputerFanRequestDto(
            final String fanId,
            final @NotNull Integer amount
    ) {
        this.fanId = fanId;
        this.amount = amount;
    }
}