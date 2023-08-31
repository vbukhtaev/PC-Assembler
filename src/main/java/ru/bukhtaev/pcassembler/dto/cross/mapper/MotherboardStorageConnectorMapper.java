package ru.bukhtaev.pcassembler.dto.cross.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.MotherboardStorageConnectorDto;
import ru.bukhtaev.pcassembler.dto.cross.MotherboardStorageConnectorRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.cross.MotherboardStorageConnector;

@Mapper
public abstract class MotherboardStorageConnectorMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return MotherboardStorageConnector.class.equals(clazz);
    }

    @Mapping(target = "motherboard.storageConnectors", ignore = true)
    public abstract MotherboardStorageConnectorDto toDto(MotherboardStorageConnector entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "motherboard", ignore = true)
    @Mapping(source = "storageConnectorId", target = "storageConnector.id")
    public abstract MotherboardStorageConnector fromDto(MotherboardStorageConnectorRequestDto dto);
}
