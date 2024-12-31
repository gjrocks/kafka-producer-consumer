package com.gj.kafka.cert.producers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.City;
import com.gj.kafka.producer.ProducerCreator;
import com.gj.kafka.serializer.CitySerializer;
import com.gj.kafka.serializer.JsonSerializer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class TestProducer {
        static int delay=1000;
        public static List<RecordMetadata> produce(String broker, String topic, List< City > list ) {
        System.out.println("City Producers");
        //Producer<String, City> producer = ProducerCreator.createProducerSSL_SASL(broker);
        Producer<String, EventData> producer = createProducer(broker);
        List<RecordMetadata> listRecordMetadata=new ArrayList<>();
        for (City city:list) {
            String key=city.getKey();

            EventData data=getEventData(key,city);
            final ProducerRecord<String, EventData> record = new ProducerRecord<String, EventData>(topic,key,data);
            try {
                RecordMetadata metadata = producer.send(record).get();
                if(delay>0){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){e.printStackTrace();}
                }
                System.out.println("Record sent with key " + key + " to partition " + metadata.partition()
                        + " with offset " + metadata.offset());
                listRecordMetadata.add(metadata);
            } catch (ExecutionException e) {
                System.out.println("Error in sending record");
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Error in sending record");
                e.printStackTrace();
            }
        }
        return listRecordMetadata;
    }

    private static Producer<String, EventData> createProducer(String broker) {



            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
            props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
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

    private static EventData getEventData(String key, City city) {
            long timestamp =getTimestamp();
            HashMap<String,String> map=new HashMap<>();
            map.put("EventTime",timestamp+"");
            EventData data=new EventData(map,new CitySerializer().serialize("",city));
            return data;
    }


    public static List<City> loadData() {

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<City> asList = mapper.readValue(new File("C:\\kafka\\java-examples\\kafka\\kafka-producer-consumer\\src\\main\\resources\\city.json"), new TypeReference<List<City>>() {
            });
            System.out.println(asList.size());
            return asList;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    //create a method to provide timestamp
    public static long getTimestamp(){
        return System.currentTimeMillis();
    }
    //create a method to provide random number
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
