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
import ru.bukhtaev.pcassembler.model.*;
import ru.bukhtaev.pcassembler.model.cross.ComputerFan;
import ru.bukhtaev.pcassembler.model.cross.ComputerHdd;
import ru.bukhtaev.pcassembler.model.cross.ComputerRamModule;
import ru.bukhtaev.pcassembler.model.cross.ComputerSsd;
import ru.bukhtaev.pcassembler.repository.*;
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
public class ComputerBuildService {

    private final Translator translator;
    private final ComputerBuildRepository computerRepository;
    private final MotherboardRepository motherboardRepository;
    private final CpuRepository cpuRepository;
    private final CoolerRepository coolerRepository;
    private final GraphicsCardRepository graphicsCardRepository;
    private final PsuRepository psuRepository;
    private final ComputerCaseRepository computerCaseRepository;
    private final RamModuleRepository ramModuleRepository;
    private final HddRepository hddRepository;
    private final SsdRepository ssdRepository;
    private final FanRepository fanRepository;

    @Autowired
    public ComputerBuildService(
            final Translator translator,
            final ComputerBuildRepository computerRepository,
            final MotherboardRepository motherboardRepository,
            final CpuRepository cpuRepository,
            final CoolerRepository coolerRepository,
            final GraphicsCardRepository graphicsCardRepository,
            final PsuRepository psuRepository,
            final ComputerCaseRepository computerCaseRepository,
            final RamModuleRepository ramModuleRepository,
            final HddRepository hddRepository,
            final SsdRepository ssdRepository,
            final FanRepository fanRepository
    ) {
        this.translator = translator;
        this.computerRepository = computerRepository;
        this.motherboardRepository = motherboardRepository;
        this.cpuRepository = cpuRepository;
        this.coolerRepository = coolerRepository;
        this.graphicsCardRepository = graphicsCardRepository;
        this.psuRepository = psuRepository;
        this.computerCaseRepository = computerCaseRepository;
        this.ramModuleRepository = ramModuleRepository;
        this.hddRepository = hddRepository;
        this.ssdRepository = ssdRepository;
        this.fanRepository = fanRepository;
    }

    public ComputerBuild getById(@UUID final String id) {
        return findById(id);
    }

    public List<ComputerBuild> getAll() {
        return computerRepository.findAll();
    }

    public Slice<ComputerBuild> getAll(@ValidPageable final Pageable pageable) {
        return computerRepository.findAllBy(pageable);
    }

    public ComputerBuild create(@Valid final ComputerBuild newComputerBuild) {
        setRelatedEntities(newComputerBuild);
        checkNotExists(null, newComputerBuild);

        return computerRepository.save(newComputerBuild);
    }

    public void delete(@UUID final String id) {
        computerRepository.deleteById(id);
    }

    public ComputerBuild update(@UUID final String id, final ComputerBuild changedComputerBuild) {
        final ComputerBuild computerToBeUpdated = findById(id);
        updateChangedProperties(computerToBeUpdated, changedComputerBuild);
        checkNotExists(id, computerToBeUpdated);

        return computerRepository.save(computerToBeUpdated);
    }

    public ComputerBuild replace(@UUID final String id, @Valid final ComputerBuild newComputerBuild) {
        setRelatedEntities(newComputerBuild);
        checkNotExists(id, newComputerBuild);

        final ComputerBuild computer = findById(id);
        newComputerBuild.setId(id);
        newComputerBuild.setCreatedAt(computer.getCreatedAt());
        return computerRepository.save(newComputerBuild);
    }

    private void updateChangedProperties(final ComputerBuild computerToBeUpdated, final ComputerBuild changedComputerBuild) {
        Optional.ofNullable(changedComputerBuild.getName()).ifPresent(computerToBeUpdated::setName);

        final Motherboard newMotherboard = changedComputerBuild.getMotherboard();
        if (newMotherboard != null && newMotherboard.getId() != null) {
            final Motherboard motherboard = motherboardRepository.findById(newMotherboard.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_MOTHERBOARD_NOT_FOUND, newMotherboard.getId())
                    ));
            computerToBeUpdated.setMotherboard(motherboard);
        }

        final Cpu newCpu = changedComputerBuild.getCpu();
        if (newCpu != null && newCpu.getId() != null) {
            final Cpu cpu = cpuRepository.findById(newCpu.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_CPU_NOT_FOUND, newCpu.getId())
                    ));
            computerToBeUpdated.setCpu(cpu);
        }

        final Cooler newCooler = changedComputerBuild.getCooler();
        if (newCooler != null && newCooler.getId() != null) {
            final Cooler cooler = coolerRepository.findById(newCooler.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_COOLER_NOT_FOUND, newCooler.getId())
                    ));
            computerToBeUpdated.setCooler(cooler);
        }

        final GraphicsCard newGraphicsCard = changedComputerBuild.getGraphicsCard();
        if (newGraphicsCard != null && newGraphicsCard.getId() != null) {
            final GraphicsCard graphicsCard = graphicsCardRepository.findById(newGraphicsCard.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_GRAPHICS_CARD_NOT_FOUND, newGraphicsCard.getId())
                    ));
            computerToBeUpdated.setGraphicsCard(graphicsCard);
        }

        final Psu newPsu = changedComputerBuild.getPsu();
        if (newPsu != null && newPsu.getId() != null) {
            final Psu psu = psuRepository.findById(newPsu.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_PSU_NOT_FOUND, newPsu.getId())
                    ));
            computerToBeUpdated.setPsu(psu);
        }

        final ComputerCase newComputerCase = changedComputerBuild.getComputerCase();
        if (newComputerCase != null && newComputerCase.getId() != null) {
            final ComputerCase computerCase = computerCaseRepository.findById(newComputerCase.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_COMPUTER_CASE_NOT_FOUND, newComputerCase.getId())
                    ));
            computerToBeUpdated.setComputerCase(computerCase);
        }

        final Set<ComputerRamModule> newRamModules = changedComputerBuild.getRamModules();
        if (newRamModules != null) {
            newRamModules.forEach(computerRamModule -> {
                final RamModule ramModule = ramModuleRepository.findById(computerRamModule.getRamModule().getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_RAM_MODULE_NOT_FOUND,
                                        computerRamModule.getRamModule().getId()
                                )
                        ));
                computerRamModule.setRamModule(ramModule);
                computerRamModule.setComputer(computerToBeUpdated);
            });
            computerToBeUpdated.setRamModules(newRamModules);
        }

        final Set<ComputerHdd> newHdds = changedComputerBuild.getHdds();
        if (newHdds != null) {
            newHdds.forEach(computerHdd -> {
                final Hdd hdd = hddRepository.findById(computerHdd.getHdd().getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_HDD_NOT_FOUND,
                                        computerHdd.getHdd().getId()
                                )
                        ));
                computerHdd.setHdd(hdd);
                computerHdd.setComputer(computerToBeUpdated);
            });
            computerToBeUpdated.setHdds(newHdds);
        }

        final Set<ComputerSsd> newSsds = changedComputerBuild.getSsds();
        if (newSsds != null) {
            newSsds.forEach(computerSsd -> {
                final Ssd ssd = ssdRepository.findById(computerSsd.getSsd().getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_SSD_NOT_FOUND,
                                        computerSsd.getSsd().getId()
                                )
                        ));
                computerSsd.setSsd(ssd);
                computerSsd.setComputer(computerToBeUpdated);
            });
            computerToBeUpdated.setSsds(newSsds);
        }

        final Set<ComputerFan> newFans = changedComputerBuild.getFans();
        if (newFans != null) {
            newFans.forEach(computerFan -> {
                final Fan fan = fanRepository.findById(computerFan.getFan().getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(
                                        MESSAGE_CODE_FAN_NOT_FOUND,
                                        computerFan.getFan().getId()
                                )
                        ));
                computerFan.setFan(fan);
                computerFan.setComputer(computerToBeUpdated);
            });
            computerToBeUpdated.setFans(newFans);
        }
    }

    private void setRelatedEntities(final ComputerBuild computer) {
        final Motherboard newMotherboard = computer.getMotherboard();
        if (newMotherboard == null || newMotherboard.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "motherboard"
            );
        }

        final Cpu newCpu = computer.getCpu();
        if (newCpu == null || newCpu.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "cpu"
            );
        }

        final Cooler newCooler = computer.getCooler();
        if (newCooler == null || newCooler.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "cooler"
            );
        }

        final GraphicsCard newGraphicsCard = computer.getGraphicsCard();
        if (newGraphicsCard == null || newGraphicsCard.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "graphicsCard"
            );
        }

        final Psu newPsu = computer.getPsu();
        if (newPsu == null || newPsu.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "psu"
            );
        }

        final ComputerCase newComputerCase = computer.getComputerCase();
        if (newComputerCase == null || newComputerCase.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "computerCase"
            );
        }

        final Set<ComputerRamModule> newRamModules = computer.getRamModules();
        if (newRamModules.stream()
                .anyMatch(computerRamModule -> StringUtils.isBlank(computerRamModule.getRamModule().getId()))) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "ramModules"
            );
        }

        final Set<ComputerHdd> newHdds = computer.getHdds();
        if (newHdds.stream()
                .anyMatch(computerHdd -> StringUtils.isBlank(computerHdd.getHdd().getId()))) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "hdds"
            );
        }

        final Set<ComputerSsd> newSsds = computer.getSsds();
        if (newSsds.stream()
                .anyMatch(computerSsd -> StringUtils.isBlank(computerSsd.getSsd().getId()))) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "ssds"
            );
        }

        final Set<ComputerFan> newFans = computer.getFans();
        if (newFans.stream()
                .anyMatch(computerFan -> StringUtils.isBlank(computerFan.getFan().getId()))) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "fans"
            );
        }

        final Motherboard foundMotherboard = motherboardRepository.findById(newMotherboard.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_MOTHERBOARD_NOT_FOUND, newMotherboard.getId())
                ));
        computer.setMotherboard(foundMotherboard);

        final Cpu foundCpu = cpuRepository.findById(newCpu.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_CPU_NOT_FOUND, newCpu.getId())
                ));
        computer.setCpu(foundCpu);

        final Cooler foundCooler = coolerRepository.findById(newCooler.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_COOLER_NOT_FOUND, newCooler.getId())
                ));
        computer.setCooler(foundCooler);

        final GraphicsCard foundGraphicsCard = graphicsCardRepository.findById(newGraphicsCard.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_GRAPHICS_CARD_NOT_FOUND, newGraphicsCard.getId())
                ));
        computer.setGraphicsCard(foundGraphicsCard);

        final Psu foundPsu = psuRepository.findById(newPsu.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_PSU_NOT_FOUND, newPsu.getId())
                ));
        computer.setPsu(foundPsu);

        final ComputerCase foundComputerCase = computerCaseRepository.findById(newComputerCase.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_COMPUTER_CASE_NOT_FOUND, newComputerCase.getId())
                ));
        computer.setComputerCase(foundComputerCase);

        newRamModules.forEach(computerRamModule -> {
            final RamModule ramModule = ramModuleRepository.findById(computerRamModule.getRamModule().getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_RAM_MODULE_NOT_FOUND,
                                    computerRamModule.getRamModule().getId()
                            )
                    ));
            computerRamModule.setRamModule(ramModule);
            computerRamModule.setComputer(computer);
        });
        computer.setRamModules(newRamModules);

        newHdds.forEach(computerHdd -> {
            final Hdd hdd = hddRepository.findById(computerHdd.getHdd().getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_HDD_NOT_FOUND,
                                    computerHdd.getHdd().getId()
                            )
                    ));
            computerHdd.setHdd(hdd);
            computerHdd.setComputer(computer);
        });
        computer.setHdds(newHdds);

        newSsds.forEach(computerSsd -> {
            final Ssd ssd = ssdRepository.findById(computerSsd.getSsd().getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_SSD_NOT_FOUND,
                                    computerSsd.getSsd().getId()
                            )
                    ));
            computerSsd.setSsd(ssd);
            computerSsd.setComputer(computer);
        });
        computer.setSsds(newSsds);

        newFans.forEach(computerFan -> {
            final Fan fan = fanRepository.findById(computerFan.getFan().getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_FAN_NOT_FOUND,
                                    computerFan.getFan().getId()
                            )
                    ));
            computerFan.setFan(fan);
            computerFan.setComputer(computer);
        });
        computer.setFans(newFans);
    }

    private void checkNotExists(final String id, final ComputerBuild computer) {
        computerRepository.findAnotherByName(id, computer)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_COMPUTER_BUILD_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private ComputerBuild findById(final String id) {
        return computerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_COMPUTER_BUILD_NOT_FOUND, id)
                ));
    }
}
