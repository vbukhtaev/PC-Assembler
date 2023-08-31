package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.request.SsdRequestDto;
import ru.bukhtaev.pcassembler.dto.response.SsdDto;
import ru.bukhtaev.pcassembler.model.Ssd;

@Mapper
public abstract class SsdMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return Ssd.class.equals(clazz);
    }

    public abstract SsdDto toDto(Ssd entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(source = "vendorId", target = "vendor.id")
    @Mapping(source = "connectorId", target = "connector.id")
    @Mapping(source = "powerConnectorId", target = "powerConnector.id")
    public abstract Ssd fromDto(SsdRequestDto dto);
}
