package com.gj.kafka.constants;

public interface IKafkaConstants {
	public static String KAFKA_BROKERS_1 = "localhost:9092";
	public static String KAFKA_BROKERS_2 = "localhost:9093";
	public static Integer MESSAGE_COUNT=1000;
	
	public static String CLIENT_ID="client1";
	
	public static String TOPIC_NAME="city";

	public static String CITIES_TOPIC="city";
	
	public static String GROUP_ID_CONFIG="consumerGroup10";
	
	public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;
	
	public static String OFFSET_RESET_LATEST="latest";
	
	public static String OFFSET_RESET_EARLIER="earliest";
	
	public static Integer MAX_POLL_RECORDS=1;
	Object KAFKA_BROKERS_ALL = "localhost:9092,localhost:9093";
}
