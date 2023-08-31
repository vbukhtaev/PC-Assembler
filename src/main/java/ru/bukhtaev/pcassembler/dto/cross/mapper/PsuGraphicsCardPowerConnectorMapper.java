package ru.bukhtaev.pcassembler.dto.cross.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.PsuGraphicsCardPowerConnectorDto;
import ru.bukhtaev.pcassembler.dto.cross.PsuGraphicsCardPowerConnectorRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.cross.PsuGraphicsCardPowerConnector;

@Mapper
public abstract class PsuGraphicsCardPowerConnectorMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return PsuGraphicsCardPowerConnector.class.equals(clazz);
    }

    @Mapping(target = "psu.cpuPowerConnectors", ignore = true)
    @Mapping(target = "psu.storagePowerConnectors", ignore = true)
    @Mapping(target = "psu.graphicsCardPowerConnectors", ignore = true)
    public abstract PsuGraphicsCardPowerConnectorDto toDto(PsuGraphicsCardPowerConnector entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "psu", ignore = true)
    @Mapping(source = "graphicsCardPowerConnectorId", target = "graphicsCardPowerConnector.id")
    public abstract PsuGraphicsCardPowerConnector fromDto(PsuGraphicsCardPowerConnectorRequestDto dto);
}
