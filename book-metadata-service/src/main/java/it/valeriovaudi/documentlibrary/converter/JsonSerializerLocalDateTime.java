package it.valeriovaudi.documentlibrary.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by Valerio on 11/06/2015.
 */
public class JsonSerializerLocalDateTime extends JsonSerializer<LocalDateTime> {

    private LocalDateTimeToStringConverter delegateConverter = new LocalDateTimeToStringConverter();

    public void setLocalDateTimeToStringConverter(LocalDateTimeToStringConverter delegateConverter) {
        this.delegateConverter = delegateConverter;
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(delegateConverter.convert(value));
    }
}
