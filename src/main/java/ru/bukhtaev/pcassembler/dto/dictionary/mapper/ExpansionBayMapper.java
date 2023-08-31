package ru.bukhtaev.pcassembler.dto.dictionary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.bukhtaev.pcassembler.dto.dictionary.ExpansionBayDto;
import ru.bukhtaev.pcassembler.dto.dictionary.ExpansionBayRequestDto;
import ru.bukhtaev.pcassembler.dto.mapper.HardwareMapper;
import ru.bukhtaev.pcassembler.model.dictionary.ExpansionBay;

import java.math.BigDecimal;

@Mapper
public abstract class ExpansionBayMapper implements HardwareMapper {

    @Override
    public boolean isMappable(Class<?> clazz) {
        return ExpansionBay.class.equals(clazz);
    }

    @Mapping(source = "size", target = "size", qualifiedByName = "bigDecimalToString")
    public abstract ExpansionBayDto toDto(ExpansionBay entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(source = "size", target = "size", qualifiedByName = "stringToBigDecimal")
    public abstract ExpansionBay fromDto(ExpansionBayRequestDto dto);

    @Named("bigDecimalToString")
    public static String bigDecimalToString(final BigDecimal bigDecimal) {
        return bigDecimal == null
        ? null
        : bigDecimal.stripTrailingZeros().toPlainString();
    }

    @Named("stringToBigDecimal")
    public static BigDecimal stringToBigDecimal(final String string) {
        return string == null
                ? null
                : new BigDecimal(string);
    }
}
