package it.valeriovaudi.documentlibrary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;

/**
 * Created by Valerio on 10/07/2015.
 */

@Configuration
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketConfig extends WebSocketMessageBrokerConfigurationSupport {

    @Autowired
    private WebSocketProperties webSocketProperties;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableStompBrokerRelay("/notify").setRelayHost(webSocketProperties.getRelayHost())
                .setClientLogin(webSocketProperties.getClientLogin()).setClientPasscode(webSocketProperties.getClientPasscode())
        .setSystemLogin(webSocketProperties.getSystemLogin()).setSystemPasscode(webSocketProperties.getSystemPasscode());
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/createBookJob").withSockJS();
    }
}