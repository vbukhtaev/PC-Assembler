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
import ru.bukhtaev.pcassembler.model.Fan;
import ru.bukhtaev.pcassembler.model.dictionary.FanPowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.FanSize;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;
import ru.bukhtaev.pcassembler.repository.FanRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.FanPowerConnectorRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.FanSizeRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.VendorRepository;
import ru.bukhtaev.pcassembler.util.exception.InvalidPropertyException;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniquePropertyException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.*;
import static ru.bukhtaev.pcassembler.model.NameableEntity.FIELD_NAME;

@Service
@Validated
public class FanService {

    private final Translator translator;
    private final FanRepository fanRepository;
    private final VendorRepository vendorRepository;
    private final FanSizeRepository sizeRepository;
    private final FanPowerConnectorRepository powerConnectorRepository;

    @Autowired
    public FanService(
            final Translator translator,
            final FanRepository fanRepository,
            final VendorRepository vendorRepository,
            final FanSizeRepository sizeRepository,
            final FanPowerConnectorRepository powerConnectorRepository
    ) {
        this.translator = translator;
        this.fanRepository = fanRepository;
        this.vendorRepository = vendorRepository;
        this.sizeRepository = sizeRepository;
        this.powerConnectorRepository = powerConnectorRepository;
    }

    public Fan getById(@UUID final String id) {
        return findById(id);
    }

    public List<Fan> getAll() {
        return fanRepository.findAll();
    }

    public Slice<Fan> getAll(@ValidPageable final Pageable pageable) {
        return fanRepository.findAllBy(pageable);
    }

    public Fan create(@Valid final Fan newFan) {
        setRelatedEntities(newFan);
        checkNotExists(null, newFan);

        return fanRepository.save(newFan);
    }

    public void delete(@UUID final String id) {
        fanRepository.deleteById(id);
    }

    public Fan update(@UUID final String id, final Fan changedFan) {
        final Fan fanToBeUpdated = findById(id);
        updateChangedProperties(fanToBeUpdated, changedFan);
        checkNotExists(id, fanToBeUpdated);

        return fanRepository.save(fanToBeUpdated);
    }

    public Fan replace(@UUID final String id, @Valid final Fan newFan) {
        setRelatedEntities(newFan);
        checkNotExists(id, newFan);

        final Fan fan = findById(id);
        newFan.setId(id);
        newFan.setCreatedAt(fan.getCreatedAt());
        return fanRepository.save(newFan);
    }

    private void updateChangedProperties(final Fan fanToBeUpdated, final Fan changedFan) {
        Optional.ofNullable(changedFan.getName()).ifPresent(fanToBeUpdated::setName);

        final Vendor newVendor = changedFan.getVendor();
        if (newVendor != null && newVendor.getId() != null) {
            final Vendor vendor = vendorRepository.findById(newVendor.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_VENDOR_NOT_FOUND,
                                    newVendor.getId()
                            )
                    ));
            fanToBeUpdated.setVendor(vendor);
        }

        final FanSize newSize = changedFan.getSize();
        if (newSize != null && newSize.getId() != null) {
            final FanSize size = sizeRepository.findById(newSize.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_FAN_SIZE_NOT_FOUND,
                                    newSize.getId()
                            )
                    ));
            fanToBeUpdated.setSize(size);
        }

        final FanPowerConnector newPowerConnector = changedFan.getPowerConnector();
        if (newPowerConnector != null && newPowerConnector.getId() != null) {
            final FanPowerConnector powerConnector = powerConnectorRepository.findById(newPowerConnector.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_FAN_POWER_CONNECTOR_NOT_FOUND,
                                    newPowerConnector.getId()
                            )
                    ));
            fanToBeUpdated.setPowerConnector(powerConnector);
        }
    }

    private void setRelatedEntities(final Fan fan) {
        final Vendor newVendor = fan.getVendor();
        if (newVendor == null || newVendor.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "vendor"
            );
        }

        final FanSize newSize = fan.getSize();
        if (newSize == null || newSize.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "size"
            );
        }

        final FanPowerConnector newPowerConnector = fan.getPowerConnector();
        if (newPowerConnector == null || newPowerConnector.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "powerConnector"
            );
        }

        final Vendor foundVendor = vendorRepository.findById(newVendor.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(
                                MESSAGE_CODE_VENDOR_NOT_FOUND,
                                newVendor.getId()
                        )
                ));
        fan.setVendor(foundVendor);

        final FanSize foundSize = sizeRepository.findById(newSize.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(
                                MESSAGE_CODE_FAN_SIZE_NOT_FOUND,
                                newSize.getId()
                        )
                ));
        fan.setSize(foundSize);

        final FanPowerConnector foundPowerConnector = powerConnectorRepository.findById(newPowerConnector.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(
                                MESSAGE_CODE_FAN_POWER_CONNECTOR_NOT_FOUND,
                                newPowerConnector.getId()
                        )
                ));
        fan.setPowerConnector(foundPowerConnector);
    }

    private void checkNotExists(final String id, final Fan fan) {
        fanRepository.findAnotherByNameAndSize(id, fan)
                .ifPresent(entity -> {
                    if (!entity.getId().equals(id)) {
                        throw new UniquePropertyException(
                                translator.getMessage(
                                        MESSAGE_CODE_FAN_UNIQUE_NAME_AND_SIZE,
                                        entity.getName(),
                                        entity.getSize().getLength(),
                                        entity.getSize().getWidth(),
                                        entity.getSize().getHeight()
                                ),
                                FIELD_NAME,
                                "size"
                        );
                    }
                });
    }

    private Fan findById(final String id) {
        return fanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_FAN_NOT_FOUND, id)
                ));
    }
}
