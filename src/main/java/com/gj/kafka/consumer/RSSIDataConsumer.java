package com.gj.kafka.consumer;

import com.gj.kafka.constants.IKafkaConstants;

import com.gj.kafka.model.RSSI;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RSSIDataConsumer {

    public static List<RSSI> consumeData(String grpName, String brokers, String topic){

            Consumer<String, RSSI> consumer = ConsumerCreator.createRSSIConsumer(grpName, brokers,topic);
            List<RSSI> list=new ArrayList<>();
            int noMessageToFetch = 0;

            while (true) {
                final ConsumerRecords<String, RSSI> consumerRecords = consumer.poll(1000);


                if (consumerRecords.count() == 0) {
                    noMessageToFetch++;
                    if (noMessageToFetch > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
                        break;
                    else
                        continue;
                }

                consumerRecords.forEach(record -> {
                    list.add(record.value());
                });

                consumer.commitAsync();
            }
            consumer.close();

            return list;

    }

    public static List<RSSI> consumeData(String brokers, String topic, String grpName, java.util.function.Predicate<RSSI> fn){
       List<RSSI> list= consumeData(brokers,topic,grpName);
       if(list!=null && !list.isEmpty()){
         List<RSSI> li= list.stream().filter(fn).collect(Collectors.toList());
         return li;
       }
       return null;
    }
}
