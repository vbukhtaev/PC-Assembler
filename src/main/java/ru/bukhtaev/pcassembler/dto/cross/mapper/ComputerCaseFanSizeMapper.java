package ru.bukhtaev.pcassembler.dto.cross.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.ComputerCaseFanSizeDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerCaseFanSizeRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.cross.ComputerCaseFanSize;

@Mapper
public abstract class ComputerCaseFanSizeMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return ComputerCaseFanSize.class.equals(clazz);
    }

    @Mapping(target = "computerCase.supportedFanSizes", ignore = true)
    @Mapping(target = "computerCase.supportedExpansionBays", ignore = true)
    public abstract ComputerCaseFanSizeDto toDto(ComputerCaseFanSize entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "computerCase", ignore = true)
    @Mapping(source = "fanSizeId", target = "fanSize.id")
    public abstract ComputerCaseFanSize fromDto(ComputerCaseFanSizeRequestDto dto);
}
