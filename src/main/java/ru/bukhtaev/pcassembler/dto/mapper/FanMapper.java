package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.request.FanRequestDto;
import ru.bukhtaev.pcassembler.dto.response.FanDto;
import ru.bukhtaev.pcassembler.model.Fan;

@Mapper
public abstract class FanMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return Fan.class.equals(clazz);
    }

    public abstract FanDto toDto(Fan entity);

    @Mapping(source = "vendorId", target = "vendor.id")
    @Mapping(source = "sizeId", target = "size.id")
    @Mapping(source = "powerConnectorId", target = "powerConnector.id")
    public abstract Fan fromDto(FanRequestDto dto);
}
