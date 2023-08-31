package ru.bukhtaev.pcassembler.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import ru.bukhtaev.pcassembler.audit.AuditorAwareImpl;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

    @Bean
    protected AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}
