package com.gj.kafka.producer;

import java.util.Properties;

import com.gj.kafka.model.City;
import com.gj.kafka.serializer.CitySerializer;
import com.gj.kafka.util.Util;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
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
		//props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");
		//props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
		props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"admin\";");
		props.put("ssl.truststore.location","C:\\kafka\\compose\\kraft-kafka-dev-devl\\secrets\\kafka.producer.truststore.jks");
		props.put("ssl.truststore.password","confluent");
		props.put("ssl.keystore.location","C:\\kafka\\compose\\kraft-kafka-dev-devl\\secrets\\kafka.producer.keystore.jks");
		props.put("ssl.keystore.password","confluent");
		props.put("ssl.key.password","confluent");
		props.put("ssl.endpoint.identification.algorithm"," ");
		props.put("producer.ssl.endpoint.identification.algorithm"," ");
		//props.put("security.protocol","SSL");

		/*=confluent

		ssl.keystore.location=/etc/kafka/secrets/kafka.producer.keystore.jks
		ssl.keystore.password=confluent

		ssl.key.password=confluent
		ssl.endpoint.identification.algorithm=
;
				security.protocol=SSL*/
		return new KafkaProducer<>(props);
	}

	public static Producer<String, City> createProducerSSL_SASL(String broker) {
//Serdes.
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CitySerializer.class.getName());
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
		props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
		props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"admin\";");
		props.put("ssl.truststore.location","C:\\kafka\\compose\\kraft-kafka-dev-devl\\secrets\\kafka.producer.truststore.jks");
		props.put("ssl.truststore.password","confluent");
		props.put("ssl.keystore.location","C:\\kafka\\compose\\kraft-kafka-dev-devl\\secrets\\kafka.producer.keystore.jks");
		props.put("ssl.keystore.password","confluent");
		props.put("ssl.key.password","confluent");
		props.put("ssl.endpoint.identification.algorithm"," ");
		props.put("producer.ssl.endpoint.identification.algorithm"," ");
		//props.put("security.protocol","SSL");

		/*=confluent

		ssl.keystore.location=/etc/kafka/secrets/kafka.producer.keystore.jks
		ssl.keystore.password=confluent

		ssl.key.password=confluent
		ssl.endpoint.identification.algorithm=
;
				security.protocol=SSL*/


		return new KafkaProducer<>(props);
	}

	public static Producer<String, City> createProducerSSL_SASLOAUTH2(String broker) {
//Serdes.
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CitySerializer.class.getName());
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
		props.put(SaslConfigs.SASL_MECHANISM, "OAUTHBEARER");
		props.put("sasl.jaas.config", "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required LoginStringClaim_sub=\"0oa24ghi00k4op6sp0h8\";");
		props.put("ssl.truststore.location","C:\\kafka\\compose\\kraft-kafka-dev-devl\\secrets\\kafka.producer.truststore.jks");
		props.put("ssl.truststore.password","confluent");
		props.put("ssl.keystore.location","C:\\kafka\\compose\\kraft-kafka-dev-devl\\secrets\\kafka.producer.keystore.jks");
		props.put("ssl.keystore.password","confluent");
		props.put("ssl.key.password","confluent");
		props.put("ssl.endpoint.identification.algorithm"," ");
		props.put("producer.ssl.endpoint.identification.algorithm"," ");
		props.put("sasl.oauthbearer.token.endpoint.url","https://sso-dev.johndeere.com/oauth2/ausi7trcalVAIeyFF0h7/v1/token");
		props.put("sasl.oauthbearer.sub.claim.name","0oa24ghi00k4op6sp0h8");
		props.put("sasl.login.callback.handler.class","com.gj.vishwaway.oauthbearer.OAuthAuthenticateLoginCallbackHandler");
		props.put("enable.idempotence" , "false");
		//props.put("listener.name.sasl.oauthbearer.sasl.server.callback.handler.class","com.gj.vishwaway.oauthbearer.OAuthAuthenticateValidatorCallbackHandler");
		//props.put("security.protocol","SSL");

		/*=confluent

		ssl.keystore.location=/etc/kafka/secrets/kafka.producer.keystore.jks
		ssl.keystore.password=confluent

		ssl.key.password=confluent
		ssl.endpoint.identification.algorithm=
;
				security.protocol=SSL*/


		return new KafkaProducer<>(props);
	}

}
