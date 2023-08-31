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
import ru.bukhtaev.pcassembler.dto.dictionary.mapper.FanPowerConnectorMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;
import ru.bukhtaev.pcassembler.model.dictionary.FanPowerConnector;
import ru.bukhtaev.pcassembler.service.dictionary.FanPowerConnectorService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Разъемы питания вентилятора")
@RestController
@RequestMapping(FanPowerConnectorRestController.API_V1_FAN_POWER_CONNECTORS)
public class FanPowerConnectorRestController {

    public static final String API_V1_FAN_POWER_CONNECTORS = "/api/v1/fan-power-connectors";

    private final FanPowerConnectorService fanPowerConnectorService;
    private final FanPowerConnectorMapper mapper;

    @Autowired
    public FanPowerConnectorRestController(
            final FanPowerConnectorService fanPowerConnectorService,
            final MapperResolver mapperResolver
    ) {
        this.fanPowerConnectorService = fanPowerConnectorService;
        this.mapper = mapperResolver.resolve(FanPowerConnector.class);
    }

    @Operation(summary = "Получение всех разъемов питания вентилятора")
    @GetMapping
    public ResponseEntity<List<NameableDto>> handleGetAll() {
        return ResponseEntity.ok(
                fanPowerConnectorService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех разъемов питания вентилятора (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<NameableDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                fanPowerConnectorService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение разъема питания вентилятора по ID")
    @GetMapping("/{id}")
    public ResponseEntity<NameableDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(fanPowerConnectorService.getById(id))
        );
    }

    @Operation(summary = "Создание разъема питания вентилятора")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<NameableDto> handleCreate(
            @RequestBody final NameableRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final NameableDto savedDto = mapper.toDto(
                fanPowerConnectorService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_FAN_POWER_CONNECTORS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение разъема питания вентилятора")
    @PatchMapping("/{id}")
    public ResponseEntity<NameableDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        fanPowerConnectorService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена разъема питания вентилятора")
    @PutMapping("/{id}")
    public ResponseEntity<NameableDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        fanPowerConnectorService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление разъема питания вентилятора по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        fanPowerConnectorService.delete(id);
    }
}
