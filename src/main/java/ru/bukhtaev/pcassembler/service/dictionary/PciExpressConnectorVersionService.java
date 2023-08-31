package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.PciExpressConnectorVersion;
import ru.bukhtaev.pcassembler.repository.dictionary.PciExpressConnectorVersionRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_PCI_EXPRESS_CONNECTOR_VERSION_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_PCI_EXPRESS_CONNECTOR_VERSION_UNIQUE_NAME;

@Service
@Validated
public class PciExpressConnectorVersionService {

    private final Translator translator;
    private final PciExpressConnectorVersionRepository repository;

    @Autowired
    public PciExpressConnectorVersionService(
            final Translator translator,
            final PciExpressConnectorVersionRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public PciExpressConnectorVersion getById(@UUID final String id) {
        return findById(id);
    }

    public List<PciExpressConnectorVersion> getAll() {
        return repository.findAll();
    }

    public Slice<PciExpressConnectorVersion> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public PciExpressConnectorVersion create(@Valid final PciExpressConnectorVersion newConnectorVersion) {
        checkNotExists(null, newConnectorVersion);
        return repository.save(newConnectorVersion);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public PciExpressConnectorVersion update(
            @UUID final String id,
            final PciExpressConnectorVersion changedConnectorVersion
    ) {
        checkNotExists(id, changedConnectorVersion);

        final PciExpressConnectorVersion connectorVersionToBeUpdated = findById(id);
        Optional.ofNullable(changedConnectorVersion.getName())
                .ifPresent(connectorVersionToBeUpdated::setName);
        return repository.save(connectorVersionToBeUpdated);
    }

    public PciExpressConnectorVersion replace(
            @UUID final String id,
            @Valid final PciExpressConnectorVersion newConnectorVersion
    ) {
        checkNotExists(id, newConnectorVersion);

        final PciExpressConnectorVersion connectorVersion = findById(id);
        newConnectorVersion.setId(id);
        newConnectorVersion.setCreatedAt(connectorVersion.getCreatedAt());
        return repository.save(newConnectorVersion);
    }

    private void checkNotExists(final String id, final PciExpressConnectorVersion connectorVersion) {
        repository.findAnotherByName(id, connectorVersion)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_PCI_EXPRESS_CONNECTOR_VERSION_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private PciExpressConnectorVersion findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_PCI_EXPRESS_CONNECTOR_VERSION_NOT_FOUND, id)
                ));
    }
}
