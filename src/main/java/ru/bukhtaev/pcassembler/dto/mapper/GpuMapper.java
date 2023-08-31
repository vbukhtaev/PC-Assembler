package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.request.GpuRequestDto;
import ru.bukhtaev.pcassembler.dto.response.GpuDto;
import ru.bukhtaev.pcassembler.model.Gpu;

@Mapper
public abstract class GpuMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return Gpu.class.equals(clazz);
    }

    public abstract GpuDto toDto(Gpu entity);

    @Mapping(source = "memoryTypeId", target = "memoryType.id")
    @Mapping(source = "manufacturerId", target = "manufacturer.id")
    public abstract Gpu fromDto(GpuRequestDto dto);
}
