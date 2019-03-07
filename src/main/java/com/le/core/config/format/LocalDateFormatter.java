package com.le.core.config.format;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author 严秋旺
 * @since 2019-01-29 9:28
 **/
public class LocalDateFormatter implements Formatter<LocalDate> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate parse(String s, Locale locale) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }

        return LocalDate.parse(s, formatter);
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return formatter.format(localDate);
    }
}
