package ru.bukhtaev.pcassembler.service;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.Ssd;
import ru.bukhtaev.pcassembler.model.dictionary.StorageConnector;
import ru.bukhtaev.pcassembler.model.dictionary.StoragePowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;
import ru.bukhtaev.pcassembler.repository.SsdRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.StorageConnectorRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.StoragePowerConnectorRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.VendorRepository;
import ru.bukhtaev.pcassembler.util.exception.InvalidPropertyException;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniquePropertyException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.*;

@Service
@Validated
public class SsdService {

    private final Translator translator;
    private final SsdRepository ssdRepository;
    private final VendorRepository vendorRepository;
    private final StorageConnectorRepository connectorRepository;
    private final StoragePowerConnectorRepository powerConnectorRepository;

    @Autowired
    public SsdService(
            final Translator translator,
            final SsdRepository ssdRepository,
            final VendorRepository vendorRepository,
            final StorageConnectorRepository connectorRepository,
            final StoragePowerConnectorRepository powerConnectorRepository
    ) {
        this.translator = translator;
        this.ssdRepository = ssdRepository;
        this.vendorRepository = vendorRepository;
        this.connectorRepository = connectorRepository;
        this.powerConnectorRepository = powerConnectorRepository;
    }

    public Ssd getById(@UUID final String id) {
        return findById(id);
    }

    public List<Ssd> getAll() {
        return ssdRepository.findAll();
    }

    public Slice<Ssd> getAll(@ValidPageable final Pageable pageable) {
        return ssdRepository.findAllBy(pageable);
    }

    public Ssd create(@Valid final Ssd newSsd) {
        setRelatedEntities(newSsd);
        checkNotExists(null, newSsd);

        return ssdRepository.save(newSsd);
    }

    public void delete(@UUID final String id) {
        ssdRepository.deleteById(id);
    }

    public Ssd update(@UUID final String id, final Ssd changedSsd) {
        final Ssd ssdToBeUpdated = findById(id);
        updateChangedProperties(ssdToBeUpdated, changedSsd);
        checkNotExists(id, ssdToBeUpdated);

        return ssdRepository.save(ssdToBeUpdated);
    }

    public Ssd replace(@UUID final String id, @Valid final Ssd newSsd) {
        setRelatedEntities(newSsd);
        checkNotExists(id, newSsd);

        final Ssd ssd = findById(id);
        newSsd.setId(id);
        newSsd.setCreatedAt(ssd.getCreatedAt());
        return ssdRepository.save(newSsd);
    }

    private void updateChangedProperties(final Ssd ssdToBeUpdated, final Ssd changedSsd) {
        Optional.ofNullable(changedSsd.getName()).ifPresent(ssdToBeUpdated::setName);
        Optional.ofNullable(changedSsd.getCapacity()).ifPresent(ssdToBeUpdated::setCapacity);
        Optional.ofNullable(changedSsd.getReadingSpeed()).ifPresent(ssdToBeUpdated::setReadingSpeed);
        Optional.ofNullable(changedSsd.getWritingSpeed()).ifPresent(ssdToBeUpdated::setWritingSpeed);

        final Vendor newVendor = changedSsd.getVendor();
        if (newVendor != null && newVendor.getId() != null) {
            final Vendor vendor = vendorRepository.findById(newVendor.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_VENDOR_NOT_FOUND, newVendor.getId())
                    ));
            ssdToBeUpdated.setVendor(vendor);
        }

        final StorageConnector newConnector = changedSsd.getConnector();
        if (newConnector != null && newConnector.getId() != null) {
            final StorageConnector connector = connectorRepository.findById(newConnector.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_STORAGE_CONNECTOR_NOT_FOUND, newConnector.getId())
                    ));
            ssdToBeUpdated.setConnector(connector);
        }

        final StoragePowerConnector newPowerConnector = changedSsd.getPowerConnector();
        if (newPowerConnector != null && newPowerConnector.getId() != null) {
            final StoragePowerConnector connector = powerConnectorRepository.findById(newPowerConnector.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_STORAGE_POWER_CONNECTOR_NOT_FOUND,
                                    newPowerConnector.getId()
                            )
                    ));
            ssdToBeUpdated.setPowerConnector(connector);
        }
    }

    private void setRelatedEntities(final Ssd ssd) {
        final Vendor newVendor = ssd.getVendor();
        if (newVendor == null || newVendor.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "vendor"
            );
        }

        final StorageConnector newConnector = ssd.getConnector();
        if (newConnector == null || newConnector.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "connector"
            );
        }

        final StoragePowerConnector newPowerConnector = ssd.getPowerConnector();
        if (newPowerConnector == null || newPowerConnector.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "powerConnector"
            );
        }

        final Vendor foundVendor = vendorRepository.findById(newVendor.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_VENDOR_NOT_FOUND, newVendor.getId())
                ));
        ssd.setVendor(foundVendor);

        final StorageConnector foundConnector = connectorRepository.findById(newConnector.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_STORAGE_CONNECTOR_NOT_FOUND, newConnector.getId())
                ));
        ssd.setConnector(foundConnector);

        final StoragePowerConnector foundPowerConnector = powerConnectorRepository.findById(newPowerConnector.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_STORAGE_POWER_CONNECTOR_NOT_FOUND, newConnector.getId())
                ));
        ssd.setPowerConnector(foundPowerConnector);
    }

    private void checkNotExists(final String id, final Ssd ssd) {
        ssdRepository.findAnotherByNameAndCapacity(id, ssd)
                .ifPresent(entity -> {
                    throw new UniquePropertyException(
                            translator.getMessage(
                                    MESSAGE_CODE_SSD_UNIQUE_NAME_AND_CAPACITY,
                                    entity.getName(),
                                    entity.getCapacity()
                            ),
                            "name",
                            "capacity"
                    );
                });
    }

    private Ssd findById(final String id) {
        return ssdRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_SSD_NOT_FOUND, id)
                ));
    }
}
