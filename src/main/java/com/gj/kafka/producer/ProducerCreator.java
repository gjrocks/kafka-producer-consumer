package com.gj.kafka.producer;

import java.util.Properties;

import com.gj.kafka.model.City;
import com.gj.kafka.model.Employee;
import com.gj.kafka.serializer.CitySerializer;
import com.gj.kafka.util.Util;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.gj.kafka.constants.IKafkaConstants;

public class ProducerCreator {

	public static Producer<String, City> createProducer(String broker) {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CitySerializer.class.getName());
		return new KafkaProducer<>(props);
	}
	public static Producer<String, Employee> createEmployeeProducer(String broker) {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
		props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG,"http://localhost:8081");
		return new KafkaProducer<>(props);
	}

}
