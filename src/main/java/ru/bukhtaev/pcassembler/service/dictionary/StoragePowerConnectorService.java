package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.StoragePowerConnector;
import ru.bukhtaev.pcassembler.repository.dictionary.StoragePowerConnectorRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_STORAGE_POWER_CONNECTOR_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_STORAGE_POWER_CONNECTOR_UNIQUE_NAME;

@Service
@Validated
public class StoragePowerConnectorService {

    private final Translator translator;
    private final StoragePowerConnectorRepository repository;

    @Autowired
    public StoragePowerConnectorService(
            final Translator translator,
            final StoragePowerConnectorRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public StoragePowerConnector getById(@UUID final String id) {
        return findById(id);
    }

    public List<StoragePowerConnector> getAll() {
        return repository.findAll();
    }

    public Slice<StoragePowerConnector> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public StoragePowerConnector create(@Valid final StoragePowerConnector newStoragePowerConnector) {
        checkNotExists(null, newStoragePowerConnector);
        return repository.save(newStoragePowerConnector);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public StoragePowerConnector update(@UUID final String id, final StoragePowerConnector changedStoragePowerConnector) {
        checkNotExists(id, changedStoragePowerConnector);

        final StoragePowerConnector connectorToBeUpdated = findById(id);
        Optional.ofNullable(changedStoragePowerConnector.getName())
                .ifPresent(connectorToBeUpdated::setName);
        return repository.save(connectorToBeUpdated);
    }

    public StoragePowerConnector replace(@UUID final String id, @Valid final StoragePowerConnector newStoragePowerConnector) {
        checkNotExists(id, newStoragePowerConnector);

        final StoragePowerConnector connector = findById(id);
        newStoragePowerConnector.setId(id);
        newStoragePowerConnector.setCreatedAt(connector.getCreatedAt());
        return repository.save(newStoragePowerConnector);
    }

    private void checkNotExists(final String id, final StoragePowerConnector connector) {
        repository.findAnotherByName(id, connector)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_STORAGE_POWER_CONNECTOR_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private StoragePowerConnector findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_STORAGE_POWER_CONNECTOR_NOT_FOUND, id)
                ));
    }
}
