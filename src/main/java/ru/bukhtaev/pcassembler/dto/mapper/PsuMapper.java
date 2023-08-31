package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.mapper.PsuCpuPowerConnectorMapper;
import ru.bukhtaev.pcassembler.dto.cross.mapper.PsuGraphicsCardPowerConnectorMapper;
import ru.bukhtaev.pcassembler.dto.cross.mapper.PsuStoragePowerConnectorMapper;
import ru.bukhtaev.pcassembler.dto.request.PsuRequestDto;
import ru.bukhtaev.pcassembler.dto.response.PsuDto;
import ru.bukhtaev.pcassembler.model.Psu;

@Mapper(uses = {
        PsuCpuPowerConnectorMapper.class,
        PsuGraphicsCardPowerConnectorMapper.class,
        PsuStoragePowerConnectorMapper.class
})
public abstract class PsuMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return Psu.class.equals(clazz);
    }

    public abstract PsuDto toDto(Psu entity);

    @Mapping(source = "vendorId", target = "vendor.id")
    @Mapping(source = "formFactorId", target = "formFactor.id")
    @Mapping(source = "certificateId", target = "certificate.id")
    @Mapping(source = "mainPowerConnectorId", target = "mainPowerConnector.id")
    public abstract Psu fromDto(PsuRequestDto dto);
}
