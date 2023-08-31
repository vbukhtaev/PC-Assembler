package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.response.ChipsetDto;
import ru.bukhtaev.pcassembler.dto.request.ChipsetRequestDto;
import ru.bukhtaev.pcassembler.model.Chipset;

@Mapper
public abstract class ChipsetMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return Chipset.class.equals(clazz);
    }

    public abstract ChipsetDto toDto(Chipset entity);

    @Mapping(source = "socketId", target = "socket.id")
    public abstract Chipset fromDto(ChipsetRequestDto dto);
}
