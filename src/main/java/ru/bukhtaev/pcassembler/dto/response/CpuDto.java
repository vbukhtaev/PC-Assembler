package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.bukhtaev.pcassembler.dto.cross.CpuRamTypeDto;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;
import java.util.Set;

@Schema(description = "Процессор")
@Getter
public class CpuDto extends NameableDto {

    @Schema(description = "Количество ядер")
    @Min(1)
    @NotNull
    protected Integer coreCount;

    @Schema(description = "Количество потоков")
    @Min(1)
    @NotNull
    protected Integer threadCount;

    @Schema(description = "Базовая частота (в МГц)")
    @Min(100)
    @NotNull
    protected Integer baseClock;

    @Schema(description = "Максимальная частота (в МГц)")
    @Min(100)
    @NotNull
    protected Integer maxClock;

    @Schema(description = "Объем L3 кэша (в Мб)")
    @Min(0)
    @NotNull
    protected Integer l3CacheSize;

    @Schema(description = "Максимальное тепловыделение (в Вт)")
    @Min(1)
    @NotNull
    protected Integer maxTdp;

    protected NameableDto manufacturer;

    protected NameableDto socket;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<CpuRamTypeDto> supportedRamTypes;

    @Builder
    public CpuDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            @NotNull final Integer coreCount,
            @NotNull final Integer threadCount,
            @NotNull final Integer baseClock,
            @NotNull final Integer maxClock,
            @NotNull final Integer l3CacheSize,
            @NotNull final Integer maxTdp,
            final NameableDto manufacturer,
            final NameableDto socket,
            final Set<CpuRamTypeDto> supportedRamTypes
    ) {
        super(id, createdAt, modifiedAt, name);
        this.coreCount = coreCount;
        this.threadCount = threadCount;
        this.baseClock = baseClock;
        this.maxClock = maxClock;
        this.l3CacheSize = l3CacheSize;
        this.maxTdp = maxTdp;
        this.manufacturer = manufacturer;
        this.socket = socket;
        this.supportedRamTypes = supportedRamTypes;
    }
}
