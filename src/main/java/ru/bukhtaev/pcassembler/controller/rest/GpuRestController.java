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
import ru.bukhtaev.pcassembler.dto.request.GpuRequestDto;
import ru.bukhtaev.pcassembler.dto.response.GpuDto;
import ru.bukhtaev.pcassembler.dto.mapper.GpuMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.model.Gpu;
import ru.bukhtaev.pcassembler.service.GpuService;
import ru.bukhtaev.pcassembler.util.sort.GpuSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Графические процессоры")
@RestController
@RequestMapping(GpuRestController.API_V1_GPUS)
public class GpuRestController {

    public static final String API_V1_GPUS = "/api/v1/gpus";

    private final GpuService gpuService;
    private final GpuMapper mapper;

    @Autowired
    public GpuRestController(
            final GpuService gpuService,
            final MapperResolver mapperResolver
    ) {
        this.gpuService = gpuService;
        this.mapper = mapperResolver.resolve(Gpu.class);
    }

    @Operation(summary = "Получение всех графических процессоров")
    @GetMapping
    public ResponseEntity<List<GpuDto>> handleGetAll() {
        return ResponseEntity.ok(
                gpuService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех графических процессоров (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<GpuDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final GpuSort sort
    ) {
        return ResponseEntity.ok(
                gpuService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение графического процессора по ID")
    @GetMapping("/{id}")
    public ResponseEntity<GpuDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(gpuService.getById(id))
        );
    }

    @Operation(summary = "Создание графического процессора")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<GpuDto> handleCreate(
            @RequestBody final GpuRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final GpuDto savedDto = mapper.toDto(
                gpuService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_GPUS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение графического процессора")
    @PatchMapping("/{id}")
    public ResponseEntity<GpuDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final GpuRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        gpuService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена графического процессора")
    @PutMapping("/{id}")
    public ResponseEntity<GpuDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final GpuRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        gpuService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление графического процессора по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        gpuService.delete(id);
    }
}
