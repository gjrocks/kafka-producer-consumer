package com.gj.kafka.consumer;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.Employee;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDataConsumer {

    public static List<Employee> consumeData(String grpName,String brokers, String topic){

            Consumer<String, Employee> consumer = ConsumerCreator.createEmployeeConsumer(grpName, brokers,topic);
            List<Employee> list=new ArrayList<>();
            int noMessageToFetch = 0;

            while (true) {
                final ConsumerRecords<String, Employee> consumerRecords = consumer.poll(1000);
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

    public static List<Employee> consumeData(String brokers, String topic, String grpName, java.util.function.Predicate<Employee> fn){
       List<Employee> list= consumeData(brokers,topic,grpName);
       if(list!=null && !list.isEmpty()){
         List<Employee> li= list.stream().filter(fn).collect(Collectors.toList());
         return li;
       }
       return null;
    }
}
