package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;

@Schema(description = "Модуль оперативной памяти")
@Getter
public class RamModuleDto extends BaseDto {

    @Schema(description = "Частота (в МГц)")
    @Min(800)
    @NotNull
    protected Integer clock;

    @Schema(description = "Объем (в Мб)")
    @Min(512)
    @NotNull
    protected Integer capacity;

    protected NameableDto type;

    protected DesignDto design;

    @Builder
    public RamModuleDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final @NotNull Integer clock,
            final @NotNull Integer capacity,
            final NameableDto type,
            final DesignDto design
    ) {
        super(id, createdAt, modifiedAt);
        this.clock = clock;
        this.capacity = capacity;
        this.type = type;
        this.design = design;
    }
}
