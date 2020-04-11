package com.example.spiringkafkademo;

import com.example.spiringkafkademo.entity.Flight;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.MessageListener;

import java.util.concurrent.CountDownLatch;

public class FlightProcessor implements MessageListener<Integer, Flight> {

	private Logger logger = LoggerFactory.getLogger(FlightProcessor.class);

	private int processed_number;

	private final CountDownLatch latch;

	public FlightProcessor(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void onMessage(ConsumerRecord<Integer, Flight> data) {
		logger.info("Test Flight message consumed: " + data.value());
		latch.countDown();
	}

	public int getProcessed_number() {
		return processed_number;
	}

	public void setProcessed_number(int processed_number) {
		this.processed_number = processed_number;
	}

}
