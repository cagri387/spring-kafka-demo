package com.example.spiringkafkademo;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"com.example"})
@PropertySources({ @PropertySource("classpath:producer.properties") })
public class ProducerConfig {

    @Value("${kafka.broker.addresses}")
    private String bootstapServers;

    @Value("${acks}")
    private String acks;

    @Value("${enable.idempotence}")
    private String enableIdempotence;

    @Value("${max.request.size}")
    private String maxRequestSize;

    @Value("${linger.ms}")
    private String lingerMs;

    @Value("${request.timeout.ms}")
    private String requestTimeoutMs;

    @Value("${batch.size}")
    private String batchSize;

    @Bean
    public Properties producerProperties() {
        Properties properties = new Properties();
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstapServers);
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, acks);
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence);
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxRequestSize);
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG, lingerMs);

        return properties;
    }
}
