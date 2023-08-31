package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.request.RamModuleRequestDto;
import ru.bukhtaev.pcassembler.dto.response.RamModuleDto;
import ru.bukhtaev.pcassembler.model.RamModule;

@Mapper
public abstract class RamModuleMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return RamModule.class.equals(clazz);
    }

    public abstract RamModuleDto toDto(RamModule entity);

    @Mapping(source = "typeId", target = "type.id")
    @Mapping(source = "designId", target = "design.id")
    public abstract RamModule fromDto(RamModuleRequestDto dto);
}
