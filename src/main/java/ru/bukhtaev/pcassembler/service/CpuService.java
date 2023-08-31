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
import ru.bukhtaev.pcassembler.model.Cpu;
import ru.bukhtaev.pcassembler.model.cross.CpuRamType;
import ru.bukhtaev.pcassembler.model.dictionary.Manufacturer;
import ru.bukhtaev.pcassembler.model.dictionary.RamType;
import ru.bukhtaev.pcassembler.model.dictionary.Socket;
import ru.bukhtaev.pcassembler.repository.CpuRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.ManufacturerRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.RamTypeRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.SocketRepository;
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
public class CpuService {

    private final Translator translator;
    private final CpuRepository cpuRepository;
    private final SocketRepository socketRepository;
    private final RamTypeRepository ramTypeRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Autowired
    public CpuService(
            final Translator translator,
            final CpuRepository cpuRepository,
            final SocketRepository socketRepository,
            final RamTypeRepository ramTypeRepository,
            final ManufacturerRepository manufacturerRepository
    ) {
        this.translator = translator;
        this.cpuRepository = cpuRepository;
        this.socketRepository = socketRepository;
        this.ramTypeRepository = ramTypeRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    public Cpu getById(@UUID final String id) {
        return findById(id);
    }

    public List<Cpu> getAll() {
        return cpuRepository.findAll();
    }

    public Slice<Cpu> getAll(@ValidPageable final Pageable pageable) {
        return cpuRepository.findAllBy(pageable);
    }

    public Cpu create(@Valid final Cpu newCpu) {
        setRelatedEntities(newCpu);
        checkNotExists(null, newCpu);

        return cpuRepository.save(newCpu);
    }

    public void delete(@UUID final String id) {
        cpuRepository.deleteById(id);
    }

    public Cpu update(@UUID final String id, final Cpu changedCpu) {
        final Cpu cpuToBeUpdated = findById(id);
        updateChangedProperties(cpuToBeUpdated, changedCpu);
        checkNotExists(id, cpuToBeUpdated);

        return cpuRepository.save(cpuToBeUpdated);
    }

    public Cpu replace(@UUID final String id, @Valid final Cpu newCpu) {
        setRelatedEntities(newCpu);
        checkNotExists(id, newCpu);

        final Cpu cpu = findById(id);
        newCpu.setId(id);
        newCpu.setCreatedAt(cpu.getCreatedAt());
        return cpuRepository.save(newCpu);
    }

    private void updateChangedProperties(final Cpu cpuToBeUpdated, final Cpu changedCpu) {
        Optional.ofNullable(changedCpu.getName()).ifPresent(cpuToBeUpdated::setName);
        Optional.ofNullable(changedCpu.getCoreCount()).ifPresent(cpuToBeUpdated::setCoreCount);
        Optional.ofNullable(changedCpu.getThreadCount()).ifPresent(cpuToBeUpdated::setThreadCount);
        Optional.ofNullable(changedCpu.getBaseClock()).ifPresent(cpuToBeUpdated::setBaseClock);
        Optional.ofNullable(changedCpu.getMaxClock()).ifPresent(cpuToBeUpdated::setMaxClock);
        Optional.ofNullable(changedCpu.getL3CacheSize()).ifPresent(cpuToBeUpdated::setL3CacheSize);
        Optional.ofNullable(changedCpu.getMaxTdp()).ifPresent(cpuToBeUpdated::setMaxTdp);

        final Manufacturer newManufacturer = changedCpu.getManufacturer();
        if (newManufacturer != null && newManufacturer.getId() != null) {
            final Manufacturer manufacturer = manufacturerRepository.findById(newManufacturer.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newManufacturer.getId())
                    ));
            cpuToBeUpdated.setManufacturer(manufacturer);
        }

        final Socket newSocket = changedCpu.getSocket();
        if (newSocket != null && newSocket.getId() != null) {
            final Socket socket = socketRepository.findById(newSocket.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_SOCKET_NOT_FOUND, newSocket.getId())
                    ));
            cpuToBeUpdated.setSocket(socket);
        }

        final Set<CpuRamType> newSupportedRamTypes = changedCpu.getSupportedRamTypes();
        if (newSupportedRamTypes != null) {
            newSupportedRamTypes.forEach(cpuRamType -> {
                final RamType ramType = ramTypeRepository.findById(cpuRamType.getRamType().getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_RAM_TYPE_NOT_FOUND,
                                        cpuRamType.getRamType().getId()
                                )
                        ));
                cpuRamType.setRamType(ramType);
                cpuRamType.setCpu(cpuToBeUpdated);
            });
            cpuToBeUpdated.setSupportedRamTypes(newSupportedRamTypes);
        }
    }

    private void setRelatedEntities(final Cpu cpu) {
        final Manufacturer newManufacturer = cpu.getManufacturer();
        if (newManufacturer == null || newManufacturer.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "manufacturer"
            );
        }

        final Socket newSocket = cpu.getSocket();
        if (newSocket == null || newSocket.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "socket"
            );
        }

        final Set<CpuRamType> newSupportedRamTypes = cpu.getSupportedRamTypes();
        if (newSupportedRamTypes.stream()
                .anyMatch(cpuRamType -> StringUtils.isBlank(cpuRamType.getRamType().getId()))) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "supportedRamTypes"
            );
        }

        final Manufacturer foundManufacturer = manufacturerRepository.findById(newManufacturer.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, newManufacturer.getId())
                ));
        cpu.setManufacturer(foundManufacturer);

        final Socket foundSocket = socketRepository.findById(newSocket.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_SOCKET_NOT_FOUND, newSocket.getId())
                ));
        cpu.setSocket(foundSocket);

        newSupportedRamTypes.forEach(cpuRamType -> {
            final RamType ramType = ramTypeRepository.findById(cpuRamType.getRamType().getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_RAM_TYPE_NOT_FOUND,
                                    cpuRamType.getRamType().getId()
                            )
                    ));
            cpuRamType.setRamType(ramType);
            cpuRamType.setCpu(cpu);
        });
        cpu.setSupportedRamTypes(newSupportedRamTypes);
    }

    private void checkNotExists(final String id, final Cpu cpu) {
        cpuRepository.findAnotherByName(id, cpu)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_CPU_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private Cpu findById(final String id) {
        return cpuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_CPU_NOT_FOUND, id)
                ));
    }
}
