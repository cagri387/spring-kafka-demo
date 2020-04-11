package com.example.spiringkafkademo;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"com.example"})
@PropertySources({ @PropertySource("classpath:consumer.properties") })
public class ConsumerConfig {

    private final String MAX_POLL_RECORDS = "1";
    private final String ENABLE_AUTO_COMMIT = "false";

    @Value("${kafka.broker.addresses}")
    private String bootstapServers;

    @Value("${spring.application.name}")
    private String groupId;

    @Value("${auto.offset.reset}")
    private String autoOffsetReset;

    @Value("${session.timeout.ms}")
    private String sessionTimeoutMs;

    @Value("${heartbeat.interval.ms}")
    private String heartBeatIntervalMs;

    @Value("${request.timeout.ms}")
    private String requestTimeoutMs;

    @Value("${max.poll.interval.ms}")
    private String maxPollIntervalMs;

    @Bean
    public Properties consumerProperties() {
        Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstapServers);
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, ENABLE_AUTO_COMMIT);
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartBeatIntervalMs);
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);

        return properties;
    }
}
