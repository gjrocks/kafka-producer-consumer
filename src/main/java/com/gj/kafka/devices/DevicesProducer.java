package com.gj.kafka.devices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.City;
import com.gj.kafka.producer.ProducerCreator;
import com.gj.kafka.serializer.CitySerializer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class DevicesProducer {

    static int delay=0;
    public static void runProducer(String broker, String topic) throws Exception{

    List<String> events=DataUtils.readDeviceJsonData();
        System.out.println("Size  Of events :" + events.size());
    produce(broker,topic,events);
    }

    public static List<RecordMetadata> produce(String broker, String topic, List<String> list ) {
        System.out.println("City Producers");
        //Producer<String, City> producer = ProducerCreator.createProducerSSL_SASL(broker);
        Producer<String, String> producer = createProducer(broker);
        // Producer<String, City> producer = ProducerCreator.createProducerSSL_SASLOAUTH2(broker);
        List<RecordMetadata> listRecordMetadata=new ArrayList<>();
        for (String device:list) {

            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode map = mapper.readValue(device, JsonNode.class);
                String key=map.get("deviceName").asText();;
                 final ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic,key,device);

                RecordMetadata metadata = producer.send(record).get();
                if(delay>0){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){e.printStackTrace();}
                }
                System.out.println("Record sent with key " + key + " to partition " + metadata.partition()
                        + " with offset " + metadata.offset() + " With timestamp " + metadata.timestamp() );
                listRecordMetadata.add(metadata);
            } catch (ExecutionException e) {
                System.out.println("Error in sending record");
                e.printStackTrace();
            } catch (InterruptedException | IOException e) {
                System.out.println("Error in sending record");
                e.printStackTrace();
            }
        }
        return listRecordMetadata;
    }

    public static Producer<String, String> createProducer(String broker) {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
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
}
