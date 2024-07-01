package com.gj.kafka.producer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.gj.kafka.App;
import com.gj.kafka.model.RSSI;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RSSIProducer {

    static int delay=1000;
    public static List<RecordMetadata> produce(String broker, String topic, List<RSSI> list ) {
        System.out.println("City Producers");
        //Producer<String, City> producer = ProducerCreator.createProducerSSL_SASL(broker);
       Producer<String, RSSI> producer = RSSIProducerCreator.createProducer(broker);
        List<RecordMetadata> listRecordMetadata=new ArrayList<>();
        for (RSSI rssi:list) {
            String key="STATIC";
            final ProducerRecord<String, RSSI> record = new ProducerRecord<String, RSSI>(topic,key,rssi);
            try {
                RecordMetadata metadata = producer.send(record).get();
                if(delay>0){
                    try{
                        Thread.sleep(App.getRandomNumber(1000,3000));
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



}
