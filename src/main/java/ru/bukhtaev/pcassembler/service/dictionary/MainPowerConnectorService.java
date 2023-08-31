package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.MainPowerConnector;
import ru.bukhtaev.pcassembler.repository.dictionary.MainPowerConnectorRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_MAIN_POWER_CONNECTOR_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_MAIN_POWER_CONNECTOR_UNIQUE_NAME;

@Service
@Validated
public class MainPowerConnectorService {

    private final Translator translator;
    private final MainPowerConnectorRepository repository;

    @Autowired
    public MainPowerConnectorService(
            final Translator translator,
            final MainPowerConnectorRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public MainPowerConnector getById(@UUID final String id) {
        return findById(id);
    }

    public List<MainPowerConnector> getAll() {
        return repository.findAll();
    }

    public Slice<MainPowerConnector> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public MainPowerConnector create(@Valid final MainPowerConnector newConnector) {
        checkNotExists(null, newConnector);
        return repository.save(newConnector);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public MainPowerConnector update(@UUID final String id, final MainPowerConnector changedConnector) {
        checkNotExists(id, changedConnector);

        final MainPowerConnector connectorToBeUpdated = findById(id);
        Optional.ofNullable(changedConnector.getName())
                .ifPresent(connectorToBeUpdated::setName);
        return repository.save(connectorToBeUpdated);
    }

    public MainPowerConnector replace(
            @UUID final String id,
            @Valid final MainPowerConnector newConnector
    ) {
        checkNotExists(id, newConnector);

        final MainPowerConnector connector = findById(id);
        newConnector.setId(id);
        newConnector.setCreatedAt(connector.getCreatedAt());
        return repository.save(newConnector);
    }

    private void checkNotExists(final String id, final MainPowerConnector connector) {
        repository.findAnotherByName(id, connector)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_MAIN_POWER_CONNECTOR_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private MainPowerConnector findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_MAIN_POWER_CONNECTOR_NOT_FOUND, id)
                ));
    }
}
