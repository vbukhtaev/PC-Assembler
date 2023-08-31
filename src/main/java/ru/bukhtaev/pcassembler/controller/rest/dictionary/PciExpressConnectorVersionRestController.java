package ru.bukhtaev.pcassembler.controller.rest.dictionary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.dictionary.mapper.PciExpressConnectorVersionMapper;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;
import ru.bukhtaev.pcassembler.model.dictionary.PciExpressConnectorVersion;
import ru.bukhtaev.pcassembler.service.dictionary.PciExpressConnectorVersionService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Версии разъема PCI-Express")
@RestController
@RequestMapping(PciExpressConnectorVersionRestController.API_V1_PCI_EXPRESS_CONNECTOR_VERSIONS)
public class PciExpressConnectorVersionRestController {

    public static final String API_V1_PCI_EXPRESS_CONNECTOR_VERSIONS = "/api/v1/pci-express-connector-versions";

    private final PciExpressConnectorVersionService pciExpressConnectorVersionService;
    private final PciExpressConnectorVersionMapper mapper;

    @Autowired
    public PciExpressConnectorVersionRestController(
            final PciExpressConnectorVersionService pciExpressConnectorVersionService,
            final MapperResolver mapperResolver
    ) {
        this.pciExpressConnectorVersionService = pciExpressConnectorVersionService;
        this.mapper = mapperResolver.resolve(PciExpressConnectorVersion.class);
    }

    @Operation(summary = "Получение всех версий разъема PCI-Express")
    @GetMapping
    public ResponseEntity<List<NameableDto>> handleGetAll() {
        return ResponseEntity.ok(
                pciExpressConnectorVersionService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех версий разъема PCI-Express (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<NameableDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                pciExpressConnectorVersionService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение версии разъема PCI-Express по ID")
    @GetMapping("/{id}")
    public ResponseEntity<NameableDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(pciExpressConnectorVersionService.getById(id))
        );
    }

    @Operation(summary = "Создание версии разъема PCI-Express")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<NameableDto> handleCreate(
            @RequestBody final NameableRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final NameableDto savedDto = mapper.toDto(
                pciExpressConnectorVersionService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_PCI_EXPRESS_CONNECTOR_VERSIONS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение версии разъема PCI-Express")
    @PatchMapping("/{id}")
    public ResponseEntity<NameableDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        pciExpressConnectorVersionService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена версии разъема PCI-Express")
    @PutMapping("/{id}")
    public ResponseEntity<NameableDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        pciExpressConnectorVersionService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление версии разъема PCI-Express по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        pciExpressConnectorVersionService.delete(id);
    }
}
