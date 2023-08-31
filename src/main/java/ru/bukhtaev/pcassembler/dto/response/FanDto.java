package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;
import ru.bukhtaev.pcassembler.model.dictionary.FanPowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.FanSize;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;

import java.util.Date;

@Schema(description = "Вентилятор")
@Getter
public class FanDto extends NameableDto {

    protected Vendor vendor;

    protected FanSize size;

    protected FanPowerConnector powerConnector;

    @Builder
    public FanDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final Vendor vendor,
            final FanSize size,
            final FanPowerConnector powerConnector
    ) {
        super(id, createdAt, modifiedAt, name);
        this.vendor = vendor;
        this.size = size;
        this.powerConnector = powerConnector;
    }
}
