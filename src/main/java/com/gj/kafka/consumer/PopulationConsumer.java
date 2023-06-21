package com.gj.kafka.consumer;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.CityAggregation;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.ArrayList;
import java.util.List;

public class PopulationConsumer {

    public static List<CityAggregation> consumeData(String grpName, String brokers, String topic) {
        Consumer<String, CityAggregation> consumer = ConsumerCreator.createCityPopulationConsumer(grpName, brokers, topic);
       List<CityAggregation> list=new ArrayList<>();
        int noMessageToFetch = 0;

        while (true) {
            final ConsumerRecords<String, CityAggregation> consumerRecords = consumer.poll(1000);
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

}
