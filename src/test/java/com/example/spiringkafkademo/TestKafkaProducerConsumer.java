package com.example.spiringkafkademo;

import com.example.spiringkafkademo.entity.Flight;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ContextConfiguration(classes = {ConsumerConfig.class, ProducerConfig.class})
@SpringBootTest
public class TestKafkaProducerConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Properties consumerProperties;

    @Autowired
    private Properties producerProperties;

    private final int MESSAGE_SIZE = 100;

    @Test
    public void testConsumer() throws Exception {
        String topicName= "SpringKafkaTestTopic";
        int numberOfPartition = 10;
        logger.info("Start auto");
        ContainerProperties containerProps = new ContainerProperties(topicName);
        final CountDownLatch latch = new CountDownLatch(MESSAGE_SIZE);
        containerProps.setMessageListener(new FlightProcessor(latch));

        KafkaMessageListenerContainer<Integer, String> container = createContainer(containerProps);
        container.setBeanName("testAuto");
        container.start();
        Thread.sleep(1000); // wait a bit for the container to start
        kafkaProducer(latch, topicName, numberOfPartition);
        container.stop();
        logger.info("Stop auto");
    }


    private void kafkaProducer(CountDownLatch latch, String topicName, int numberOfPartitions) throws InterruptedException, JsonProcessingException {
        final String EVENT_NAME = "eventName";
        final String SOURCE_MICROSERVICE = "sourceMicroservice";
        final String ID = "id";
        final String TIMESTAMP = "timestamp";
        IdGenerator defaultIdGenerator = new AlternativeJdkIdGenerator();
        KafkaTemplate<Integer, String> template = createTemplate();

        for(int i = 0; i < MESSAGE_SIZE; i++) {
            Flight flight = new Flight();
            flight.setLatitude(100);
            flight.setLongitude(90);
            flight.setNumber("number" + i + LocalDateTime.now().toString());

            String value = new ObjectMapper().writeValueAsString(flight);

            ProducerRecord producerRecord = new ProducerRecord(topicName, new Random().nextInt(numberOfPartitions), i % numberOfPartitions, value);
            producerRecord.headers().add(new RecordHeader(SOURCE_MICROSERVICE, "microserviceA".getBytes()));
            producerRecord.headers().add(new RecordHeader(EVENT_NAME, "Test".getBytes()));
            producerRecord.headers().add(new RecordHeader(ID, defaultIdGenerator.generateId().toString().getBytes()));
            producerRecord.headers().add(new RecordHeader(TIMESTAMP, Long.valueOf(System.currentTimeMillis()).toString().getBytes()));

            template.send(producerRecord);
        }

        Assertions.assertTrue(latch.await(60, TimeUnit.SECONDS));
    }

    private KafkaMessageListenerContainer<Integer, String> createContainer(ContainerProperties containerProps) {
        DefaultKafkaConsumerFactory<Integer, String> cf =
                new DefaultKafkaConsumerFactory<Integer, String>(new HashMap(consumerProperties));
        KafkaMessageListenerContainer<Integer, String> container =
                new KafkaMessageListenerContainer<>(cf, containerProps);
        return container;
    }

    private KafkaTemplate<Integer, String> createTemplate() {
        ProducerFactory<Integer, String> pf =
                new DefaultKafkaProducerFactory<>(new HashMap(producerProperties));
        KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
        return template;
    }
}
