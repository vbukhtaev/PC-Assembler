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
import ru.bukhtaev.pcassembler.model.Chipset;
import ru.bukhtaev.pcassembler.model.Design;
import ru.bukhtaev.pcassembler.model.Motherboard;
import ru.bukhtaev.pcassembler.model.cross.MotherboardStorageConnector;
import ru.bukhtaev.pcassembler.model.dictionary.*;
import ru.bukhtaev.pcassembler.repository.ChipsetRepository;
import ru.bukhtaev.pcassembler.repository.DesignRepository;
import ru.bukhtaev.pcassembler.repository.MotherboardRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.*;
import ru.bukhtaev.pcassembler.util.exception.InvalidPropertyException;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniquePropertyException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.*;

@Service
@Validated
public class MotherboardService {

    private final Translator translator;
    private final MotherboardRepository motherboardRepository;
    private final DesignRepository designRepository;
    private final ChipsetRepository chipsetRepository;
    private final RamTypeRepository ramTypeRepository;
    private final StorageConnectorRepository storageConnectorRepository;
    private final MotherboardFormFactorRepository formFactorRepository;
    private final CpuPowerConnectorRepository cpuPowerConnectorRepository;
    private final MainPowerConnectorRepository mainPowerConnectorRepository;
    private final FanPowerConnectorRepository fanPowerConnectorRepository;
    private final PciExpressConnectorVersionRepository pciExpressConnectorVersionRepository;

    @Autowired
    public MotherboardService(
            final Translator translator,
            final MotherboardRepository motherboardRepository,
            final DesignRepository designRepository,
            final ChipsetRepository chipsetRepository,
            final RamTypeRepository ramTypeRepository,
            final StorageConnectorRepository storageConnectorRepository,
            final MotherboardFormFactorRepository formFactorRepository,
            final CpuPowerConnectorRepository cpuPowerConnectorRepository,
            final MainPowerConnectorRepository mainPowerConnectorRepository,
            final FanPowerConnectorRepository fanPowerConnectorRepository,
            final PciExpressConnectorVersionRepository pciExpressConnectorVersionRepository
    ) {
        this.translator = translator;
        this.motherboardRepository = motherboardRepository;
        this.designRepository = designRepository;
        this.chipsetRepository = chipsetRepository;
        this.ramTypeRepository = ramTypeRepository;
        this.storageConnectorRepository = storageConnectorRepository;
        this.formFactorRepository = formFactorRepository;
        this.cpuPowerConnectorRepository = cpuPowerConnectorRepository;
        this.mainPowerConnectorRepository = mainPowerConnectorRepository;
        this.fanPowerConnectorRepository = fanPowerConnectorRepository;
        this.pciExpressConnectorVersionRepository = pciExpressConnectorVersionRepository;
    }

    public Motherboard getById(@UUID final String id) {
        return findById(id);
    }

    public List<Motherboard> getAll() {
        return motherboardRepository.findAll();
    }

    public Slice<Motherboard> getAll(@ValidPageable final Pageable pageable) {
        return motherboardRepository.findAllBy(pageable);
    }

    public Motherboard create(@Valid final Motherboard newMotherboard) {
        setRelatedEntities(newMotherboard);
        checkNotExists(null, newMotherboard);

        return motherboardRepository.save(newMotherboard);
    }

    public void delete(@UUID final String id) {
        motherboardRepository.deleteById(id);
    }

    public Motherboard update(@UUID final String id, final Motherboard changedMotherboard) {
        final Motherboard motherboardBeUpdated = findById(id);
        updateChangedProperties(motherboardBeUpdated, changedMotherboard);
        checkNotExists(id, motherboardBeUpdated);

        return motherboardRepository.save(motherboardBeUpdated);
    }

    public Motherboard replace(@UUID final String id, @Valid final Motherboard newMotherboard) {
        setRelatedEntities(newMotherboard);
        checkNotExists(id, newMotherboard);

        final Motherboard motherboard = findById(id);
        newMotherboard.setId(id);
        newMotherboard.setCreatedAt(motherboard.getCreatedAt());
        return motherboardRepository.save(newMotherboard);
    }

    private void updateChangedProperties(final Motherboard motherboardBeUpdated, final Motherboard changedMotherboard) {
        Optional.ofNullable(changedMotherboard.getMaxMemoryClock())
                .ifPresent(motherboardBeUpdated::setMaxMemoryClock);
        Optional.ofNullable(changedMotherboard.getMaxMemoryOverClock())
                .ifPresent(motherboardBeUpdated::setMaxMemoryOverClock);
        Optional.ofNullable(changedMotherboard.getSlotsCount())
                .ifPresent(motherboardBeUpdated::setSlotsCount);

        final Design newDesign = changedMotherboard.getDesign();
        if (newDesign != null && newDesign.getId() != null) {
            final Design design = designRepository.findById(newDesign.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_DESIGN_NOT_FOUND, newDesign.getId())
                    ));
            motherboardBeUpdated.setDesign(design);
        }

        final Chipset newChipset = changedMotherboard.getChipset();
        if (newChipset != null && newChipset.getId() != null) {
            final Chipset chipset = chipsetRepository.findById(newChipset.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_CHIPSET_NOT_FOUND, newChipset.getId())
                    ));
            motherboardBeUpdated.setChipset(chipset);
        }

        final RamType newRamType = changedMotherboard.getRamType();
        if (newRamType != null && newRamType.getId() != null) {
            final RamType ramType = ramTypeRepository.findById(newRamType.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_RAM_TYPE_NOT_FOUND, newRamType.getId())
                    ));
            motherboardBeUpdated.setRamType(ramType);
        }

        final MotherboardFormFactor newFormFactor = changedMotherboard.getFormFactor();
        if (newFormFactor != null && newFormFactor.getId() != null) {
            final MotherboardFormFactor formFactor = formFactorRepository.findById(newFormFactor.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_MOTHERBOARD_FORM_FACTOR_NOT_FOUND, newFormFactor.getId())
                    ));
            motherboardBeUpdated.setFormFactor(formFactor);
        }

        final CpuPowerConnector newCpuPowerConnector = changedMotherboard.getCpuPowerConnector();
        if (newCpuPowerConnector != null && newCpuPowerConnector.getId() != null) {
            final CpuPowerConnector cpuPowerConnector
                    = cpuPowerConnectorRepository.findById(newCpuPowerConnector.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_CPU_POWER_CONNECTOR_NOT_FOUND,
                                    newCpuPowerConnector.getId()
                            )
                    ));
            motherboardBeUpdated.setCpuPowerConnector(cpuPowerConnector);
        }

        final MainPowerConnector newMainPowerConnector = changedMotherboard.getMainPowerConnector();
        if (newMainPowerConnector != null && newMainPowerConnector.getId() != null) {
            final MainPowerConnector mainPowerConnector
                    = mainPowerConnectorRepository.findById(newMainPowerConnector.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_MAIN_POWER_CONNECTOR_NOT_FOUND,
                                    newMainPowerConnector.getId()
                            )
                    ));
            motherboardBeUpdated.setMainPowerConnector(mainPowerConnector);
        }

        final FanPowerConnector newFanPowerConnector = changedMotherboard.getFanPowerConnector();
        if (newFanPowerConnector != null && newFanPowerConnector.getId() != null) {
            final FanPowerConnector fanPowerConnector
                    = fanPowerConnectorRepository.findById(newFanPowerConnector.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_FAN_POWER_CONNECTOR_NOT_FOUND,
                                    newFanPowerConnector.getId()
                            )
                    ));
            motherboardBeUpdated.setFanPowerConnector(fanPowerConnector);
        }

        final PciExpressConnectorVersion newPciExpressConnectorVersion
                = changedMotherboard.getPciExpressConnectorVersion();
        if (newPciExpressConnectorVersion != null && newPciExpressConnectorVersion.getId() != null) {
            final PciExpressConnectorVersion pciExpressConnectorVersion
                    = pciExpressConnectorVersionRepository.findById(newPciExpressConnectorVersion.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_PCI_EXPRESS_CONNECTOR_VERSION_NOT_FOUND,
                                    newPciExpressConnectorVersion.getId()
                            )
                    ));
            motherboardBeUpdated.setPciExpressConnectorVersion(pciExpressConnectorVersion);
        }

        final Set<MotherboardStorageConnector> newStorageConnectors = changedMotherboard.getStorageConnectors();
        if (newStorageConnectors != null) {
            newStorageConnectors.forEach(motherboardStorageConnector -> {
                final StorageConnector storageConnector
                        = storageConnectorRepository.findById(motherboardStorageConnector.getStorageConnector().getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_STORAGE_CONNECTOR_NOT_FOUND,
                                        motherboardStorageConnector.getStorageConnector().getId()
                                )
                        ));
                motherboardStorageConnector.setStorageConnector(storageConnector);
                motherboardStorageConnector.setMotherboard(motherboardBeUpdated);
            });
            motherboardBeUpdated.setStorageConnectors(newStorageConnectors);
        }
    }

    private void setRelatedEntities(final Motherboard motherboard) {
        final Chipset newChipset = motherboard.getChipset();
        if (newChipset == null || newChipset.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "chipset"
            );
        }

        final Design newDesign = motherboard.getDesign();
        if (newDesign == null || newDesign.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "design"
            );
        }

        final RamType newRamType = motherboard.getRamType();
        if (newRamType == null || newRamType.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "ramType"
            );
        }

        final MotherboardFormFactor newFormFactor = motherboard.getFormFactor();
        if (newFormFactor == null || newFormFactor.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "formFactor"
            );
        }

        final CpuPowerConnector newCpuPowerConnector = motherboard.getCpuPowerConnector();
        if (newCpuPowerConnector == null || newCpuPowerConnector.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "cpuPowerConnector"
            );
        }

        final MainPowerConnector newMainPowerConnector = motherboard.getMainPowerConnector();
        if (newMainPowerConnector == null || newMainPowerConnector.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "mainPowerConnector"
            );
        }

        final FanPowerConnector newFanPowerConnector = motherboard.getFanPowerConnector();
        if (newFanPowerConnector == null || newFanPowerConnector.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "fanPowerConnector"
            );
        }

        final PciExpressConnectorVersion newPciExpressConnectorVersion = motherboard.getPciExpressConnectorVersion();
        if (newPciExpressConnectorVersion == null || newPciExpressConnectorVersion.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "pciExpressConnectorVersion"
            );
        }

        final Set<MotherboardStorageConnector> newStorageConnectors = motherboard.getStorageConnectors();
        if (newStorageConnectors.stream().anyMatch(motherboardStorageConnector ->
                StringUtils.isBlank(motherboardStorageConnector.getStorageConnector().getId()))) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "storageConnectors"
            );
        }

        final Chipset foundChipset = chipsetRepository.findById(newChipset.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_CHIPSET_NOT_FOUND, newChipset.getId())
                ));
        motherboard.setChipset(foundChipset);

        final Design foundDesign = designRepository.findById(newDesign.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_DESIGN_NOT_FOUND, newDesign.getId())
                ));
        motherboard.setDesign(foundDesign);

        final RamType foundRamType = ramTypeRepository.findById(newRamType.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_RAM_TYPE_NOT_FOUND, newRamType.getId())
                ));
        motherboard.setRamType(foundRamType);

        final MotherboardFormFactor foundFormFactor = formFactorRepository.findById(newFormFactor.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(
                                MESSAGE_CODE_MOTHERBOARD_FORM_FACTOR_NOT_FOUND,
                                newFormFactor.getId()
                        )
                ));
        motherboard.setFormFactor(foundFormFactor);

        final CpuPowerConnector foundCpuPowerConnector
                = cpuPowerConnectorRepository.findById(newCpuPowerConnector.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(
                                MESSAGE_CODE_CPU_POWER_CONNECTOR_NOT_FOUND,
                                newCpuPowerConnector.getId()
                        )
                ));
        motherboard.setCpuPowerConnector(foundCpuPowerConnector);

        final MainPowerConnector foundMainPowerConnector
                = mainPowerConnectorRepository.findById(newMainPowerConnector.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(
                                MESSAGE_CODE_MAIN_POWER_CONNECTOR_NOT_FOUND,
                                newMainPowerConnector.getId()
                        )
                ));
        motherboard.setMainPowerConnector(foundMainPowerConnector);

        final FanPowerConnector foundFanPowerConnector
                = fanPowerConnectorRepository.findById(newFanPowerConnector.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(
                                MESSAGE_CODE_FAN_POWER_CONNECTOR_NOT_FOUND,
                                newFanPowerConnector.getId()
                        )
                ));
        motherboard.setFanPowerConnector(foundFanPowerConnector);

        final PciExpressConnectorVersion foundPciExpressConnectorVersion
                = pciExpressConnectorVersionRepository.findById(newPciExpressConnectorVersion.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(
                                MESSAGE_CODE_PCI_EXPRESS_CONNECTOR_VERSION_NOT_FOUND,
                                newPciExpressConnectorVersion.getId()
                        )
                ));
        motherboard.setPciExpressConnectorVersion(foundPciExpressConnectorVersion);

        newStorageConnectors.forEach(motherboardStorageConnector -> {
            final StorageConnector storageConnector
                    = storageConnectorRepository.findById(motherboardStorageConnector.getStorageConnector().getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_STORAGE_CONNECTOR_NOT_FOUND,
                                    motherboardStorageConnector.getStorageConnector().getId()
                            )
                    ));
            motherboardStorageConnector.setStorageConnector(storageConnector);
            motherboardStorageConnector.setMotherboard(motherboard);
        });
        motherboard.setStorageConnectors(newStorageConnectors);
    }

    private void checkNotExists(final String id, final Motherboard motherboard) {
        motherboardRepository.findAnotherByDesignAndChipsetAndRamType(id, motherboard)
                .ifPresent(entity -> {
                    if (!entity.getId().equals(id)) {
                        throw new UniquePropertyException(
                                translator.getMessage(
                                        MESSAGE_CODE_MOTHERBOARD_UNIQUE_DESIGN_AND_CHIPSET_AND_RAM_TYPE,
                                        entity.getDesign().getName(),
                                        entity.getChipset().getName(),
                                        entity.getRamType().getName()
                                ),
                                "design",
                                "chipset",
                                "ramType"
                        );
                    }
                });
    }

    private Motherboard findById(final String id) {
        return motherboardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_MOTHERBOARD_NOT_FOUND, id)
                ));
    }
}
