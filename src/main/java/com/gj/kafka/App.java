package com.gj.kafka;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.model.City;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.consumer.ConsumerCreator;
import com.gj.kafka.producer.ProducerCreator;

public class App {

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
	public static void main(String[] args) {

		String grpName=System.getProperty("groupname");
		String broker=System.getProperty("broker");
		String action=System.getProperty("action");
		System.out.println("GrpName: "+ grpName);
		System.out.println("action: "+ action);
		System.out.println("broker: "+ broker);
		if(action==null || action.trim().isEmpty()){
			System.out.println("Please provide the action possible values are producer/consumer, returning without any processing");
			return;
		}
		if(action.equalsIgnoreCase("producer")) {
			runProducer(broker);
		}
		if(action.equalsIgnoreCase("consumer")) {
			runConsumer(grpName,broker);
		}
		//runConsumer();
		//runConsumer(grpName2);
	}

	static void runConsumer(String grpName,String broker) {
		Consumer<String, City> consumer = ConsumerCreator.createConsumer(grpName, broker);

		int noMessageToFetch = 0;

		while (true) {
			final ConsumerRecords<String, City> consumerRecords = consumer.poll(1000);
			if (consumerRecords.count() == 0) {
				noMessageToFetch++;
				if (noMessageToFetch > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
					break;
				else
					continue;
			}

			consumerRecords.forEach(record -> {
				System.out.println("Record Key " + record.key());
				System.out.println("Record value " + record.value());
				System.out.println("Record partition " + record.partition());
				System.out.println("Record offset " + record.offset());
			});
			consumer.commitAsync();
		}
		consumer.close();
	}

	static void runProducer(String broker) {
		Producer<String, City> producer = ProducerCreator.createProducer(broker);
       List<City> list=loadData();

		for (City city:list )  {
        String key=city.getStateId()+"|"+city.getCity()+"|"+city.getId();
			final ProducerRecord<String, City> record = new ProducerRecord<String, City>(IKafkaConstants.CITIES_TOPIC,key,city);
			try {
				RecordMetadata metadata = producer.send(record).get();
				System.out.println("Record sent with key " + key + " to partition " + metadata.partition()
						+ " with offset " + metadata.offset());
			} catch (ExecutionException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			} catch (InterruptedException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			}
		}
	}
}
