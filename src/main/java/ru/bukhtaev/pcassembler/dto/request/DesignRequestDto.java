package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;

@Schema(description = "Вариант исполнения")
@Getter
public class DesignRequestDto extends NameableRequestDto {

    @UUID
    @NotBlank
    protected String vendorId;

    @Builder
    public DesignRequestDto(final String name, final String vendorId) {
        super(name);
        this.vendorId = vendorId;
    }
}

