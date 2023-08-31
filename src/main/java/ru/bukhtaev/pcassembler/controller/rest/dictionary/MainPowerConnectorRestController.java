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
import ru.bukhtaev.pcassembler.dto.dictionary.mapper.MainPowerConnectorMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;
import ru.bukhtaev.pcassembler.model.dictionary.MainPowerConnector;
import ru.bukhtaev.pcassembler.service.dictionary.MainPowerConnectorService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Основные разъемы питания")
@RestController
@RequestMapping(MainPowerConnectorRestController.API_V1_MAIN_POWER_CONNECTORS)
public class MainPowerConnectorRestController {

    public static final String API_V1_MAIN_POWER_CONNECTORS = "/api/v1/main-power-connectors";

    private final MainPowerConnectorService mainPowerConnectorService;
    private final MainPowerConnectorMapper mapper;

    @Autowired
    public MainPowerConnectorRestController(
            final MainPowerConnectorService mainPowerConnectorService,
            final MapperResolver mapperResolver
    ) {
        this.mainPowerConnectorService = mainPowerConnectorService;
        this.mapper = mapperResolver.resolve(MainPowerConnector.class);
    }

    @Operation(summary = "Получение всех основных разъемов питания")
    @GetMapping
    public ResponseEntity<List<NameableDto>> handleGetAll() {
        return ResponseEntity.ok(
                mainPowerConnectorService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех основных разъемов питания (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<NameableDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                mainPowerConnectorService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение основного разъема питания по ID")
    @GetMapping("/{id}")
    public ResponseEntity<NameableDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(mainPowerConnectorService.getById(id))
        );
    }

    @Operation(summary = "Создание основного разъема питания")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<NameableDto> handleCreate(
            @RequestBody final NameableRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final NameableDto savedDto = mapper.toDto(
                mainPowerConnectorService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_MAIN_POWER_CONNECTORS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение основного разъема питания")
    @PatchMapping("/{id}")
    public ResponseEntity<NameableDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        mainPowerConnectorService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена основного разъема питания")
    @PutMapping("/{id}")
    public ResponseEntity<NameableDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        mainPowerConnectorService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление основного разъема питания по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        mainPowerConnectorService.delete(id);
    }
}
