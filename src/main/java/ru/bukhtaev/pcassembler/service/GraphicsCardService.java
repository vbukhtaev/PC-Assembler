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
import ru.bukhtaev.pcassembler.model.Gpu;
import ru.bukhtaev.pcassembler.model.GraphicsCard;
import ru.bukhtaev.pcassembler.model.dictionary.GraphicsCardPowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.PciExpressConnectorVersion;
import ru.bukhtaev.pcassembler.repository.DesignRepository;
import ru.bukhtaev.pcassembler.repository.GpuRepository;
import ru.bukhtaev.pcassembler.repository.GraphicsCardRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.GraphicsCardPowerConnectorRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.PciExpressConnectorVersionRepository;
import ru.bukhtaev.pcassembler.util.exception.InvalidPropertyException;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniquePropertyException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.*;

@Service
@Validated
public class GraphicsCardService {

    private final Translator translator;
    private final GraphicsCardRepository graphicsCardRepository;
    private final GpuRepository gpuRepository;
    private final DesignRepository designRepository;
    private final GraphicsCardPowerConnectorRepository powerConnectorRepository;
    private final PciExpressConnectorVersionRepository pciExpressConnectorVersionRepository;

    @Autowired
    public GraphicsCardService(
            final Translator translator,
            final GraphicsCardRepository graphicsCardRepository,
            final GpuRepository gpuRepository,
            final DesignRepository designRepository,
            final GraphicsCardPowerConnectorRepository powerConnectorRepository,
            final PciExpressConnectorVersionRepository pciExpressConnectorVersionRepository
    ) {
        this.translator = translator;
        this.graphicsCardRepository = graphicsCardRepository;
        this.gpuRepository = gpuRepository;
        this.designRepository = designRepository;
        this.powerConnectorRepository = powerConnectorRepository;
        this.pciExpressConnectorVersionRepository = pciExpressConnectorVersionRepository;
    }

    public GraphicsCard getById(@UUID final String id) {
        return findById(id);
    }

    public List<GraphicsCard> getAll() {
        return graphicsCardRepository.findAll();
    }

    public Slice<GraphicsCard> getAll(@ValidPageable final Pageable pageable) {
        return graphicsCardRepository.findAllBy(pageable);
    }

    public GraphicsCard create(@Valid final GraphicsCard newGraphicsCard) {
        checkNotExists(null, newGraphicsCard);
        setRelatedEntities(newGraphicsCard);

        return graphicsCardRepository.save(newGraphicsCard);
    }

    public void delete(@UUID final String id) {
        graphicsCardRepository.deleteById(id);
    }

    public GraphicsCard update(@UUID final String id, final GraphicsCard changedGraphicsCard) {
        final GraphicsCard graphicsCarBeUpdated = findById(id);
        updateChangedProperties(graphicsCarBeUpdated, changedGraphicsCard);
        checkNotExists(id, graphicsCarBeUpdated);

        return graphicsCardRepository.save(graphicsCarBeUpdated);
    }

    public GraphicsCard replace(@UUID final String id, @Valid final GraphicsCard newGraphicsCard) {
        setRelatedEntities(newGraphicsCard);
        checkNotExists(id, newGraphicsCard);

        final GraphicsCard graphicsCard = findById(id);
        newGraphicsCard.setId(id);
        newGraphicsCard.setCreatedAt(graphicsCard.getCreatedAt());
        return graphicsCardRepository.save(newGraphicsCard);
    }

    private void updateChangedProperties(final GraphicsCard graphicsCarBeUpdated, final GraphicsCard changedGraphicsCard) {
        Optional.ofNullable(changedGraphicsCard.getLength()).ifPresent(graphicsCarBeUpdated::setLength);

        final Gpu newGpu = changedGraphicsCard.getGpu();
        if (newGpu != null && newGpu.getId() != null) {
            final Gpu gpu = gpuRepository.findById(newGpu.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_GPU_NOT_FOUND, newGpu.getId())
                    ));
            graphicsCarBeUpdated.setGpu(gpu);
        }

        final Design newDesign = changedGraphicsCard.getDesign();
        if (newDesign != null && newDesign.getId() != null) {
            final Design design = designRepository.findById(newDesign.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_DESIGN_NOT_FOUND, newDesign.getId())
                    ));
            graphicsCarBeUpdated.setDesign(design);
        }

        final PciExpressConnectorVersion newPciExpressConnectorVersion
                = changedGraphicsCard.getPciExpressConnectorVersion();
        if (newPciExpressConnectorVersion != null && newPciExpressConnectorVersion.getId() != null) {
            final PciExpressConnectorVersion pciExpressConnectorVersion
                    = pciExpressConnectorVersionRepository.findById(newPciExpressConnectorVersion.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_PCI_EXPRESS_CONNECTOR_VERSION_NOT_FOUND,
                                    newPciExpressConnectorVersion.getId()
                            )
                    ));
            graphicsCarBeUpdated.setPciExpressConnectorVersion(pciExpressConnectorVersion);
        }

        final GraphicsCardPowerConnector newPowerConnector = changedGraphicsCard.getPowerConnector();
        if (newPowerConnector != null && newPowerConnector.getId() != null) {
            final GraphicsCardPowerConnector powerConnector
                    = powerConnectorRepository.findById(newPowerConnector.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(
                                    MESSAGE_CODE_GRAPHICS_CARD_POWER_CONNECTOR_NOT_FOUND,
                                    newPowerConnector.getId()
                            )
                    ));
            graphicsCarBeUpdated.setPowerConnector(powerConnector);
        }
    }

    private void setRelatedEntities(final GraphicsCard graphicsCard) {
        final Gpu newGpu = graphicsCard.getGpu();
        if (newGpu == null || newGpu.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "gpu"
            );
        }

        final Design newDesign = graphicsCard.getDesign();
        if (newDesign == null || newDesign.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "design"
            );
        }

        final PciExpressConnectorVersion newPciExpressConnectorVersion = graphicsCard.getPciExpressConnectorVersion();
        if (newPciExpressConnectorVersion == null || newPciExpressConnectorVersion.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "pciExpressConnectorVersion"
            );
        }

        final GraphicsCardPowerConnector newPowerConnector = graphicsCard.getPowerConnector();
        if (newPowerConnector == null || newPowerConnector.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "powerConnector"
            );
        }

        final Gpu foundGpu = gpuRepository.findById(newGpu.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_GPU_NOT_FOUND, newGpu.getId())
                ));
        graphicsCard.setGpu(foundGpu);

        final Design foundDesign = designRepository.findById(newDesign.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_DESIGN_NOT_FOUND, newDesign.getId())
                ));
        graphicsCard.setDesign(foundDesign);

        final PciExpressConnectorVersion foundPciExpressConnectorVersion
                = pciExpressConnectorVersionRepository.findById(newPciExpressConnectorVersion.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(
                                MESSAGE_CODE_PCI_EXPRESS_CONNECTOR_VERSION_NOT_FOUND,
                                newPciExpressConnectorVersion.getId()
                        )
                ));
        graphicsCard.setPciExpressConnectorVersion(foundPciExpressConnectorVersion);

        final GraphicsCardPowerConnector foundPowerConnector
                = powerConnectorRepository.findById(newPowerConnector.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(
                                MESSAGE_CODE_PCI_EXPRESS_CONNECTOR_VERSION_NOT_FOUND,
                                newPowerConnector.getId()
                        )
                ));
        graphicsCard.setPowerConnector(foundPowerConnector);
    }

    private void checkNotExists(final String id, final GraphicsCard graphicsCard) {
        graphicsCardRepository.findAnotherByGpuAndDesign(id, graphicsCard)
                .ifPresent(entity -> {
                    throw new UniquePropertyException(
                            translator.getMessage(
                                    MESSAGE_CODE_GRAPHICS_CARD_UNIQUE_GPU_AND_DESIGN,
                                    entity.getGpu().getName(),
                                    entity.getDesign().getName()
                            ),
                            "gpu",
                            "design"
                    );
                });
    }

    private GraphicsCard findById(final String id) {
        return graphicsCardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_GRAPHICS_CARD_NOT_FOUND, id)
                ));
    }
}
