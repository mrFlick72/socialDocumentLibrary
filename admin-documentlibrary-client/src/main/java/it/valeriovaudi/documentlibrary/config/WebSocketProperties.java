package it.valeriovaudi.documentlibrary.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Valerio on 12/03/2016.
 */

@Data
@ConfigurationProperties(prefix = "spring.websocket")
public class WebSocketProperties {

    private String relayHost;

    private String clientLogin;
    private String clientPasscode;

    private String systemLogin;
    private String systemPasscode;

}
