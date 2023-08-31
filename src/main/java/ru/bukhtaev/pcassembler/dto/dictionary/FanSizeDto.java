package ru.bukhtaev.pcassembler.dto.dictionary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import ru.bukhtaev.pcassembler.dto.response.BaseDto;

import java.util.Date;

@Schema(description = "Размер вентилятора")
@Getter
public class FanSizeDto extends BaseDto {

    @Schema(description = "Длина (в мм)")
    protected Integer length;

    @Schema(description = "Ширина (в мм)")
    protected Integer width;

    @Schema(description = "Толщина (в мм)")
    protected Integer height;

    @Builder
    public FanSizeDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final Integer length,
            final Integer width,
            final Integer height
    ) {
        super(id, createdAt, modifiedAt);
        this.length = length;
        this.width = width;
        this.height = height;
    }
}
