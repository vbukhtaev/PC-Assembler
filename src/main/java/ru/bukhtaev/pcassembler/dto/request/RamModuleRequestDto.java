package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Модуль оперативной памяти")
@Getter
public class RamModuleRequestDto {

    @Schema(description = "Частота (в МГц)")
    @Min(800)
    @NotNull
    protected Integer clock;

    @Schema(description = "Объем (в Мб)")
    @Min(512)
    @NotNull
    protected Integer capacity;

    @UUID
    @NotBlank
    protected String typeId;

    @UUID
    @NotBlank
    protected String designId;

    @Builder
    public RamModuleRequestDto(
            final @NotNull Integer clock,
            final @NotNull Integer capacity,
            final String typeId,
            final String designId
    ) {
        this.clock = clock;
        this.capacity = capacity;
        this.typeId = typeId;
        this.designId = designId;
    }
}
