package ru.bukhtaev.pcassembler.dto.cross.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.PsuCpuPowerConnectorDto;
import ru.bukhtaev.pcassembler.dto.cross.PsuCpuPowerConnectorRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.cross.PsuCpuPowerConnector;

@Mapper
public abstract class PsuCpuPowerConnectorMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return PsuCpuPowerConnector.class.equals(clazz);
    }

    @Mapping(target = "psu.cpuPowerConnectors", ignore = true)
    @Mapping(target = "psu.storagePowerConnectors", ignore = true)
    @Mapping(target = "psu.graphicsCardPowerConnectors", ignore = true)
    public abstract PsuCpuPowerConnectorDto toDto(PsuCpuPowerConnector entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "psu", ignore = true)
    @Mapping(source = "cpuPowerConnectorId", target = "cpuPowerConnector.id")
    public abstract PsuCpuPowerConnector fromDto(PsuCpuPowerConnectorRequestDto dto);
}
