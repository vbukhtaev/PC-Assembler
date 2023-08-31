package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.mapper.MotherboardStorageConnectorMapper;
import ru.bukhtaev.pcassembler.dto.request.MotherboardRequestDto;
import ru.bukhtaev.pcassembler.dto.response.MotherboardDto;
import ru.bukhtaev.pcassembler.model.Motherboard;

@Mapper(uses = MotherboardStorageConnectorMapper.class)
public abstract class MotherboardMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return Motherboard.class.equals(clazz);
    }

    public abstract MotherboardDto toDto(Motherboard entity);

    @Mapping(source = "designId", target = "design.id")
    @Mapping(source = "chipsetId", target = "chipset.id")
    @Mapping(source = "ramTypeId", target = "ramType.id")
    @Mapping(source = "formFactorId", target = "formFactor.id")
    @Mapping(source = "cpuPowerConnectorId", target = "cpuPowerConnector.id")
    @Mapping(source = "mainPowerConnectorId", target = "mainPowerConnector.id")
    @Mapping(source = "fanPowerConnectorId", target = "fanPowerConnector.id")
    @Mapping(source = "pciExpressConnectorVersionId", target = "pciExpressConnectorVersion.id")
    public abstract Motherboard fromDto(MotherboardRequestDto dto);
}
