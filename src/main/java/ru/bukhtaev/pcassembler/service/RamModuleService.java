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
import ru.bukhtaev.pcassembler.model.Design;
import ru.bukhtaev.pcassembler.model.RamModule;
import ru.bukhtaev.pcassembler.model.dictionary.RamType;
import ru.bukhtaev.pcassembler.repository.DesignRepository;
import ru.bukhtaev.pcassembler.repository.RamModuleRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.RamTypeRepository;
import ru.bukhtaev.pcassembler.util.exception.InvalidPropertyException;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniquePropertyException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.*;

@Service
@Validated
public class RamModuleService {

    private final Translator translator;
    private final RamModuleRepository ramModuleRepository;
    private final RamTypeRepository ramTypeRepository;
    private final DesignRepository designRepository;

    @Autowired
    public RamModuleService(
            final Translator translator,
            final RamModuleRepository ramModuleRepository,
            final RamTypeRepository ramTypeRepository,
            final DesignRepository designRepository
    ) {
        this.translator = translator;
        this.ramModuleRepository = ramModuleRepository;
        this.ramTypeRepository = ramTypeRepository;
        this.designRepository = designRepository;
    }

    public RamModule getById(@UUID final String id) {
        return findById(id);
    }

    public List<RamModule> getAll() {
        return ramModuleRepository.findAll();
    }

    public Slice<RamModule> getAll(@ValidPageable final Pageable pageable) {
        return ramModuleRepository.findAllBy(pageable);
    }

    public RamModule create(@Valid final RamModule newRamModule) {
        setRelatedEntities(newRamModule);
        checkNotExists(null, newRamModule);

        return ramModuleRepository.save(newRamModule);
    }

    public void delete(@UUID final String id) {
        ramModuleRepository.deleteById(id);
    }

    public RamModule update(@UUID final String id, final RamModule changedRamModule) {
        final RamModule ramModuleToBeUpdated = findById(id);
        updateChangedProperties(ramModuleToBeUpdated, changedRamModule);
        checkNotExists(id, ramModuleToBeUpdated);

        return ramModuleRepository.save(ramModuleToBeUpdated);

    }

    public RamModule replace(@UUID final String id, @Valid final RamModule newRamModule) {
        setRelatedEntities(newRamModule);
        checkNotExists(id, newRamModule);

        final RamModule ramModule = findById(id);
        newRamModule.setId(id);
        newRamModule.setCreatedAt(ramModule.getCreatedAt());
        return ramModuleRepository.save(newRamModule);
    }

    private void updateChangedProperties(final RamModule ramModuleToBeUpdated, final RamModule changedRamModule) {
        Optional.ofNullable(changedRamModule.getClock()).ifPresent(ramModuleToBeUpdated::setClock);
        Optional.ofNullable(changedRamModule.getCapacity()).ifPresent(ramModuleToBeUpdated::setCapacity);

        final RamType newRamType = changedRamModule.getType();
        if (newRamType != null && newRamType.getId() != null) {
            final RamType ramType = ramTypeRepository.findById(newRamType.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_RAM_TYPE_NOT_FOUND, newRamType.getId())
                    ));

            ramModuleToBeUpdated.setType(ramType);
        }

        final Design newDesign = changedRamModule.getDesign();
        if (newDesign != null && newDesign.getId() != null) {
            final Design design = designRepository.findById(newDesign.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_DESIGN_NOT_FOUND, newDesign.getId())
                    ));

            ramModuleToBeUpdated.setDesign(design);
        }
    }

    private void setRelatedEntities(final RamModule ramModule) {
        final RamType newRamType = ramModule.getType();
        if (newRamType == null || newRamType.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "type"
            );
        }

        final Design newDesign = ramModule.getDesign();
        if (newDesign == null || newDesign.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "design"
            );
        }

        final RamType foundRamType = ramTypeRepository.findById(newRamType.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_RAM_TYPE_NOT_FOUND, newRamType.getId())
                ));
        ramModule.setType(foundRamType);

        final Design foundDesign = designRepository.findById(newDesign.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_DESIGN_NOT_FOUND, newDesign.getId())
                ));
        ramModule.setDesign(foundDesign);
    }

    private void checkNotExists(final String id, final RamModule module) {
        ramModuleRepository.findByClockAndCapacityAndTypeAndDesign(id, module)
                .ifPresent(entity -> {
                    throw new UniquePropertyException(
                            translator.getMessage(
                                    MESSAGE_CODE_RAM_MODULE_UNIQUE_CLOCK_AND_CAPACITY_AND_TYPE_AND_DESIGN,
                                    entity.getClock(),
                                    entity.getCapacity(),
                                    entity.getType().getName(),
                                    entity.getDesign().getName()
                            ),
                            "clock",
                            "capacity",
                            "type",
                            "design"
                    );
                });
    }

    private RamModule findById(final String id) {
        return ramModuleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_RAM_MODULE_NOT_FOUND, id)
                ));
    }
}