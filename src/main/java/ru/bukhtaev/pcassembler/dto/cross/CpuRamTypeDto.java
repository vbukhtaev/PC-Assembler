package ru.bukhtaev.pcassembler.dto.cross;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;
import ru.bukhtaev.pcassembler.dto.response.CpuDto;

@Schema(description = "Максимальная поддерживаемая процессором частота оперативной памяти")
@Getter
@Setter
public class CpuRamTypeDto {

    @Schema(description = "ID")
    protected String id;

    @JsonIgnoreProperties({
            "name",
            "createdAt",
            "modifiedAt",
            "coreCount",
            "threadCount",
            "baseClock",
            "maxClock",
            "l3CacheSize",
            "maxTdp",
            "manufacturer",
            "socket",
            "supportedRamTypes"
    })
    protected CpuDto cpu;

    @JsonIgnoreProperties({
            "createdAt",
            "modifiedAt"
    })
    protected NameableDto ramType;

    @Schema(description = "Максимальная частота оперативной памяти (в МГц)")
    @Min(1333)
    @NotNull
    protected Integer maxMemoryClock;
}
