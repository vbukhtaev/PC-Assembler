package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.mapper.CpuRamTypeMapper;
import ru.bukhtaev.pcassembler.dto.request.CpuRequestDto;
import ru.bukhtaev.pcassembler.dto.response.CpuDto;
import ru.bukhtaev.pcassembler.model.Cpu;

@Mapper(uses = CpuRamTypeMapper.class)
public abstract class CpuMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return Cpu.class.equals(clazz);
    }

    public abstract CpuDto toDto(Cpu entity);

    @Mapping(source = "socketId", target = "socket.id")
    @Mapping(source = "manufacturerId", target = "manufacturer.id")
    public abstract Cpu fromDto(CpuRequestDto dto);
}
