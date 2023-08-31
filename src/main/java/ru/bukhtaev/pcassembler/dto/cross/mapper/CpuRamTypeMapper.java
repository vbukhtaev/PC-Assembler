package ru.bukhtaev.pcassembler.dto.cross.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.CpuRamTypeDto;
import ru.bukhtaev.pcassembler.dto.cross.CpuRamTypeRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.cross.CpuRamType;

@Mapper
public abstract class CpuRamTypeMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return CpuRamType.class.equals(clazz);
    }

    @Mapping(target = "cpu.supportedRamTypes", ignore = true)
    public abstract CpuRamTypeDto toDto(CpuRamType entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cpu", ignore = true)
    @Mapping(source = "ramTypeId", target = "ramType.id")
    public abstract CpuRamType fromDto(CpuRamTypeRequestDto dto);
}
