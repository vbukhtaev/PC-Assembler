package ru.bukhtaev.pcassembler.service;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.Psu;
import ru.bukhtaev.pcassembler.model.cross.PsuCpuPowerConnector;
import ru.bukhtaev.pcassembler.model.cross.PsuGraphicsCardPowerConnector;
import ru.bukhtaev.pcassembler.model.cross.PsuStoragePowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.*;
import ru.bukhtaev.pcassembler.repository.PsuRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.*;
import ru.bukhtaev.pcassembler.util.exception.InvalidPropertyException;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.*;

@Service
@Validated
public class PsuService {

    private final Translator translator;
    private final PsuRepository psuRepository;
    private final VendorRepository vendorRepository;
    private final PsuFormFactorRepository formFactorRepository;
    private final PsuCertificateRepository certificateRepository;
    private final MainPowerConnectorRepository mainPowerConnectorRepository;
    private final CpuPowerConnectorRepository cpuPowerConnectorRepository;
    private final StoragePowerConnectorRepository storagePowerConnectorRepository;
    private final GraphicsCardPowerConnectorRepository graphicsCardPowerConnectorRepository;

    @Autowired
    public PsuService(
            final Translator translator,
            final PsuRepository psuRepository,
            final VendorRepository vendorRepository,
            final PsuFormFactorRepository formFactorRepository,
            final PsuCertificateRepository certificateRepository,
            final MainPowerConnectorRepository mainPowerConnectorRepository,
            final StoragePowerConnectorRepository storagePowerConnectorRepository,
            final CpuPowerConnectorRepository cpuPowerConnectorRepository,
            final GraphicsCardPowerConnectorRepository graphicsCardPowerConnectorRepository
    ) {
        this.translator = translator;
        this.psuRepository = psuRepository;
        this.vendorRepository = vendorRepository;
        this.formFactorRepository = formFactorRepository;
        this.certificateRepository = certificateRepository;
        this.mainPowerConnectorRepository = mainPowerConnectorRepository;
        this.storagePowerConnectorRepository = storagePowerConnectorRepository;
        this.cpuPowerConnectorRepository = cpuPowerConnectorRepository;
        this.graphicsCardPowerConnectorRepository = graphicsCardPowerConnectorRepository;
    }

    public Psu getById(@UUID final String id) {
        return findById(id);
    }

    public List<Psu> getAll() {
        return psuRepository.findAll();
    }

    public Slice<Psu> getAll(@ValidPageable final Pageable pageable) {
        return psuRepository.findAllBy(pageable);
    }

    public Psu create(@Valid final Psu newPsu) {
        setRelatedEntities(newPsu);
        checkNotExists(null, newPsu);

        return psuRepository.save(newPsu);
    }

    public void delete(@UUID final String id) {
        psuRepository.deleteById(id);
    }

    public Psu update(@UUID final String id, final Psu changedPsu) {
        final Psu psuToBeUpdated = findById(id);
        updateChangedProperties(psuToBeUpdated, changedPsu);
        checkNotExists(id, psuToBeUpdated);

        return psuRepository.save(psuToBeUpdated);
    }

    public Psu replace(@UUID final String id, @Valid final Psu newPsu) {
        setRelatedEntities(newPsu);
        checkNotExists(id, newPsu);

        final Psu psu = findById(id);
        newPsu.setId(id);
        newPsu.setCreatedAt(psu.getCreatedAt());
        return psuRepository.save(newPsu);
    }

    private void updateChangedProperties(final Psu psuToBeUpdated, final Psu changedPsu) {
        Optional.ofNullable(changedPsu.getName()).ifPresent(psuToBeUpdated::setName);
        Optional.ofNullable(changedPsu.getPower()).ifPresent(psuToBeUpdated::setPower);
        Optional.ofNullable(changedPsu.getPower12V()).ifPresent(psuToBeUpdated::setPower12V);
        Optional.ofNullable(changedPsu.getLength()).ifPresent(psuToBeUpdated::setLength);

        final Vendor newVendor = changedPsu.getVendor();
        if (newVendor != null && newVendor.getId() != null) {
            final Vendor vendor = vendorRepository.findById(newVendor.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newVendor.getId())
                    ));
            psuToBeUpdated.setVendor(vendor);
        }

        final PsuFormFactor newFormFactor = changedPsu.getFormFactor();
        if (newFormFactor != null && newFormFactor.getId() != null) {
            final PsuFormFactor formFactor = formFactorRepository.findById(newFormFactor.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newFormFactor.getId())
                    ));
            psuToBeUpdated.setFormFactor(formFactor);
        }

        final PsuCertificate newCertificate = changedPsu.getCertificate();
        if (newCertificate != null && newCertificate.getId() != null) {
            final PsuCertificate certificate = certificateRepository.findById(newCertificate.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newCertificate.getId())
                    ));
            psuToBeUpdated.setCertificate(certificate);
        }

        final MainPowerConnector newMainPowerConnector = changedPsu.getMainPowerConnector();
        if (newMainPowerConnector != null && newMainPowerConnector.getId() != null) {
            final MainPowerConnector mainPowerConnector
                    = mainPowerConnectorRepository.findById(newMainPowerConnector.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newMainPowerConnector.getId())
                    ));
            psuToBeUpdated.setMainPowerConnector(mainPowerConnector);
        }

        final Set<PsuCpuPowerConnector> newCpuPowerConnectors = changedPsu.getCpuPowerConnectors();
        if (newCpuPowerConnectors != null) {
            newCpuPowerConnectors.forEach(psuCpuPowerConnector -> {
                final CpuPowerConnector cpuPowerConnector
                        = cpuPowerConnectorRepository.findById(psuCpuPowerConnector.getCpuPowerConnector().getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_CPU_POWER_CONNECTOR_NOT_FOUND,
                                        psuCpuPowerConnector.getCpuPowerConnector().getId()
                                )
                        ));
                psuCpuPowerConnector.setCpuPowerConnector(cpuPowerConnector);
                psuCpuPowerConnector.setPsu(psuToBeUpdated);
            });
            psuToBeUpdated.setCpuPowerConnectors(newCpuPowerConnectors);
        }

        final Set<PsuGraphicsCardPowerConnector> newGraphicsCardPowerConnectors = changedPsu.getGraphicsCardPowerConnectors();
        if (newGraphicsCardPowerConnectors != null) {
            newGraphicsCardPowerConnectors.forEach(psuGraphicsCardPowerConnector -> {
                final GraphicsCardPowerConnector graphicsCardPowerConnector
                        = graphicsCardPowerConnectorRepository.findById(
                                psuGraphicsCardPowerConnector.getGraphicsCardPowerConnector().getId()
                        )
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_GRAPHICS_CARD_POWER_CONNECTOR_NOT_FOUND,
                                        psuGraphicsCardPowerConnector.getGraphicsCardPowerConnector().getId()
                                )
                        ));
                psuGraphicsCardPowerConnector.setGraphicsCardPowerConnector(graphicsCardPowerConnector);
                psuGraphicsCardPowerConnector.setPsu(psuToBeUpdated);
            });
            psuToBeUpdated.setGraphicsCardPowerConnectors(newGraphicsCardPowerConnectors);
        }

        final Set<PsuStoragePowerConnector> newStoragePowerConnectors = changedPsu.getStoragePowerConnectors();
        if (newStoragePowerConnectors != null) {
            newStoragePowerConnectors.forEach(psuStoragePowerConnector -> {
                final StoragePowerConnector storagePowerConnector
                        = storagePowerConnectorRepository.findById(psuStoragePowerConnector.getStoragePowerConnector().getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_STORAGE_POWER_CONNECTOR_NOT_FOUND,
                                        psuStoragePowerConnector.getStoragePowerConnector().getId()
                                )
                        ));
                psuStoragePowerConnector.setStoragePowerConnector(storagePowerConnector);
                psuStoragePowerConnector.setPsu(psuToBeUpdated);
            });
            psuToBeUpdated.setStoragePowerConnectors(newStoragePowerConnectors);
        }

    }

    private void setRelatedEntities(final Psu psu) {
        final Vendor newVendor = psu.getVendor();
        if (newVendor == null || newVendor.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "vendor"
            );
        }

        final PsuFormFactor newFormFactor = psu.getFormFactor();
        if (newFormFactor == null || newFormFactor.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "formFactor"
            );
        }

        final PsuCertificate newCertificate = psu.getCertificate();
        if (newCertificate == null || newCertificate.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "certificate"
            );
        }

        final MainPowerConnector newMainPowerConnector = psu.getMainPowerConnector();
        if (newMainPowerConnector == null || newMainPowerConnector.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "mainPowerConnector"
            );
        }

        final Set<PsuCpuPowerConnector> newCpuPowerConnectors = psu.getCpuPowerConnectors();
        if (newCpuPowerConnectors.stream().anyMatch(psuCpuPowerConnector ->
                StringUtils.isBlank(psuCpuPowerConnector.getCpuPowerConnector().getId()))) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "cpuPowerConnectors"
            );
        }

        final Set<PsuGraphicsCardPowerConnector> newGraphicsCardPowerConnectors = psu.getGraphicsCardPowerConnectors();
        if (newGraphicsCardPowerConnectors.stream().anyMatch(psuGraphicsCardPowerConnector ->
                StringUtils.isBlank(psuGraphicsCardPowerConnector.getGraphicsCardPowerConnector().getId()))) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "graphicsCardPowerConnectors"
            );
        }

        final Set<PsuStoragePowerConnector> newStoragePowerConnectors = psu.getStoragePowerConnectors();
        if (newStoragePowerConnectors.stream().anyMatch(psuStoragePowerConnector ->
                StringUtils.isBlank(psuStoragePowerConnector.getStoragePowerConnector().getId()))) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "storagePowerConnectors"
            );
        }

        final Vendor foundVendor = vendorRepository.findById(newVendor.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newVendor.getId())
                ));
        psu.setVendor(foundVendor);

        final PsuFormFactor foundFormFactor = formFactorRepository.findById(newFormFactor.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newFormFactor.getId())
                ));
        psu.setFormFactor(foundFormFactor);

        final PsuCertificate foundCertificate = certificateRepository.findById(newCertificate.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newCertificate.getId())
                ));
        psu.setCertificate(foundCertificate);

        final MainPowerConnector foundMainPowerConnector
                = mainPowerConnectorRepository.findById(newMainPowerConnector.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newMainPowerConnector.getId())
                ));
        psu.setMainPowerConnector(foundMainPowerConnector);

        newCpuPowerConnectors.forEach(psuCpuPowerConnector -> {
            final CpuPowerConnector cpuPowerConnector
                    = cpuPowerConnectorRepository.findById(psuCpuPowerConnector.getCpuPowerConnector().getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_CPU_POWER_CONNECTOR_NOT_FOUND,
                                    psuCpuPowerConnector.getCpuPowerConnector().getId()
                            )
                    ));
            psuCpuPowerConnector.setCpuPowerConnector(cpuPowerConnector);
            psuCpuPowerConnector.setPsu(psu);
        });
        psu.setCpuPowerConnectors(newCpuPowerConnectors);

        newGraphicsCardPowerConnectors.forEach(psuGraphicsCardPowerConnector -> {
            final GraphicsCardPowerConnector graphicsCardPowerConnector
                    = graphicsCardPowerConnectorRepository.findById(
                            psuGraphicsCardPowerConnector.getGraphicsCardPowerConnector().getId()
                    )
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_GRAPHICS_CARD_POWER_CONNECTOR_NOT_FOUND,
                                    psuGraphicsCardPowerConnector.getGraphicsCardPowerConnector().getId()
                            )
                    ));
            psuGraphicsCardPowerConnector.setGraphicsCardPowerConnector(graphicsCardPowerConnector);
            psuGraphicsCardPowerConnector.setPsu(psu);
        });
        psu.setGraphicsCardPowerConnectors(newGraphicsCardPowerConnectors);

        newStoragePowerConnectors.forEach(psuStoragePowerConnector -> {
            final StoragePowerConnector storagePowerConnector
                    = storagePowerConnectorRepository.findById(
                            psuStoragePowerConnector.getStoragePowerConnector().getId()
                    )
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_STORAGE_POWER_CONNECTOR_NOT_FOUND,
                                    psuStoragePowerConnector.getStoragePowerConnector().getId()
                            )
                    ));
            psuStoragePowerConnector.setStoragePowerConnector(storagePowerConnector);
            psuStoragePowerConnector.setPsu(psu);
        });
        psu.setStoragePowerConnectors(newStoragePowerConnectors);
    }

    private void checkNotExists(final String id, final Psu psu) {
        psuRepository.findAnotherByName(id, psu)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_PSU_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private Psu findById(final String id) {
        return psuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_PSU_NOT_FOUND, id)
                ));
    }
}
