package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.bukhtaev.pcassembler.dto.request.CoolerRequestDto;
import ru.bukhtaev.pcassembler.dto.response.CoolerDto;
import ru.bukhtaev.pcassembler.model.Cooler;
import ru.bukhtaev.pcassembler.model.dictionary.Socket;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public abstract class CoolerMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return Cooler.class.equals(clazz);
    }

    public abstract CoolerDto toDto(Cooler entity);

    @Mapping(source = "vendorId", target = "vendor.id")
    @Mapping(source = "fanSizeId", target = "fanSize.id")
    @Mapping(source = "fanPowerConnectorId", target = "fanPowerConnector.id")
    @Mapping(source = "supportedSocketIds", target = "supportedSockets", qualifiedByName = "toSocketSet")
    public abstract Cooler fromDto(CoolerRequestDto dto);

    @Named("toSocketSet")
    static Set<Socket> socketIdSetToSocketSet(Set<String> socketIds) {
        return socketIds.stream()
                .map(socketId -> Socket.builder().id(socketId).build())
                .collect(Collectors.toSet());
    }
}