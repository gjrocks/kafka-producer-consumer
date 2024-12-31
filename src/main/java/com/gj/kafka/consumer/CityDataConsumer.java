package com.gj.kafka.consumer;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.City;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;

public class CityDataConsumer {

    public static List<City> seekDataUsingStartTimeStamp(String grpName,String brokers, String topic, long startTimeStamp, int partition){

        Consumer<String, City> consumer = ConsumerCreator.createConsumerForSeek(grpName, brokers,topic);

        TopicPartition partitionToReadFrom = new TopicPartition(topic, partition);

        consumer.assign(Arrays.asList(partitionToReadFrom));

        // seek
        //consumer.seek(partitionToReadFrom, offset);
        //Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes(Map<TopicPartition, Long> var1);
        Map<TopicPartition, Long> seekTimeStamp=new HashMap<>();
        seekTimeStamp.put(partitionToReadFrom,startTimeStamp);
        Map<TopicPartition, OffsetAndTimestamp> ret= consumer.offsetsForTimes(seekTimeStamp);
        long offset=ret.get(partitionToReadFrom).offset();
        consumer.seek(partitionToReadFrom, offset);

        List<City> list=new ArrayList<>();
        int noMessageToFetch = 0;

        while (true) {
            final ConsumerRecords<String, City> consumerRecords = consumer.poll(1000);
            //consumer.offsetsForTimes()

            if (consumerRecords.count() == 0) {
                noMessageToFetch++;
                if (noMessageToFetch > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
                    break;
                else
                    continue;
            }

            consumerRecords.forEach(record -> {
                list.add(record.value());
                System.out.println("Key :" + record.value().getKey() + " offset :" + record.offset());

            });

            consumer.commitAsync();
        }
        consumer.close();

        return list;

    }

    public static List<City> seekDataUsingStartOffset(String grpName, String brokers, String topic, long offset, int partition){

        Consumer<String, City> consumer = ConsumerCreator.createConsumerForSeek(grpName, brokers,topic);

        TopicPartition partitionToReadFrom = new TopicPartition(topic, partition);
        long offsetToReadFrom = 7L;
        consumer.assign(Arrays.asList(partitionToReadFrom));

        // seek
        consumer.seek(partitionToReadFrom, offset);

        List<City> list=new ArrayList<>();
        int noMessageToFetch = 0;

        while (true) {
            final ConsumerRecords<String, City> consumerRecords = consumer.poll(1000);
            //consumer.offsetsForTimes()

            if (consumerRecords.count() == 0) {
                noMessageToFetch++;
                if (noMessageToFetch > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
                    break;
                else
                    continue;
            }

            consumerRecords.forEach(record -> {
                list.add(record.value());
                System.out.println("Key :" + record.value().getKey() + " offset :" + record.offset());

            });

            consumer.commitAsync();
        }
        consumer.close();

        return list;

    }



    public static List<City> consumeData(String grpName,String brokers, String topic){

            Consumer<String, City> consumer = ConsumerCreator.createConsumer(grpName, brokers,topic);
            List<City> list=new ArrayList<>();
            int noMessageToFetch = 0;

            while (true) {
                final ConsumerRecords<String, City> consumerRecords = consumer.poll(1000);
                //consumer.offsetsForTimes()

                if (consumerRecords.count() == 0) {
                    noMessageToFetch++;
                    if (noMessageToFetch > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
                        break;
                    else
                        continue;
                }

                consumerRecords.forEach(record -> {
                    list.add(record.value());
                    System.out.println("Key :" + record.value().getKey() + " offset :" + record.offset());

                });

                consumer.commitAsync();
            }
            consumer.close();

            return list;

    }

    public static List<City> consumeData(String brokers, String topic, String grpName, java.util.function.Predicate<City> fn){
       List<City> list= consumeData(brokers,topic,grpName);
       if(list!=null && !list.isEmpty()){
         List<City> li= list.stream().filter(fn).collect(Collectors.toList());
         return li;
       }
       return null;
    }
}
