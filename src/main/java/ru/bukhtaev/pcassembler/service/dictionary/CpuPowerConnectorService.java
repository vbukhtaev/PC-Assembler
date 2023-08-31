package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.CpuPowerConnector;
import ru.bukhtaev.pcassembler.repository.dictionary.CpuPowerConnectorRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_CPU_POWER_CONNECTOR_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_CPU_POWER_CONNECTOR_UNIQUE_NAME;

@Service
@Validated
public class CpuPowerConnectorService {

    private final Translator translator;
    private final CpuPowerConnectorRepository repository;

    @Autowired
    public CpuPowerConnectorService(
            final Translator translator,
            final CpuPowerConnectorRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public CpuPowerConnector getById(@UUID final String id) {
        return findById(id);
    }

    public List<CpuPowerConnector> getAll() {
        return repository.findAll();
    }

    public Slice<CpuPowerConnector> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public CpuPowerConnector create(@Valid final CpuPowerConnector newConnector) {
        checkNotExists(null, newConnector);
        return repository.save(newConnector);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public CpuPowerConnector update(@UUID final String id, final CpuPowerConnector changedConnector) {
        checkNotExists(id, changedConnector);

        final CpuPowerConnector connectorToBeUpdated = findById(id);
        Optional.ofNullable(changedConnector.getName())
                .ifPresent(connectorToBeUpdated::setName);
        return repository.save(connectorToBeUpdated);
    }

    public CpuPowerConnector replace(
            @UUID final String id,
            @Valid final CpuPowerConnector newConnector
    ) {
        checkNotExists(id, newConnector);

        final CpuPowerConnector connector = findById(id);
        newConnector.setId(id);
        newConnector.setCreatedAt(connector.getCreatedAt());
        return repository.save(newConnector);
    }

    private void checkNotExists(final String id, final CpuPowerConnector connector) {
        repository.findAnotherByName(id, connector)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_CPU_POWER_CONNECTOR_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private CpuPowerConnector findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_CPU_POWER_CONNECTOR_NOT_FOUND, id)
                ));
    }
}
