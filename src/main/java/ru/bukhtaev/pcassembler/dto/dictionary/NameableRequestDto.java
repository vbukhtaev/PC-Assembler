package ru.bukhtaev.pcassembler.dto.dictionary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NameableRequestDto {

    @Schema(description = "Название")
    protected String name;
}
