package ru.bukhtaev.pcassembler.internationalization;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

public class CustomLocaleResolver extends AcceptHeaderLocaleResolver {

    private static final List<Locale> LOCALES = List.of(
            new Locale("en"),
            new Locale("ru")
    );

    @Override
    public @NonNull Locale resolveLocale(HttpServletRequest request) {
        final String headerLang = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return headerLang == null || headerLang.isEmpty()
                ? Locale.getDefault()
                : Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
    }
}
