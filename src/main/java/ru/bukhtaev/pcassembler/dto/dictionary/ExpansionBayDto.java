package ru.bukhtaev.pcassembler.dto.dictionary;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import ru.bukhtaev.pcassembler.dto.response.BaseDto;

import java.util.Date;

@Schema(description = "Отсек расширения")
@Getter
public class ExpansionBayDto extends BaseDto {

    @Schema(description = "Размер (в ″)")
    @Pattern(regexp = "[1-9][.]\\d{1,2}")
    protected String size;

    @Builder
    public ExpansionBayDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String size
    ) {
        super(id, createdAt, modifiedAt);
        this.size = size;
    }
}
