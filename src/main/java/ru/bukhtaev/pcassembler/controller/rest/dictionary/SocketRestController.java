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
import ru.bukhtaev.pcassembler.dto.dictionary.mapper.SocketMapper;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;
import ru.bukhtaev.pcassembler.model.dictionary.Socket;
import ru.bukhtaev.pcassembler.service.dictionary.SocketService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Сокеты")
@RestController
@RequestMapping(SocketRestController.API_V1_SOCKETS)
public class SocketRestController {

    public static final String API_V1_SOCKETS = "/api/v1/sockets";

    private final SocketService socketService;
    private final SocketMapper mapper;

    @Autowired
    public SocketRestController(
            final SocketService socketService,
            final MapperResolver mapperResolver
    ) {
        this.socketService = socketService;
        this.mapper = mapperResolver.resolve(Socket.class);
    }

    @Operation(summary = "Получение всех сокетов")
    @GetMapping
    public ResponseEntity<List<NameableDto>> handleGetAll() {
        return ResponseEntity.ok(
                socketService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех сокетов (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<NameableDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                socketService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение сокетов по ID")
    @GetMapping("/{id}")
    public ResponseEntity<NameableDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(socketService.getById(id))
        );
    }

    @Operation(summary = "Создание сокета")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<NameableDto> handleCreate(
            @RequestBody final NameableRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final NameableDto savedDto = mapper.toDto(
                socketService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_SOCKETS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение сокета")
    @PatchMapping("/{id}")
    public ResponseEntity<NameableDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        socketService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена сокета")
    @PutMapping("/{id}")
    public ResponseEntity<NameableDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        socketService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление сокета по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        socketService.delete(id);
    }
}
