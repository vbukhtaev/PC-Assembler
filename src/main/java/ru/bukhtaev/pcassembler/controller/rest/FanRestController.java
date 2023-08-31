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
import ru.bukhtaev.pcassembler.dto.request.FanRequestDto;
import ru.bukhtaev.pcassembler.dto.response.FanDto;
import ru.bukhtaev.pcassembler.dto.mapper.FanMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.model.Fan;
import ru.bukhtaev.pcassembler.service.FanService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Вентиляторы")
@RestController
@RequestMapping(FanRestController.API_V1_FANS)
public class FanRestController {

    public static final String API_V1_FANS = "/api/v1/fans";

    private final FanService fanService;
    private final FanMapper mapper;

    @Autowired
    public FanRestController(
            final FanService fanService,
            final MapperResolver mapperResolver
    ) {
        this.fanService = fanService;
        this.mapper = mapperResolver.resolve(Fan.class);
    }

    @Operation(summary = "Получение всех вентиляторов")
    @GetMapping
    public ResponseEntity<List<FanDto>> handleGetAll() {
        return ResponseEntity.ok(
                fanService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех вентиляторов (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<FanDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                fanService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение вентилятора по ID")
    @GetMapping("/{id}")
    public ResponseEntity<FanDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(fanService.getById(id))
        );
    }

    @Operation(summary = "Создание вентилятора")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<FanDto> handleCreate(
            @RequestBody final FanRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final FanDto savedDto = mapper.toDto(
                fanService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_FANS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение вентилятора")
    @PatchMapping("/{id}")
    public ResponseEntity<FanDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final FanRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        fanService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена вентилятора")
    @PutMapping("/{id}")
    public ResponseEntity<FanDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final FanRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        fanService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление вентилятора по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        fanService.delete(id);
    }
}
