package ru.bukhtaev.pcassembler.audit;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public @NonNull Optional<String> getCurrentAuditor() {
        return Optional.of("Vladislav Bukhtaev");
    }
}
