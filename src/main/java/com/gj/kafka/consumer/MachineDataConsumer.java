package com.gj.kafka.consumer;

import com.gj.kafka.model.MachineData;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

public class MachineDataConsumer {
    //create apache kafka consumer for the topic machinedata from broker at localhost:9092
    //create a consumer record iterator for the consumer
    public static void readDataFromMachineDataTopic() {
        //create apache kafka consumer for the topic machinedata from broker at localhost:9092
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "com.gj.kafka.consumer.MachineDataDeserializer");
        properties.put("group.id", "machine-data-consumer-group");
        properties.put("auto.offset.reset", "earliest");
        properties.put("enable.auto.commit", "false");
        final MachineDataConsumer consumer = new MachineDataConsumer();
        KafkaConsumer<String, MachineData> machineDataKafkaConsumer = new KafkaConsumer<String, MachineData>(properties);
        machineDataKafkaConsumer.subscribe(Collections.singletonList("machinedata"));
        int count = 0;
        while (true) {
            if (count == 100)
                break;
            ConsumerRecords<String, MachineData> records = machineDataKafkaConsumer.poll(1000);

            System.out.println("count is " + count);
            if (records.count() > 0) {
                count = count + records.count();
                records.forEach(record -> {
                    System.out.println(record.value());
                });
            }

        }

    }
}




