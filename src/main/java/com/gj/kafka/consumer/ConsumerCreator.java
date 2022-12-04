package com.gj.kafka.consumer;

import java.util.Collections;
import java.util.Properties;

import com.gj.kafka.deserializer.CityDeserializer;
import com.gj.kafka.model.City;
import com.gj.kafka.serializer.CitySerializer;
import com.gj.kafka.util.Util;
import com.gj.kafka.constants.IKafkaConstants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerCreator {

	public static Consumer<String, City> createConsumer(String grpName, String broker) {
		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Util.getBroker(broker));
		props.put(ConsumerConfig.GROUP_ID_CONFIG, (grpName!=null?grpName: IKafkaConstants.GROUP_ID_CONFIG));
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CityDeserializer.class.getName());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, IKafkaConstants.MAX_POLL_RECORDS);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, IKafkaConstants.OFFSET_RESET_EARLIER);

		final Consumer<String, City> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(IKafkaConstants.CITIES_TOPIC));
		return consumer;
	}

}
