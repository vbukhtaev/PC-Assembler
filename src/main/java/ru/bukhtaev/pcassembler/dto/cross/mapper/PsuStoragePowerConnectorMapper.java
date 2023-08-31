package ru.bukhtaev.pcassembler.dto.cross.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.PsuStoragePowerConnectorDto;
import ru.bukhtaev.pcassembler.dto.cross.PsuStoragePowerConnectorRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.cross.PsuStoragePowerConnector;

@Mapper
public abstract class PsuStoragePowerConnectorMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return PsuStoragePowerConnector.class.equals(clazz);
    }

    @Mapping(target = "psu.cpuPowerConnectors", ignore = true)
    @Mapping(target = "psu.storagePowerConnectors", ignore = true)
    @Mapping(target = "psu.graphicsCardPowerConnectors", ignore = true)
    public abstract PsuStoragePowerConnectorDto toDto(PsuStoragePowerConnector entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "psu", ignore = true)
    @Mapping(source = "storagePowerConnectorId", target = "storagePowerConnector.id")
    public abstract PsuStoragePowerConnector fromDto(PsuStoragePowerConnectorRequestDto dto);
}
