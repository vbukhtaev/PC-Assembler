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
import ru.bukhtaev.pcassembler.dto.request.CpuRequestDto;
import ru.bukhtaev.pcassembler.dto.response.CpuDto;
import ru.bukhtaev.pcassembler.dto.mapper.CpuMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.model.Cpu;
import ru.bukhtaev.pcassembler.service.CpuService;
import ru.bukhtaev.pcassembler.util.sort.CpuSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Процессоры")
@RestController
@RequestMapping(CpuRestController.API_V1_CPUS)
public class CpuRestController {

    public static final String API_V1_CPUS = "/api/v1/cpus";

    private final CpuService cpuService;
    private final CpuMapper mapper;

    @Autowired
    public CpuRestController(
            final CpuService cpuService,
            final MapperResolver mapperResolver
    ) {
        this.cpuService = cpuService;
        this.mapper = mapperResolver.resolve(Cpu.class);
    }

    @Operation(summary = "Получение всех процессоров")
    @GetMapping
    public ResponseEntity<List<CpuDto>> handleGetAll() {
        return ResponseEntity.ok(
                cpuService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех процессоров (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<CpuDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final CpuSort sort
    ) {
        return ResponseEntity.ok(
                cpuService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение процессора по ID")
    @GetMapping("/{id}")
    public ResponseEntity<CpuDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(cpuService.getById(id))
        );
    }

    @Operation(summary = "Создание процессора")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<CpuDto> handleCreate(
            @RequestBody final CpuRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final CpuDto savedDto = mapper.toDto(
                cpuService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_CPUS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение процессора")
    @PatchMapping("/{id}")
    public ResponseEntity<CpuDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final CpuRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        cpuService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена процессора")
    @PutMapping("/{id}")
    public ResponseEntity<CpuDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final CpuRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        cpuService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление процессора по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        cpuService.delete(id);
    }
}
