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
import ru.bukhtaev.pcassembler.model.Gpu;
import ru.bukhtaev.pcassembler.model.dictionary.Manufacturer;
import ru.bukhtaev.pcassembler.model.dictionary.VideoMemoryType;
import ru.bukhtaev.pcassembler.repository.GpuRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.ManufacturerRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.VideoMemoryTypeRepository;
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
public class GpuService {

    private final Translator translator;
    private final GpuRepository gpuRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final VideoMemoryTypeRepository videoMemoryTypeRepository;

    @Autowired
    public GpuService(
            final Translator translator,
            final GpuRepository gpuRepository,
            final ManufacturerRepository manufacturerRepository,
            final VideoMemoryTypeRepository videoMemoryTypeRepository
    ) {
        this.translator = translator;
        this.gpuRepository = gpuRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.videoMemoryTypeRepository = videoMemoryTypeRepository;
    }

    public Gpu getById(@UUID final String id) {
        return findById(id);
    }

    public List<Gpu> getAll() {
        return gpuRepository.findAll();
    }

    public Slice<Gpu> getAll(@ValidPageable final Pageable pageable) {
        return gpuRepository.findAllBy(pageable);
    }

    public Gpu create(@Valid final Gpu newGpu) {
        setRelatedEntities(newGpu);
        checkNotExists(null, newGpu);

        return gpuRepository.save(newGpu);
    }

    public void delete(@UUID final String id) {
        gpuRepository.deleteById(id);
    }

    public Gpu update(@UUID final String id, final Gpu changedGpu) {
        final Gpu gpuToBeUpdated = findById(id);
        updateChangedProperties(gpuToBeUpdated, changedGpu);
        checkNotExists(id, gpuToBeUpdated);

        return gpuRepository.save(gpuToBeUpdated);
    }

    public Gpu replace(@UUID final String id, @Valid final Gpu newGpu) {
        setRelatedEntities(newGpu);
        checkNotExists(id, newGpu);

        final Gpu gpu = findById(id);
        newGpu.setId(id);
        newGpu.setCreatedAt(gpu.getCreatedAt());
        return gpuRepository.save(newGpu);
    }

    private void updateChangedProperties(final Gpu gpuToBeUpdated, final Gpu changedGpu) {
        Optional.ofNullable(changedGpu.getName()).ifPresent(gpuToBeUpdated::setName);
        Optional.ofNullable(changedGpu.getMemorySize()).ifPresent(gpuToBeUpdated::setMemorySize);
        Optional.ofNullable(changedGpu.getPowerConsumption()).ifPresent(gpuToBeUpdated::setPowerConsumption);

        final Manufacturer newManufacturer = changedGpu.getManufacturer();
        if (newManufacturer != null && newManufacturer.getId() != null) {
            final Manufacturer manufacturer = manufacturerRepository.findById(newManufacturer.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newManufacturer.getId())
                    ));
            gpuToBeUpdated.setManufacturer(manufacturer);
        }

        final VideoMemoryType newVideoMemoryType = changedGpu.getMemoryType();
        if (newVideoMemoryType != null && newVideoMemoryType.getId() != null) {
            final VideoMemoryType videoMemoryType = videoMemoryTypeRepository.findById(newVideoMemoryType.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_VIDEO_MEMORY_TYPE_NOT_FOUND, newVideoMemoryType.getId())
                    ));
            gpuToBeUpdated.setMemoryType(videoMemoryType);
        }
    }

    private void setRelatedEntities(final Gpu gpu) {
        final Manufacturer newManufacturer = gpu.getManufacturer();
        if (newManufacturer == null || newManufacturer.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "manufacturer"
            );
        }

        final VideoMemoryType newVideoMemoryType = gpu.getMemoryType();
        if (newVideoMemoryType == null || newVideoMemoryType.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "memoryType"
            );
        }

        final Manufacturer foundManufacturer = manufacturerRepository.findById(newManufacturer.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newManufacturer.getId())
                ));
        gpu.setManufacturer(foundManufacturer);

        final VideoMemoryType foundVideoMemoryType = videoMemoryTypeRepository.findById(newVideoMemoryType.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_VIDEO_MEMORY_TYPE_NOT_FOUND, newVideoMemoryType.getId())
                ));
        gpu.setMemoryType(foundVideoMemoryType);
    }

    private void checkNotExists(final String id, final Gpu gpu) {
        gpuRepository.findAnotherByNameAndMemorySize(id, gpu)
                .ifPresent(entity -> {
                    if (!entity.getId().equals(id)) {
                        throw new UniquePropertyException(
                                translator.getMessage(
                                        MESSAGE_CODE_GPU_UNIQUE_NAME_AND_MEMORY_SIZE,
                                        entity.getName(),
                                        entity.getMemorySize()
                                ),
                                FIELD_NAME,
                                "memorySize"
                        );
                    }
                });
    }

    private Gpu findById(final String id) {
        return gpuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_GPU_NOT_FOUND, id)
                ));
    }
}
