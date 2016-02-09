package it.valeriovaudi.documentlibrary.config;


import it.valeriovaudi.documentlibrary.converter.LocalDateTimeToStringConverter;
import it.valeriovaudi.documentlibrary.converter.StringToLocalDateTimeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Arrays;

/**
 * Created by Valerio on 06/06/2015.
 */
@Configuration
public class MongoConfig {

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public CustomConversions customConversions() {
        return new CustomConversions(Arrays.asList(new StringToLocalDateTimeConverter(),
                                                   new LocalDateTimeToStringConverter()));
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}

