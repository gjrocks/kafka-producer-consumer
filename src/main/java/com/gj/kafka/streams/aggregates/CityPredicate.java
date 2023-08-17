package com.gj.kafka.streams.aggregates;

import com.gj.kafka.model.City;
import com.gj.kafka.producer.CityDataProducer;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityPredicate implements org.apache.kafka.streams.kstream.Predicate<String,City> {
    public static Integer topicNames[]={1840034016,1840020491,1840000494};
    public static List<Integer> topics= Arrays.asList(topicNames); // get it from properties file, like comma separated list and spilt
    public String brokers;
    public CityPredicate(String brokers) {
        this.brokers=brokers;
    }

    @Override
    public boolean test(String key, City city) {
        if(topics.contains(city.getId())) {
            List<City> li=new ArrayList<>();
            li.add(city);
            List<RecordMetadata> mi=CityDataProducer.produce(this.brokers,city.getId()+"", li);
            mi.forEach(record->{
                System.out.println("Record sent to :" + record.topic() + "with key :" + city.getKey());
            });
            return true;
        }

        return false;
    }
}
