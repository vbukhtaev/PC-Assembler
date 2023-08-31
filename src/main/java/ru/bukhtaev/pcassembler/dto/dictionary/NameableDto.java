package ru.bukhtaev.pcassembler.dto.dictionary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.bukhtaev.pcassembler.dto.response.BaseDto;

import java.util.Date;

@Getter
public class NameableDto extends BaseDto {

    @Schema(description = "Название")
    protected String name;

    public NameableDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name
    ) {
        super(id, createdAt, modifiedAt);
        this.name = name;
    }
}
