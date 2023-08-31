package ru.bukhtaev.pcassembler.dto.dictionary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.dictionary.FanSizeDto;
import ru.bukhtaev.pcassembler.dto.dictionary.FanSizeRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.dictionary.FanSize;

@Mapper
public abstract class FanSizeMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return FanSize.class.equals(clazz);
    }

    public abstract FanSizeDto toDto(FanSize entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    public abstract FanSize fromDto(FanSizeRequestDto dto);
}
