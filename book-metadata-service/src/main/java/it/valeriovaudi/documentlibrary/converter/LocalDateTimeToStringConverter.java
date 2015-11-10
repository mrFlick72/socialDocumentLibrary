package it.valeriovaudi.documentlibrary.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Valerio on 11/06/2015.
 */
public class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {
    @Override
    public String convert(LocalDateTime source) {
        return DateTimeFormatter.ISO_DATE_TIME.format(source);
    }

};