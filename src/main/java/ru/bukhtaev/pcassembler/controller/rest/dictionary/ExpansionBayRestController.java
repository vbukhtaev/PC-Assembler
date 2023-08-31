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
import ru.bukhtaev.pcassembler.dto.dictionary.ExpansionBayDto;
import ru.bukhtaev.pcassembler.dto.dictionary.mapper.ExpansionBayMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.dictionary.ExpansionBayRequestDto;
import ru.bukhtaev.pcassembler.model.dictionary.ExpansionBay;
import ru.bukhtaev.pcassembler.service.dictionary.ExpansionBayService;
import ru.bukhtaev.pcassembler.util.sort.ExpansionBaySort;

import java.util.List;
import java.util.Map;

@Tag(name = "Слоты расширения")
@RestController
@RequestMapping(ExpansionBayRestController.API_V1_EXPANSION_BAYS)
public class ExpansionBayRestController {

    public static final String API_V1_EXPANSION_BAYS = "/api/v1/expansion-bays";

    private final ExpansionBayService expansionBayService;
    private final ExpansionBayMapper mapper;

    @Autowired
    public ExpansionBayRestController(
            final ExpansionBayService expansionBayService,
            final MapperResolver mapperResolver
    ) {
        this.expansionBayService = expansionBayService;
        this.mapper = mapperResolver.resolve(ExpansionBay.class);
    }

    @Operation(summary = "Получение всех слотов расширения")
    @GetMapping
    public ResponseEntity<List<ExpansionBayDto>> handleGetAll() {
        return ResponseEntity.ok(
                expansionBayService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех слотов расширения (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<ExpansionBayDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "SIZE_ASC") final ExpansionBaySort sort
    ) {
        return ResponseEntity.ok(
                expansionBayService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение слота расширения по ID")
    @GetMapping("/{id}")
    public ResponseEntity<ExpansionBayDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(expansionBayService.getById(id))
        );
    }

    @Operation(summary = "Создание слота расширения")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ExpansionBayDto> handleCreate(
            @RequestBody final ExpansionBayRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final ExpansionBayDto savedDto = mapper.toDto(
                expansionBayService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_EXPANSION_BAYS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение слота расширения")
    @PatchMapping("/{id}")
    public ResponseEntity<ExpansionBayDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final ExpansionBayRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        expansionBayService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена слота расширения")
    @PutMapping("/{id}")
    public ResponseEntity<ExpansionBayDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final ExpansionBayRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        expansionBayService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление слота расширения по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        expansionBayService.delete(id);
    }
}
