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
import ru.bukhtaev.pcassembler.dto.dictionary.FanSizeDto;
import ru.bukhtaev.pcassembler.dto.dictionary.mapper.FanSizeMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.dictionary.FanSizeRequestDto;
import ru.bukhtaev.pcassembler.model.dictionary.FanSize;
import ru.bukhtaev.pcassembler.service.dictionary.FanSizeService;
import ru.bukhtaev.pcassembler.util.sort.FanSizeSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Размеры вентиляторов")
@RestController
@RequestMapping(FanSizeRestController.API_V1_FAN_SIZES)
public class FanSizeRestController {

    public static final String API_V1_FAN_SIZES = "/api/v1/fan-sizes";

    private final FanSizeService fanSizeService;
    private final FanSizeMapper mapper;

    @Autowired
    public FanSizeRestController(
            final FanSizeService fanSizeService,
            final MapperResolver mapperResolver
    ) {
        this.fanSizeService = fanSizeService;
        this.mapper = mapperResolver.resolve(FanSize.class);
    }

    @Operation(summary = "Получение всех размеров вентиляторов")
    @GetMapping
    public ResponseEntity<List<FanSizeDto>> handleGetAll() {
        return ResponseEntity.ok(
                fanSizeService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех размеров вентиляторов (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<FanSizeDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "LENGTH_ASC") final FanSizeSort sort
    ) {
        return ResponseEntity.ok(
                fanSizeService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение размера вентилятора по ID")
    @GetMapping("/{id}")
    public ResponseEntity<FanSizeDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(fanSizeService.getById(id))
        );
    }

    @Operation(summary = "Создание размера вентилятора")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<FanSizeDto> handleCreate(
            @RequestBody final FanSizeRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final FanSizeDto savedDto = mapper.toDto(
                fanSizeService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_FAN_SIZES + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение размера вентилятора")
    @PatchMapping("/{id}")
    public ResponseEntity<FanSizeDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final FanSizeRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        fanSizeService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена размера вентилятора")
    @PutMapping("/{id}")
    public ResponseEntity<FanSizeDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final FanSizeRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        fanSizeService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление размера вентилятора по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        fanSizeService.delete(id);
    }
}
