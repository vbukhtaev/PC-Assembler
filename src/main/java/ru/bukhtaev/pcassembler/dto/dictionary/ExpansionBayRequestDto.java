package ru.bukhtaev.pcassembler.dto.dictionary;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Schema(description = "Отсек расширения")
@Getter
public class ExpansionBayRequestDto {

    @Schema(description = "Размер (в ″)")
    @Pattern(regexp = "[1-9][.]\\d{1,2}")
    protected String size;
}
