package ru.bukhtaev.pcassembler.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.bukhtaev.pcassembler.dto.cross.mapper.ComputerCaseExpansionBayMapper;
import ru.bukhtaev.pcassembler.dto.cross.mapper.ComputerCaseFanSizeMapper;
import ru.bukhtaev.pcassembler.dto.request.ComputerCaseRequestDto;
import ru.bukhtaev.pcassembler.dto.response.ComputerCaseDto;
import ru.bukhtaev.pcassembler.model.ComputerCase;
import ru.bukhtaev.pcassembler.model.dictionary.MotherboardFormFactor;
import ru.bukhtaev.pcassembler.model.dictionary.PsuFormFactor;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {
        ComputerCaseFanSizeMapper.class,
        ComputerCaseExpansionBayMapper.class
})
public abstract class ComputerCaseMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return ComputerCase.class.equals(clazz);
    }

    public abstract ComputerCaseDto toDto(ComputerCase entity);

    @Mapping(source = "vendorId", target = "vendor.id")
    @Mapping(
            source = "supportedMotherboardFormFactorIds",
            target = "supportedMotherboardFormFactors",
            qualifiedByName = "toMotherboardFormFactorSet"
    )
    @Mapping(
            source = "supportedPsuFormFactorIds",
            target = "supportedPsuFormFactors",
            qualifiedByName = "toPsuFormFactorSet"
    )
    public abstract ComputerCase fromDto(ComputerCaseRequestDto dto);

    @Named("toMotherboardFormFactorSet")
    static Set<MotherboardFormFactor> socketIdSetToMotherboardFormFactorSet(Set<String> formFactorIds) {
        return formFactorIds.stream()
                .map(formFactorId -> MotherboardFormFactor.builder().id(formFactorId).build())
                .collect(Collectors.toSet());
    }

    @Named("toPsuFormFactorSet")
    static Set<PsuFormFactor> socketIdSetToPsuFormFactorSet(Set<String> formFactorIds) {
        return formFactorIds.stream()
                .map(formFactorId -> PsuFormFactor.builder().id(formFactorId).build())
                .collect(Collectors.toSet());
    }
}
