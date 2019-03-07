package com.le.core.config.format;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.Formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author 严秋旺
 * @since 2019-01-29 9:28
 **/
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime parse(String s, Locale locale) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }

        return LocalDateTime.parse(s, formatter);
    }

    @Override
    public String print(LocalDateTime localDateTime, Locale locale) {
        return formatter.format(localDateTime);
    }
}
