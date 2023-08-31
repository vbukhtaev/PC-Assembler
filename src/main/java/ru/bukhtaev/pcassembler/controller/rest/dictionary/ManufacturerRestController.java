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
import ru.bukhtaev.pcassembler.dto.dictionary.mapper.ManufacturerMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;
import ru.bukhtaev.pcassembler.model.dictionary.Manufacturer;
import ru.bukhtaev.pcassembler.service.dictionary.ManufacturerService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Производители")
@RestController
@RequestMapping(ManufacturerRestController.API_V1_MANUFACTURERS)
public class ManufacturerRestController {

    public static final String API_V1_MANUFACTURERS = "/api/v1/manufacturers";

    private final ManufacturerService manufacturerService;
    private final ManufacturerMapper mapper;

    @Autowired
    public ManufacturerRestController(
            final ManufacturerService manufacturerService,
            final MapperResolver mapperResolver
    ) {
        this.manufacturerService = manufacturerService;
        this.mapper = mapperResolver.resolve(Manufacturer.class);
    }

    @Operation(summary = "Получение всех производителей")
    @GetMapping
    public ResponseEntity<List<NameableDto>> handleGetAll() {
        return ResponseEntity.ok(
                manufacturerService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех производителей (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<NameableDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                manufacturerService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение производителя по ID")
    @GetMapping("/{id}")
    public ResponseEntity<NameableDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(manufacturerService.getById(id))
        );
    }

    @Operation(summary = "Создание производителя")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<NameableDto> handleCreate(
            @RequestBody final NameableRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final NameableDto savedDto = mapper.toDto(
                manufacturerService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_MANUFACTURERS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение производителя")
    @PatchMapping("/{id}")
    public ResponseEntity<NameableDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        manufacturerService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена производителя")
    @PutMapping("/{id}")
    public ResponseEntity<NameableDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        manufacturerService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление производителя по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        manufacturerService.delete(id);
    }
}
