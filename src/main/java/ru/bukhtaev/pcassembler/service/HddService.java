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
import ru.bukhtaev.pcassembler.model.Hdd;
import ru.bukhtaev.pcassembler.model.dictionary.StorageConnector;
import ru.bukhtaev.pcassembler.model.dictionary.StoragePowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;
import ru.bukhtaev.pcassembler.repository.HddRepository;
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
public class HddService {

    private final Translator translator;
    private final HddRepository hddRepository;
    private final VendorRepository vendorRepository;
    private final StorageConnectorRepository connectorRepository;
    private final StoragePowerConnectorRepository powerConnectorRepository;

    @Autowired
    public HddService(
            final Translator translator,
            final HddRepository hddRepository,
            final VendorRepository vendorRepository,
            final StorageConnectorRepository connectorRepository,
            final StoragePowerConnectorRepository powerConnectorRepository
    ) {
        this.translator = translator;
        this.hddRepository = hddRepository;
        this.vendorRepository = vendorRepository;
        this.connectorRepository = connectorRepository;
        this.powerConnectorRepository = powerConnectorRepository;
    }

    public Hdd getById(@UUID final String id) {
        return findById(id);
    }

    public List<Hdd> getAll() {
        return hddRepository.findAll();
    }

    public Slice<Hdd> getAll(@ValidPageable final Pageable pageable) {
        return hddRepository.findAllBy(pageable);
    }

    public Hdd create(@Valid final Hdd newHdd) {
        setRelatedEntities(newHdd);
        checkNotExists(null, newHdd);

        return hddRepository.save(newHdd);
    }

    public void delete(@UUID final String id) {
        hddRepository.deleteById(id);
    }

    public Hdd update(@UUID final String id, final Hdd changedHdd) {
        final Hdd hddToBeUpdated = findById(id);
        updateChangedProperties(hddToBeUpdated, changedHdd);
        checkNotExists(id, hddToBeUpdated);

        return hddRepository.save(hddToBeUpdated);
    }

    public Hdd replace(@UUID final String id, @Valid final Hdd newHdd) {
        setRelatedEntities(newHdd);
        checkNotExists(id, newHdd);

        final Hdd hdd = findById(id);
        newHdd.setId(id);
        newHdd.setCreatedAt(hdd.getCreatedAt());
        return hddRepository.save(newHdd);
    }

    private void updateChangedProperties(final Hdd hddToBeUpdated, final Hdd changedHdd) {
        Optional.ofNullable(changedHdd.getName()).ifPresent(hddToBeUpdated::setName);
        Optional.ofNullable(changedHdd.getCapacity()).ifPresent(hddToBeUpdated::setCapacity);
        Optional.ofNullable(changedHdd.getReadingSpeed()).ifPresent(hddToBeUpdated::setReadingSpeed);
        Optional.ofNullable(changedHdd.getWritingSpeed()).ifPresent(hddToBeUpdated::setWritingSpeed);
        Optional.ofNullable(changedHdd.getSpindleSpeed()).ifPresent(hddToBeUpdated::setSpindleSpeed);
        Optional.ofNullable(changedHdd.getCacheSize()).ifPresent(hddToBeUpdated::setCacheSize);

        final Vendor newVendor = changedHdd.getVendor();
        if (newVendor != null && newVendor.getId() != null) {
            final Vendor vendor = vendorRepository.findById(newVendor.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_VENDOR_NOT_FOUND, newVendor.getId())
                    ));
            hddToBeUpdated.setVendor(vendor);
        }

        final StorageConnector newConnector = changedHdd.getConnector();
        if (newConnector != null && newConnector.getId() != null) {
            final StorageConnector connector = connectorRepository.findById(newConnector.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_STORAGE_CONNECTOR_NOT_FOUND, newConnector.getId())
                    ));
            hddToBeUpdated.setConnector(connector);
        }

        final StoragePowerConnector newPowerConnector = changedHdd.getPowerConnector();
        if (newPowerConnector != null && newPowerConnector.getId() != null) {
            final StoragePowerConnector connector = powerConnectorRepository.findById(newPowerConnector.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_STORAGE_POWER_CONNECTOR_NOT_FOUND,
                                    newPowerConnector.getId()
                            )
                    ));
            hddToBeUpdated.setPowerConnector(connector);
        }
    }

    private void setRelatedEntities(final Hdd hdd) {
        final Vendor newVendor = hdd.getVendor();
        if (newVendor == null || newVendor.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "vendor"
            );
        }

        final StorageConnector newConnector = hdd.getConnector();
        if (newConnector == null || newConnector.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "connector"
            );
        }

        final StoragePowerConnector newPowerConnector = hdd.getPowerConnector();
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
        hdd.setVendor(foundVendor);

        final StorageConnector foundConnector = connectorRepository.findById(newConnector.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_STORAGE_CONNECTOR_NOT_FOUND, newConnector.getId())
                ));
        hdd.setConnector(foundConnector);

        final StoragePowerConnector foundPowerConnector = powerConnectorRepository.findById(newPowerConnector.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_STORAGE_POWER_CONNECTOR_NOT_FOUND, newConnector.getId())
                ));
        hdd.setPowerConnector(foundPowerConnector);
    }

    private void checkNotExists(final String id, final Hdd hdd) {
        hddRepository.findAnotherByNameAndCapacity(id, hdd)
                .ifPresent(entity -> {
                    throw new UniquePropertyException(
                            translator.getMessage(
                                    MESSAGE_CODE_HDD_UNIQUE_NAME_AND_CAPACITY,
                                    entity.getName(),
                                    entity.getCapacity()
                            ),
                            "name",
                            "capacity"
                    );
                });
    }

    private Hdd findById(final String id) {
        return hddRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_HDD_NOT_FOUND, id)
                ));
    }
}
