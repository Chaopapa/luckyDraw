package com.le.core.config.format;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author 严秋旺
 * @since 2019-01-29 9:28
 **/
public class LocalTimeFormatter implements Formatter<LocalTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public LocalTime parse(String s, Locale locale) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }

        return LocalTime.parse(s, formatter);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return formatter.format(localTime);
    }
}
