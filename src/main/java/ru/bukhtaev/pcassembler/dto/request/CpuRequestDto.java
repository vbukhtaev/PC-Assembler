package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.dto.cross.CpuRamTypeRequestDto;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;

import java.util.Set;

@Schema(description = "Процессор")
@Getter
public class CpuRequestDto extends NameableRequestDto {

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

    @UUID
    @NotBlank
    protected String manufacturerId;

    @UUID
    @NotBlank
    protected String socketId;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<CpuRamTypeRequestDto> supportedRamTypes;

    @Builder
    public CpuRequestDto(
            final String name,
            final @NotNull Integer coreCount,
            final @NotNull Integer threadCount,
            final @NotNull Integer baseClock,
            final @NotNull Integer maxClock,
            final @NotNull Integer l3CacheSize,
            final @NotNull Integer maxTdp,
            final String manufacturerId,
            final String socketId,
            final Set<CpuRamTypeRequestDto> supportedRamTypes
    ) {
        super(name);
        this.coreCount = coreCount;
        this.threadCount = threadCount;
        this.baseClock = baseClock;
        this.maxClock = maxClock;
        this.l3CacheSize = l3CacheSize;
        this.maxTdp = maxTdp;
        this.manufacturerId = manufacturerId;
        this.socketId = socketId;
        this.supportedRamTypes = supportedRamTypes;
    }
}
