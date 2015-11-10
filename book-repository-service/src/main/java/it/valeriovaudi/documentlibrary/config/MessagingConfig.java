package it.valeriovaudi.documentlibrary.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

/**
 * Created by Valerio on 04/05/2015.
 */

@Configuration
@EnableConfigurationProperties(ActiveMQProperties.class)
public class MessagingConfig {

    public static final String CREATE_BOOK_DESTINATION_NAME             = "createBookQueue";
    public static final String CREATE_BOOK_RESULT_DESTINATION_NAME      = "createBookResultQueue";

    @Bean
    public ConnectionFactory jmsConnectionFactory(ActiveMQProperties properties){
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(properties.getBrokerUrl());
        activeMQConnectionFactory.setUserName(properties.getUser());
        activeMQConnectionFactory.setPassword(properties.getPassword());

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setSessionCacheSize(10);
        cachingConnectionFactory.setCacheConsumers(false);
        cachingConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);

        return cachingConnectionFactory;
    }

    @Bean
    public Destination createBookQueue() {
        return new ActiveMQQueue(CREATE_BOOK_DESTINATION_NAME);
    }

    @Bean
    public Destination createBookResultQueue() {
        return new ActiveMQQueue(CREATE_BOOK_RESULT_DESTINATION_NAME);
    }
}
