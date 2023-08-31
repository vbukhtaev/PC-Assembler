package ru.bukhtaev.pcassembler.dto.cross.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.ComputerRamModuleDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerRamModuleRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.cross.ComputerRamModule;

@Mapper
public abstract class ComputerRamModuleMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return ComputerRamModule.class.equals(clazz);
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
    public abstract ComputerRamModuleDto toDto(ComputerRamModule entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "computer", ignore = true)
    @Mapping(source = "ramModuleId", target = "ramModule.id")
    public abstract ComputerRamModule fromDto(ComputerRamModuleRequestDto dto);
}
