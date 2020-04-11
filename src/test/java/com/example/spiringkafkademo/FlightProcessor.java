package com.example.spiringkafkademo;

import com.example.spiringkafkademo.entity.Flight;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
public class FlightProcessor implements MessageListener<Integer, Flight> {

	private Logger logger = LoggerFactory.getLogger(FlightProcessor.class);

	private final CountDownLatch latch;

	@Override
	public void onMessage(ConsumerRecord<Integer, Flight> data) {
		logger.info("Test Flight message consumed: " + data.value());
		latch.countDown();
	}
}
