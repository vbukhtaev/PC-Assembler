package ru.bukhtaev.pcassembler.dto.dictionary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.dictionary.GraphicsCardPowerConnector;

@Mapper
public abstract class GraphicsCardPowerConnectorMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return GraphicsCardPowerConnector.class.equals(clazz);
    }

    public abstract NameableDto toDto(GraphicsCardPowerConnector entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    public abstract GraphicsCardPowerConnector fromDto(NameableRequestDto dto);
}
