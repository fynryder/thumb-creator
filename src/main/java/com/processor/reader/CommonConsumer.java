package com.processor.reader;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CommonConsumer {

	private CommonConsumer() {

	}
	final static Logger logger = Logger.getLogger(CommonConsumer.class);
	private static CommonConsumer instance = null;
	private KafkaConsumer<String, String> consumer;

	public static CommonConsumer getInstance() {
		if (instance == null) {
			synchronized (CommonConsumer.class) {
				if (instance == null) {
					instance = new CommonConsumer();
				}
			}
		}

		return instance;
	}

	private void initConsumer() {

		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
		properties.setProperty("group.id", "default");
		properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		properties.put("enable.auto.commit", "true");
		properties.put("auto.commit.interval.ms", "1000");
		properties.put("auto.offset.reset", "earliest");
		properties.put("session.timeout.ms", "30000");

		consumer = new KafkaConsumer<String, String>(properties);
		consumer.subscribe(Arrays.asList("image-reader"));

	}

	public void startProcessor() {
		initConsumer();
		startConsumer();
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
	}

	private void startConsumer() {
		long count = 0;
		try {
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(200);
				if (records.count() == 0) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					for (ConsumerRecord<String, String> record : records) {
						count += 1;
						MessageProcessor.getInstance().processMessage(record.value());
						logger.info(count + ": " + record.value() + " " + record.key());
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error while starting consumer",ex);
		}
	}

}
