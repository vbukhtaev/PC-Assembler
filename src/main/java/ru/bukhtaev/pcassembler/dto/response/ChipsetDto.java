package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;

@Schema(description = "Чипсет")
@Getter
public class ChipsetDto extends NameableDto {

    protected NameableDto socket;

    @Builder
    public ChipsetDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final NameableDto socket
    ) {
        super(id, createdAt, modifiedAt, name);
        this.socket = socket;
    }
}
