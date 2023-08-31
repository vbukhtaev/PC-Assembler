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
import ru.bukhtaev.pcassembler.model.Cooler;
import ru.bukhtaev.pcassembler.model.dictionary.FanPowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.FanSize;
import ru.bukhtaev.pcassembler.model.dictionary.Socket;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;
import ru.bukhtaev.pcassembler.repository.CoolerRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.FanPowerConnectorRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.FanSizeRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.SocketRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.VendorRepository;
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
public class CoolerService {

    private final Translator translator;
    private final CoolerRepository coolerRepository;
    private final VendorRepository vendorRepository;
    private final FanSizeRepository fanSizeRepository;
    private final SocketRepository socketRepository;
    private final FanPowerConnectorRepository fanPowerConnectorRepository;

    @Autowired
    public CoolerService(
            final Translator translator,
            final CoolerRepository coolerRepository,
            final VendorRepository vendorRepository,
            final FanSizeRepository fanSizeRepository,
            final SocketRepository socketRepository,
            final FanPowerConnectorRepository fanPowerConnectorRepository
    ) {
        this.translator = translator;
        this.coolerRepository = coolerRepository;
        this.vendorRepository = vendorRepository;
        this.fanSizeRepository = fanSizeRepository;
        this.socketRepository = socketRepository;
        this.fanPowerConnectorRepository = fanPowerConnectorRepository;
    }

    public Cooler getById(@UUID final String id) {
        return findById(id);
    }

    public List<Cooler> getAll() {
        return coolerRepository.findAll();
    }

    public Slice<Cooler> getAll(@ValidPageable final Pageable pageable) {
        return coolerRepository.findAllBy(pageable);
    }

    public Cooler create(@Valid final Cooler newCooler) {
        setRelatedEntities(newCooler);
        checkNotExists(null, newCooler);

        return coolerRepository.save(newCooler);
    }

    public void delete(@UUID final String id) {
        coolerRepository.deleteById(id);
    }

    public Cooler update(@UUID final String id, final Cooler changedCooler) {
        final Cooler coolerToBeUpdated = findById(id);
        updateChangedProperties(coolerToBeUpdated, changedCooler);
        checkNotExists(id, coolerToBeUpdated);

        return coolerRepository.save(coolerToBeUpdated);
    }

    public Cooler replace(@UUID final String id, @Valid final Cooler newCooler) {
        setRelatedEntities(newCooler);
        checkNotExists(id, newCooler);

        final Cooler cooler = findById(id);
        newCooler.setId(id);
        newCooler.setCreatedAt(cooler.getCreatedAt());
        return coolerRepository.save(newCooler);
    }

    private void updateChangedProperties(final Cooler coolerToBeUpdated, final Cooler changedCooler) {
        Optional.ofNullable(changedCooler.getName()).ifPresent(coolerToBeUpdated::setName);
        Optional.ofNullable(changedCooler.getPowerDissipation()).ifPresent(coolerToBeUpdated::setPowerDissipation);
        Optional.ofNullable(changedCooler.getHeight()).ifPresent(coolerToBeUpdated::setHeight);

        final Vendor newVendor = changedCooler.getVendor();
        if (newVendor != null && newVendor.getId() != null) {
            final Vendor vendor = vendorRepository.findById(newVendor.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_VENDOR_NOT_FOUND, newVendor.getId())
                    ));
            coolerToBeUpdated.setVendor(vendor);
        }

        final FanSize newFanSize = changedCooler.getFanSize();
        if (newFanSize != null && newFanSize.getId() != null) {
            final FanSize fanSize = fanSizeRepository.findById(newFanSize.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_FAN_SIZE_NOT_FOUND, newFanSize.getId())
                    ));
            coolerToBeUpdated.setFanSize(fanSize);
        }

        final FanPowerConnector newFanPowerConnector = changedCooler.getFanPowerConnector();
        if (newFanPowerConnector != null && newFanPowerConnector.getId() != null) {
            final FanPowerConnector fanPowerConnector
                    = fanPowerConnectorRepository.findById(newFanPowerConnector.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_FAN_POWER_CONNECTOR_NOT_FOUND,
                                    newFanPowerConnector.getId()
                            )
                    ));
            coolerToBeUpdated.setFanPowerConnector(fanPowerConnector);
        }

        if (changedCooler.getSupportedSockets() != null) {
            final Set<Socket> foundSupportedSockets = Set.copyOf(changedCooler.getSupportedSockets())
                    .stream()
                    .map(socket -> socketRepository.findById(socket.getId())
                            .orElseThrow(() -> new NotFoundException(
                                    translator.getMessage(MESSAGE_CODE_SOCKET_NOT_FOUND, socket.getId())
                            )))
                    .collect(Collectors.toSet());
            coolerToBeUpdated.setSupportedSockets(foundSupportedSockets);
        }
    }

    private void setRelatedEntities(final Cooler cooler) {
        final Vendor newVendor = cooler.getVendor();
        if (newVendor == null || newVendor.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "vendor"
            );
        }

        final FanSize newFanSize = cooler.getFanSize();
        if (newFanSize == null || newFanSize.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "fanSize"
            );
        }

        final FanPowerConnector newFanPowerConnector = cooler.getFanPowerConnector();
        if (newFanPowerConnector == null || newFanPowerConnector.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "fanPowerConnector"
            );
        }

        if (cooler.getSupportedSockets() == null
                || cooler.getSupportedSockets().isEmpty()
                || cooler.getSupportedSockets().stream().anyMatch(Objects::isNull)
                || cooler.getSupportedSockets().stream().anyMatch(socket -> socket.getId() == null)
        ) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "supportedSockets"
            );
        }

        final Vendor foundVendor = vendorRepository.findById(newVendor.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_VENDOR_NOT_FOUND, newVendor.getId())
                ));
        cooler.setVendor(foundVendor);

        final FanSize foundFanSize = fanSizeRepository.findById(newFanSize.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_FAN_SIZE_NOT_FOUND, newFanSize.getId())
                ));
        cooler.setFanSize(foundFanSize);

        final FanPowerConnector foundFanPowerConnector = fanPowerConnectorRepository.findById(newFanPowerConnector.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(
                                MESSAGE_CODE_FAN_POWER_CONNECTOR_NOT_FOUND,
                                newFanPowerConnector.getId()
                        )
                ));
        cooler.setFanPowerConnector(foundFanPowerConnector);

        final Set<Socket> foundSupportedSockets = Set.copyOf(cooler.getSupportedSockets())
                .stream()
                .map(socket -> socketRepository.findById(socket.getId())
                        .orElseThrow(() -> new NotFoundException(
                                translator.getMessage(MESSAGE_CODE_SOCKET_NOT_FOUND, socket.getId())
                        )))
                .collect(Collectors.toSet());
        cooler.setSupportedSockets(foundSupportedSockets);
    }

    private void checkNotExists(final String id, final Cooler cooler) {
        coolerRepository.findAnotherByName(id, cooler)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_COOLER_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private Cooler findById(final String id) {
        return coolerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_COOLER_NOT_FOUND, id)
                ));
    }
}
