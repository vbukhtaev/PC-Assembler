package ru.bukhtaev.pcassembler.dto.dictionary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Размер вентилятора")
@Getter
public class FanSizeRequestDto {

    @Schema(description = "Длина (в мм)")
    protected Integer length;

    @Schema(description = "Ширина (в мм)")
    protected Integer width;

    @Schema(description = "Толщина (в мм)")
    protected Integer height;
}
