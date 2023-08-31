package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.request.DesignRequestDto;
import ru.bukhtaev.pcassembler.dto.response.DesignDto;
import ru.bukhtaev.pcassembler.model.Design;

@Mapper
public abstract class DesignMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return Design.class.equals(clazz);
    }

    public abstract DesignDto toDto(Design entity);

    @Mapping(source = "vendorId", target = "vendor.id")
    public abstract Design fromDto(DesignRequestDto dto);
}
