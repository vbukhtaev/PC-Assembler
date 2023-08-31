package ru.bukhtaev.pcassembler.controller.rest;

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
import ru.bukhtaev.pcassembler.dto.request.CoolerRequestDto;
import ru.bukhtaev.pcassembler.dto.response.CoolerDto;
import ru.bukhtaev.pcassembler.dto.mapper.CoolerMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.model.Cooler;
import ru.bukhtaev.pcassembler.service.CoolerService;
import ru.bukhtaev.pcassembler.util.sort.CoolerSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Кулеры")
@RestController
@RequestMapping(CoolerRestController.API_V1_COOLERS)
public class CoolerRestController {

    public static final String API_V1_COOLERS = "/api/v1/coolers";

    private final CoolerService coolerService;
    private final CoolerMapper mapper;

    @Autowired
    public CoolerRestController(
            final CoolerService coolerService,
            final MapperResolver mapperResolver
    ) {
        this.coolerService = coolerService;
        this.mapper = mapperResolver.resolve(Cooler.class);
    }

    @Operation(summary = "Получение всех кулеров")
    @GetMapping
    public ResponseEntity<List<CoolerDto>> handleGetAll() {
        return ResponseEntity.ok(
                coolerService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех кулеров (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<CoolerDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final CoolerSort sort
    ) {
        return ResponseEntity.ok(
                coolerService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение кулера по ID")
    @GetMapping("/{id}")
    public ResponseEntity<CoolerDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(coolerService.getById(id))
        );
    }

    @Operation(summary = "Создание кулера")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<CoolerDto> handleCreate(
            @RequestBody final CoolerRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final CoolerDto savedDto = mapper.toDto(
                coolerService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_COOLERS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение кулера")
    @PatchMapping("/{id}")
    public ResponseEntity<CoolerDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final CoolerRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        coolerService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена кулера")
    @PutMapping("/{id}")
    public ResponseEntity<CoolerDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final CoolerRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        coolerService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление кулера по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        coolerService.delete(id);
    }
}
