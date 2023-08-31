package ru.bukhtaev.pcassembler.dto.cross.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.ComputerFanDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerFanRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.cross.ComputerFan;

@Mapper
public abstract class ComputerFanMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return ComputerFan.class.equals(clazz);
    }

    @Mapping(target = "computer.fans", ignore = true)
    @Mapping(target = "computer.hdds", ignore = true)
    @Mapping(target = "computer.ssds", ignore = true)
    @Mapping(target = "computer.ramModules", ignore = true)
    @Mapping(target = "computer.cpu.supportedRamTypes", ignore = true)
    @Mapping(target = "computer.psu.cpuPowerConnectors", ignore = true)
    @Mapping(target = "computer.cooler.supportedSockets", ignore = true)
    @Mapping(target = "computer.psu.storagePowerConnectors", ignore = true)
    @Mapping(target = "computer.motherboard.storageConnectors", ignore = true)
    @Mapping(target = "computer.computerCase.supportedFanSizes", ignore = true)
    @Mapping(target = "computer.psu.graphicsCardPowerConnectors", ignore = true)
    @Mapping(target = "computer.computerCase.supportedExpansionBays", ignore = true)
    @Mapping(target = "computer.computerCase.supportedPsuFormFactors", ignore = true)
    @Mapping(target = "computer.computerCase.supportedMotherboardFormFactors", ignore = true)
    public abstract ComputerFanDto toDto(ComputerFan entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "computer", ignore = true)
    @Mapping(source = "fanId", target = "fan.id")
    public abstract ComputerFan fromDto(ComputerFanRequestDto dto);
}
