package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.request.HddRequestDto;
import ru.bukhtaev.pcassembler.dto.response.HddDto;
import ru.bukhtaev.pcassembler.model.Hdd;

@Mapper
public abstract class HddMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return Hdd.class.equals(clazz);
    }

    public abstract HddDto toDto(Hdd entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(source = "vendorId", target = "vendor.id")
    @Mapping(source = "connectorId", target = "connector.id")
    @Mapping(source = "powerConnectorId", target = "powerConnector.id")
    public abstract Hdd fromDto(HddRequestDto dto);
}
