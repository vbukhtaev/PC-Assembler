package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.FanPowerConnector;
import ru.bukhtaev.pcassembler.repository.dictionary.FanPowerConnectorRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_FAN_POWER_CONNECTOR_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_FAN_POWER_CONNECTOR_UNIQUE_NAME;

@Service
@Validated
public class FanPowerConnectorService {

    private final Translator translator;
    private final FanPowerConnectorRepository repository;

    @Autowired
    public FanPowerConnectorService(
            final Translator translator,
            final FanPowerConnectorRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public FanPowerConnector getById(@UUID final String id) {
        return findById(id);
    }

    public List<FanPowerConnector> getAll() {
        return repository.findAll();
    }

    public Slice<FanPowerConnector> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public FanPowerConnector create(@Valid final FanPowerConnector newConnector) {
        checkNotExists(null, newConnector);
        return repository.save(newConnector);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public FanPowerConnector update(
            @UUID final String id,
            final FanPowerConnector changedConnector
    ) {
        checkNotExists(id, changedConnector);

        final FanPowerConnector connectorToBeUpdated = findById(id);
        Optional.ofNullable(changedConnector.getName())
                .ifPresent(connectorToBeUpdated::setName);
        return repository.save(connectorToBeUpdated);
    }

    public FanPowerConnector replace(
            @UUID final String id,
            @Valid final FanPowerConnector newConnector
    ) {
        checkNotExists(id, newConnector);

        final FanPowerConnector connector = findById(id);
        newConnector.setId(id);
        newConnector.setCreatedAt(connector.getCreatedAt());
        return repository.save(newConnector);
    }

    private void checkNotExists(final String id, final FanPowerConnector connector) {
        repository.findAnotherByName(id, connector)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_FAN_POWER_CONNECTOR_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private FanPowerConnector findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_FAN_POWER_CONNECTOR_NOT_FOUND, id)
                ));
    }
}
