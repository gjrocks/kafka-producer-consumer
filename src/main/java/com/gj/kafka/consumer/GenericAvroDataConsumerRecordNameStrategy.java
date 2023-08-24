package com.gj.kafka.consumer;

import com.gj.kafka.constants.IKafkaConstants;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenericAvroDataConsumerRecordNameStrategy {

    public static List<GenericRecord> consumeData(String grpName,String brokers, String topic){

        System.out.println("Called GenericAvroDataConsumerRecordNameStrategy.consumeData");
            Consumer<String, GenericRecord> consumer = ConsumerCreator.createGenericDataConsumerRecordNameStrategy(grpName, brokers,topic);
            List<GenericRecord> list=new ArrayList<>();
            int noMessageToFetch = 0;

            while (true) {
                final ConsumerRecords<String, GenericRecord> consumerRecords = consumer.poll(1000);
                if (consumerRecords.count() == 0) {
                    noMessageToFetch++;
                    if (noMessageToFetch > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
                        break;
                    else
                        continue;
                }

                consumerRecords.forEach(record -> {

                    if(record!=null)
                    list.add(record.value());
                    else
                        System.out.println("Record is null in consumer");
                });

                consumer.commitAsync();
            }
            consumer.close();

            return list;

    }

    public static List<GenericRecord> consumeData(String brokers, String topic, String grpName, java.util.function.Predicate<GenericRecord> fn){
       List<GenericRecord> list= consumeData(brokers,topic,grpName);
       if(list!=null && !list.isEmpty()){
         List<GenericRecord> li= list.stream().filter(fn).collect(Collectors.toList());
         return li;
       }
       return null;
    }
}
