package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;

@Schema(description = "Вариант исполнения")
@Getter
public class DesignDto extends NameableDto {

    protected NameableDto vendor;

    @Builder
    public DesignDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final NameableDto vendor
    ) {
        super(id, createdAt, modifiedAt, name);
        this.vendor = vendor;
    }
}
