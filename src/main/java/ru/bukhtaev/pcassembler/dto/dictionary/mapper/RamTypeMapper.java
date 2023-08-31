package ru.bukhtaev.pcassembler.dto.dictionary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.dictionary.RamType;

@Mapper
public abstract class RamTypeMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return RamType.class.equals(clazz);
    }

    public abstract NameableDto toDto(RamType entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    public abstract RamType fromDto(NameableRequestDto dto);
}