package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bukhtaev.pcassembler.dto.cross.mapper.ComputerFanMapper;
import ru.bukhtaev.pcassembler.dto.cross.mapper.ComputerHddMapper;
import ru.bukhtaev.pcassembler.dto.cross.mapper.ComputerRamModuleMapper;
import ru.bukhtaev.pcassembler.dto.cross.mapper.ComputerSsdMapper;
import ru.bukhtaev.pcassembler.dto.request.ComputerBuildRequestDto;
import ru.bukhtaev.pcassembler.dto.response.ComputerBuildDto;
import ru.bukhtaev.pcassembler.model.ComputerBuild;

@Mapper(uses = {
        ComputerFanMapper.class,
        ComputerHddMapper.class,
        ComputerSsdMapper.class,
        ComputerRamModuleMapper.class
})
public abstract class ComputerBuildMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return ComputerBuild.class.equals(clazz);
    }

    @Mapping(target = "cpu.supportedRamTypes", ignore = true)
    @Mapping(target = "psu.cpuPowerConnectors", ignore = true)
    @Mapping(target = "cooler.supportedSockets", ignore = true)
    @Mapping(target = "psu.storagePowerConnectors", ignore = true)
    @Mapping(target = "motherboard.storageConnectors", ignore = true)
    @Mapping(target = "computerCase.supportedFanSizes", ignore = true)
    @Mapping(target = "psu.graphicsCardPowerConnectors", ignore = true)
    @Mapping(target = "computerCase.supportedExpansionBays", ignore = true)
    @Mapping(target = "computerCase.supportedPsuFormFactors", ignore = true)
    @Mapping(target = "computerCase.supportedMotherboardFormFactors", ignore = true)
    public abstract ComputerBuildDto toDto(ComputerBuild entity);

    @Mapping(source = "cpuId", target = "cpu.id")
    @Mapping(source = "psuId", target = "psu.id")
    @Mapping(source = "motherboardId", target = "motherboard.id")
    @Mapping(source = "coolerId", target = "cooler.id")
    @Mapping(source = "graphicsCardId", target = "graphicsCard.id")
    @Mapping(source = "computerCaseId", target = "computerCase.id")
    public abstract ComputerBuild fromDto(ComputerBuildRequestDto dto);
}
