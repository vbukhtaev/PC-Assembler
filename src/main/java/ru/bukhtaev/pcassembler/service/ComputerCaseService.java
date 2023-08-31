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
import ru.bukhtaev.pcassembler.model.ComputerCase;
import ru.bukhtaev.pcassembler.model.cross.ComputerCaseExpansionBay;
import ru.bukhtaev.pcassembler.model.cross.ComputerCaseFanSize;
import ru.bukhtaev.pcassembler.model.dictionary.*;
import ru.bukhtaev.pcassembler.repository.ComputerCaseRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.*;
import ru.bukhtaev.pcassembler.util.exception.InvalidPropertyException;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.*;

@Service
@Validated
public class ComputerCaseService {

    private final Translator translator;
    private final FanSizeRepository fanSizeRepository;
    private final ExpansionBayRepository expansionBayRepository;
    private final ComputerCaseRepository computerCaseRepository;
    private final PsuFormFactorRepository psuFormFactorRepository;
    private final MotherboardFormFactorRepository motherboardFormFactorRepository;
    private final VendorRepository vendorRepository;

    @Autowired
    public ComputerCaseService(
            final Translator translator,
            final FanSizeRepository fanSizeRepository,
            final ExpansionBayRepository expansionBayRepository,
            final ComputerCaseRepository computerCaseRepository,
            final PsuFormFactorRepository psuFormFactorRepository,
            final MotherboardFormFactorRepository motherboardFormFactorRepository,
            final VendorRepository vendorRepository
    ) {
        this.translator = translator;
        this.fanSizeRepository = fanSizeRepository;
        this.expansionBayRepository = expansionBayRepository;
        this.computerCaseRepository = computerCaseRepository;
        this.psuFormFactorRepository = psuFormFactorRepository;
        this.motherboardFormFactorRepository = motherboardFormFactorRepository;
        this.vendorRepository = vendorRepository;
    }

    public ComputerCase getById(@UUID final String id) {
        return findById(id);
    }

    public List<ComputerCase> getAll() {
        return computerCaseRepository.findAll();
    }

    public Slice<ComputerCase> getAll(@ValidPageable final Pageable pageable) {
        return computerCaseRepository.findAllBy(pageable);
    }

    public ComputerCase create(@Valid final ComputerCase newComputerCase) {
        setRelatedEntities(newComputerCase);
        checkNotExists(null, newComputerCase);

        return computerCaseRepository.save(newComputerCase);
    }

    public void delete(@UUID final String id) {
        computerCaseRepository.deleteById(id);
    }

    public ComputerCase update(@UUID final String id, final ComputerCase changedComputerCase) {
        final ComputerCase computerCaseToBeUpdated = findById(id);
        updateChangedProperties(computerCaseToBeUpdated, changedComputerCase);
        checkNotExists(id, computerCaseToBeUpdated);

        return computerCaseRepository.save(computerCaseToBeUpdated);
    }

    public ComputerCase replace(@UUID final String id, @Valid final ComputerCase newComputerCase) {
        setRelatedEntities(newComputerCase);
        checkNotExists(id, newComputerCase);

        final ComputerCase computerCase = findById(id);
        newComputerCase.setId(id);
        newComputerCase.setCreatedAt(computerCase.getCreatedAt());
        return computerCaseRepository.save(newComputerCase);
    }

    private void updateChangedProperties(
            final ComputerCase computerCaseToBeUpdated,
            final ComputerCase changedComputerCase
    ) {
        Optional.ofNullable(changedComputerCase.getName())
                .ifPresent(computerCaseToBeUpdated::setName);
        Optional.ofNullable(changedComputerCase.getPsuMaxLength())
                .ifPresent(computerCaseToBeUpdated::setPsuMaxLength);
        Optional.ofNullable(changedComputerCase.getGraphicsCardMaxLength())
                .ifPresent(computerCaseToBeUpdated::setGraphicsCardMaxLength);
        Optional.ofNullable(changedComputerCase.getCoolerMaxHeight())
                .ifPresent(computerCaseToBeUpdated::setCoolerMaxHeight);

        final Vendor newVendor = changedComputerCase.getVendor();
        if (newVendor != null && newVendor.getId() != null) {
            final Vendor vendor = vendorRepository.findById(newVendor.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_VENDOR_NOT_FOUND, newVendor.getId())
                    ));
            computerCaseToBeUpdated.setVendor(vendor);
        }

        final Set<MotherboardFormFactor> supportedMotherboardFormFactors
                = changedComputerCase.getSupportedMotherboardFormFactors();
        if (supportedMotherboardFormFactors != null) {
            final Set<MotherboardFormFactor> foundFormFactors = Set.copyOf(supportedMotherboardFormFactors)
                    .stream()
                    .map(formFactor -> motherboardFormFactorRepository.findById(formFactor.getId())
                            .orElseThrow(() -> new NotFoundException(
                                    translator.getMessage(
                                            MESSAGE_CODE_MOTHERBOARD_FORM_FACTOR_NOT_FOUND,
                                            formFactor.getId()
                                    )
                            )))
                    .collect(Collectors.toSet());
            computerCaseToBeUpdated.setSupportedMotherboardFormFactors(foundFormFactors);
        }

        final Set<PsuFormFactor> supportedPsuFormFactors
                = changedComputerCase.getSupportedPsuFormFactors();
        if (supportedPsuFormFactors != null) {
            final Set<PsuFormFactor> foundFormFactors = Set.copyOf(supportedPsuFormFactors)
                    .stream()
                    .map(formFactor -> psuFormFactorRepository.findById(formFactor.getId())
                            .orElseThrow(() -> new NotFoundException(
                                    translator.getMessage(
                                            MESSAGE_CODE_PSU_FORM_FACTOR_NOT_FOUND,
                                            formFactor.getId()
                                    )
                            )))
                    .collect(Collectors.toSet());
            computerCaseToBeUpdated.setSupportedPsuFormFactors(foundFormFactors);
        }

        final Set<ComputerCaseExpansionBay> newSupportedExpansionBays = changedComputerCase.getSupportedExpansionBays();
        if (newSupportedExpansionBays != null) {
            newSupportedExpansionBays.forEach(caseExpansionBay -> {
                final ExpansionBay expansionBay
                        = expansionBayRepository.findById(caseExpansionBay.getExpansionBay().getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_EXPANSION_BAY_NOT_FOUND,
                                        caseExpansionBay.getExpansionBay().getId()
                                )
                        ));
                caseExpansionBay.setExpansionBay(expansionBay);
                caseExpansionBay.setComputerCase(computerCaseToBeUpdated);
            });
            computerCaseToBeUpdated.setSupportedExpansionBays(newSupportedExpansionBays);
        }

        final Set<ComputerCaseFanSize> newSupportedFanSizes = changedComputerCase.getSupportedFanSizes();
        if (newSupportedFanSizes != null) {
            newSupportedFanSizes.forEach(caseFanSize -> {
                final FanSize fanSize = fanSizeRepository.findById(caseFanSize.getFanSize().getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_FAN_SIZE_NOT_FOUND,
                                        caseFanSize.getFanSize().getId()
                                )
                        ));
                caseFanSize.setFanSize(fanSize);
                caseFanSize.setComputerCase(computerCaseToBeUpdated);
            });
            computerCaseToBeUpdated.setSupportedFanSizes(newSupportedFanSizes);
        }
    }

    private void setRelatedEntities(final ComputerCase computerCase) {
        final Vendor newVendor = computerCase.getVendor();
        if (newVendor == null || newVendor.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "vendor"
            );
        }

        if (computerCase.getSupportedMotherboardFormFactors() == null
                || computerCase.getSupportedMotherboardFormFactors().isEmpty()
                || computerCase.getSupportedMotherboardFormFactors().stream().anyMatch(Objects::isNull)
                || computerCase.getSupportedMotherboardFormFactors()
                .stream().anyMatch(formFactor -> formFactor.getId() == null)
        ) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "supportedMotherboardFormFactors"
            );
        }

        if (computerCase.getSupportedPsuFormFactors() == null
                || computerCase.getSupportedPsuFormFactors().isEmpty()
                || computerCase.getSupportedPsuFormFactors().stream().anyMatch(Objects::isNull)
                || computerCase.getSupportedPsuFormFactors().stream().anyMatch(formFactor -> formFactor.getId() == null)
        ) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "supportedPsuFormFactors"
            );
        }

        final Set<ComputerCaseExpansionBay> newSupportedExpansionBays = computerCase.getSupportedExpansionBays();
        if (newSupportedExpansionBays.stream()
                .anyMatch(caseExpansionBay -> StringUtils.isBlank(caseExpansionBay.getExpansionBay().getId()))) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "supportedExpansionBays"
            );
        }

        final Set<ComputerCaseFanSize> newSupportedFanSizes = computerCase.getSupportedFanSizes();
        if (newSupportedFanSizes.stream()
                .anyMatch(caseFanSize -> StringUtils.isBlank(caseFanSize.getFanSize().getId()))) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "supportedFanSizes"
            );
        }

        final Vendor foundVendor = vendorRepository.findById(newVendor.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_VENDOR_NOT_FOUND, newVendor.getId())
                ));
        computerCase.setVendor(foundVendor);

        final Set<MotherboardFormFactor> foundMotherboardFormFactors
                = Set.copyOf(computerCase.getSupportedMotherboardFormFactors())
                .stream()
                .map(formFactor -> motherboardFormFactorRepository.findById(formFactor.getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_MOTHERBOARD_FORM_FACTOR_NOT_FOUND,
                                        formFactor.getId()
                                )
                        )))
                .collect(Collectors.toSet());
        computerCase.setSupportedMotherboardFormFactors(foundMotherboardFormFactors);

        final Set<PsuFormFactor> foundPsuFormFactors = Set.copyOf(computerCase.getSupportedPsuFormFactors())
                .stream()
                .map(formFactor -> psuFormFactorRepository.findById(formFactor.getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(MESSAGE_CODE_PSU_FORM_FACTOR_NOT_FOUND, formFactor.getId())
                        )))
                .collect(Collectors.toSet());
        computerCase.setSupportedPsuFormFactors(foundPsuFormFactors);

        newSupportedExpansionBays.forEach(caseExpansionBay -> {
            final ExpansionBay expansionBay
                    = expansionBayRepository.findById(caseExpansionBay.getExpansionBay().getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_EXPANSION_BAY_NOT_FOUND,
                                    caseExpansionBay.getExpansionBay().getId()
                            )
                    ));
            caseExpansionBay.setExpansionBay(expansionBay);
            caseExpansionBay.setComputerCase(computerCase);
        });
        computerCase.setSupportedExpansionBays(newSupportedExpansionBays);

        newSupportedFanSizes.forEach(caseFanSize -> {
            final FanSize fanSize = fanSizeRepository.findById(caseFanSize.getFanSize().getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_FAN_SIZE_NOT_FOUND, caseFanSize.getFanSize().getId())
                    ));
            caseFanSize.setFanSize(fanSize);
            caseFanSize.setComputerCase(computerCase);
        });
        computerCase.setSupportedFanSizes(newSupportedFanSizes);
    }

    private void checkNotExists(final String id, final ComputerCase computerCase) {
        computerCaseRepository.findAnotherByName(id, computerCase)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_COMPUTER_CASE_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private ComputerCase findById(final String id) {
        return computerCaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_COMPUTER_CASE_NOT_FOUND, id)
                ));
    }
}
