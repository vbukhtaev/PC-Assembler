package ru.bukhtaev.pcassembler.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bukhtaev.pcassembler.util.exception.InvalidMappersCountException;

import java.util.List;

@Component
public class MapperResolver {

    private final List<HardwareMapper> mappers;

    @Autowired
    public MapperResolver(final List<HardwareMapper> mappers) {
        this.mappers = mappers;
    }

    public <T, M extends HardwareMapper> M resolve(final Class<T> clazz) {
        final List<HardwareMapper> hardwareMappers = mappers.stream()
                .filter(m -> m.isMappable(clazz)).toList();
        if (hardwareMappers.size() != 1) {
            throw new InvalidMappersCountException(clazz, hardwareMappers);
        }
        return (M) hardwareMappers.get(0);
    }
}
