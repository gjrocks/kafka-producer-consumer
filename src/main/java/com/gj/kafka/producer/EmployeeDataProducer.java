package com.gj.kafka.producer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.model.City;
import com.gj.kafka.model.Employee;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmployeeDataProducer {

    static int delay=1000;
    public static List<RecordMetadata> produce(String broker, String topic, List<Employee> list ) {
        Producer<String, Employee> producer = ProducerCreator.createEmployeeProducer(broker);
        List<RecordMetadata> listRecordMetadata=new ArrayList<>();
        System.out.println("Ganesh here");
        for (Employee employee:list) {
            String key=String.valueOf(employee.getFName());
            System.out.println("Schema name :" +  employee.getSchema().getName());
            System.out.println("Ganesh here-2");
            final ProducerRecord<String, Employee> record = new ProducerRecord<String, Employee>(topic,key,employee);
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
                System.out.println(e);
            } catch (InterruptedException e) {
                System.out.println("Error in sending record");
                System.out.println(e);
            }finally {
                producer.close();
                producer.flush();
            }
        }
        return listRecordMetadata;
    }


    public static List<City> loadData() {

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<City> asList = mapper.readValue(new File("C:\\kafka\\java-examples\\kafka\\kafka-producer-consumer\\src\\main\\resources\\cityTemp.json"), new TypeReference<List<City>>() {
            });
            System.out.println(asList.size());
            return asList;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
}
