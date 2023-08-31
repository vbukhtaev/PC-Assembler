package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.request.GraphicsCardRequestDto;
import ru.bukhtaev.pcassembler.dto.response.GraphicsCardDto;
import ru.bukhtaev.pcassembler.model.GraphicsCard;

@Mapper
public abstract class GraphicsCardMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return GraphicsCard.class.equals(clazz);
    }

    public abstract GraphicsCardDto toDto(GraphicsCard entity);

    @Mapping(source = "gpuId", target = "gpu.id")
    @Mapping(source = "designId", target = "design.id")
    @Mapping(source = "pciExpressConnectorVersionId", target = "pciExpressConnectorVersion.id")
    @Mapping(source = "powerConnectorId", target = "powerConnector.id")
    public abstract GraphicsCard fromDto(GraphicsCardRequestDto dto);
}
