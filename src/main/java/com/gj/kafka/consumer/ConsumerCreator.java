package com.gj.kafka.consumer;

import java.util.Collections;
import java.util.Properties;

import com.gj.kafka.deserializer.CityAggregateDeserializer;
import com.gj.kafka.deserializer.CityDeserializer;
import com.gj.kafka.model.City;
import com.gj.kafka.model.CityAggregation;
import com.gj.kafka.model.Employee;
import com.gj.kafka.serializer.CityAggregateSerializer;
import com.gj.kafka.serializer.CitySerializer;
import com.gj.kafka.util.Util;
import com.gj.kafka.constants.IKafkaConstants;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerCreator {

	public static Consumer<String, City> createConsumer(String grpName, String broker,String topic) {
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, grpName);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CityDeserializer.class.getName());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, IKafkaConstants.MAX_POLL_RECORDS);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, IKafkaConstants.OFFSET_RESET_EARLIER);

		final Consumer<String, City> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(topic));
		return consumer;
	}

	public static Consumer<String, Employee> createEmployeeConsumer(String grpName, String broker, String topic) {
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, grpName);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, IKafkaConstants.MAX_POLL_RECORDS);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, IKafkaConstants.OFFSET_RESET_EARLIER);
		props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
		props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG,"http://localhost:8081"); //<----- Run Schema Registry on 8081
		final Consumer<String, Employee> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(topic));
		return consumer;
	}
	public static Consumer<String, CityAggregation> createCityPopulationConsumer(String grpName, String broker, String topic) {
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, grpName);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CityAggregateDeserializer.class.getName());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, IKafkaConstants.MAX_POLL_RECORDS);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, IKafkaConstants.OFFSET_RESET_EARLIER);

		final Consumer<String, CityAggregation> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(topic));
		return consumer;
	}

}
