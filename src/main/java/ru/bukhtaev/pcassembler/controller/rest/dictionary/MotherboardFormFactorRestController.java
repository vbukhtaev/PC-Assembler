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
import ru.bukhtaev.pcassembler.dto.dictionary.mapper.MotherboardFormFactorMapper;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;
import ru.bukhtaev.pcassembler.model.dictionary.MotherboardFormFactor;
import ru.bukhtaev.pcassembler.service.dictionary.MotherboardFormFactorService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Форм-факторы материнских плат")
@RestController
@RequestMapping(MotherboardFormFactorRestController.API_V1_MOTHERBOARD_FORM_FACTORS)
public class MotherboardFormFactorRestController {

    public static final String API_V1_MOTHERBOARD_FORM_FACTORS = "/api/v1/motherboard-form-factors";

    private final MotherboardFormFactorService motherboardFormFactorService;
    private final MotherboardFormFactorMapper mapper;

    @Autowired
    public MotherboardFormFactorRestController(
            final MotherboardFormFactorService motherboardFormFactorService,
            final MapperResolver mapperResolver
    ) {
        this.motherboardFormFactorService = motherboardFormFactorService;
        this.mapper = mapperResolver.resolve(MotherboardFormFactor.class);
    }

    @Operation(summary = "Получение всех форм-факторов материнских плат")
    @GetMapping
    public ResponseEntity<List<NameableDto>> handleGetAll() {
        return ResponseEntity.ok(
                motherboardFormFactorService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех форм-факторов материнских плат (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<NameableDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                motherboardFormFactorService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение форм-фактора материнской платы по ID")
    @GetMapping("/{id}")
    public ResponseEntity<NameableDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(motherboardFormFactorService.getById(id))
        );
    }

    @Operation(summary = "Создание форм-фактора материнской платы")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<NameableDto> handleCreate(
            @RequestBody final NameableRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final NameableDto savedDto = mapper.toDto(
                motherboardFormFactorService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_MOTHERBOARD_FORM_FACTORS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение форм-фактора материнской платы")
    @PatchMapping("/{id}")
    public ResponseEntity<NameableDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        motherboardFormFactorService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена форм-фактора материнской платы")
    @PutMapping("/{id}")
    public ResponseEntity<NameableDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        motherboardFormFactorService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление форм-фактора материнской платы по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        motherboardFormFactorService.delete(id);
    }
}
