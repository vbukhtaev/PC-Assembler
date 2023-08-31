package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.bukhtaev.pcassembler.dto.dictionary.FanSizeDto;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;
import java.util.Set;

@Schema(description = "Кулер")
@Getter
public class CoolerDto extends NameableDto {

    @Schema(description = "Рассеиваемая мощность (в Вт)")
    @Min(1)
    @NotNull
    protected Integer powerDissipation;

    @Schema(description = "Высота (в мм)")
    @Min(1)
    @NotNull
    protected Integer height;

    protected NameableDto vendor;

    protected FanSizeDto fanSize;

    protected NameableDto fanPowerConnector;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<NameableDto> supportedSockets;

    @Builder
    public CoolerDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            @NotNull final Integer powerDissipation,
            @NotNull final Integer height,
            final NameableDto vendor,
            final FanSizeDto fanSize,
            final NameableDto fanPowerConnector,
            final Set<NameableDto> supportedSockets
    ) {
        super(id, createdAt, modifiedAt, name);
        this.powerDissipation = powerDissipation;
        this.height = height;
        this.vendor = vendor;
        this.fanSize = fanSize;
        this.fanPowerConnector = fanPowerConnector;
        this.supportedSockets = supportedSockets;
    }
}
