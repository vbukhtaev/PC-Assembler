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
import ru.bukhtaev.pcassembler.dto.response.ChipsetDto;
import ru.bukhtaev.pcassembler.dto.mapper.ChipsetMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.request.ChipsetRequestDto;
import ru.bukhtaev.pcassembler.model.Chipset;
import ru.bukhtaev.pcassembler.service.ChipsetService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Чипсеты")
@RestController
@RequestMapping(ChipsetRestController.API_V1_CHIPSETS)
public class ChipsetRestController {

    public static final String API_V1_CHIPSETS = "/api/v1/chipsets";

    private final ChipsetService chipsetService;
    private final ChipsetMapper mapper;

    @Autowired
    public ChipsetRestController(
            final ChipsetService chipsetService,
            final MapperResolver mapperResolver
    ) {
        this.chipsetService = chipsetService;
        this.mapper = mapperResolver.resolve(Chipset.class);
    }

    @Operation(summary = "Получение всех чипсетов")
    @GetMapping
    public ResponseEntity<List<ChipsetDto>> handleGetAll() {
        return ResponseEntity.ok(
                chipsetService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех чипсетов (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<ChipsetDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                chipsetService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение чипсета по ID")
    @GetMapping("/{id}")
    public ResponseEntity<ChipsetDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(chipsetService.getById(id))
        );
    }

    @Operation(summary = "Создание чипсета")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ChipsetDto> handleCreate(
            @RequestBody final ChipsetRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final ChipsetDto savedDto = mapper.toDto(
                chipsetService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_CHIPSETS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение чипсета")
    @PatchMapping("/{id}")
    public ResponseEntity<ChipsetDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final ChipsetRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        chipsetService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена чипсета")
    @PutMapping("/{id}")
    public ResponseEntity<ChipsetDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final ChipsetRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        chipsetService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление чипсета по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        chipsetService.delete(id);
    }
}
