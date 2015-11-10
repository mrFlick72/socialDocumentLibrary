package it.valeriovaudi.documentlibrary.config;

import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UrlPathHelper;

/**
 * Created by Valerio on 08/06/2015.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

 /*   @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLocalDateTime());
        registry.addConverter(new LocalDateTimeToString());
        registry.addConverter(new StringToLocalDate());
        registry.addConverter(new LocalDateToString());
        registry.addConverter(new StringToLocalTime());
        registry.addConverter(new LocalTimeToString());
    }
*/

    @Bean
    public JSR310Module jsr310Module() {
        return new JSR310Module();
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }
}

