package ru.bukhtaev.pcassembler.dto.cross.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.ComputerHddDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerHddRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.cross.ComputerHdd;

@Mapper
public abstract class ComputerHddMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return ComputerHdd.class.equals(clazz);
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
    public abstract ComputerHddDto toDto(ComputerHdd entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "computer", ignore = true)
    @Mapping(source = "hddId", target = "hdd.id")
    public abstract ComputerHdd fromDto(ComputerHddRequestDto dto);
}
