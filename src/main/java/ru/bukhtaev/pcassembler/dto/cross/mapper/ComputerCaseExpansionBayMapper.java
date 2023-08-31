package ru.bukhtaev.pcassembler.dto.cross.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.ComputerCaseExpansionBayDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerCaseExpansionBayRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.cross.ComputerCaseExpansionBay;

@Mapper
public abstract class ComputerCaseExpansionBayMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return ComputerCaseExpansionBay.class.equals(clazz);
    }

    @Mapping(target = "computerCase.supportedFanSizes", ignore = true)
    @Mapping(target = "computerCase.supportedExpansionBays", ignore = true)
    public abstract ComputerCaseExpansionBayDto toDto(ComputerCaseExpansionBay entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "computerCase", ignore = true)
    @Mapping(source = "expansionBayId", target = "expansionBay.id")
    public abstract ComputerCaseExpansionBay fromDto(ComputerCaseExpansionBayRequestDto dto);
}
