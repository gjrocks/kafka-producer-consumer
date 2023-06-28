package com.gj.kafka;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.consumer.CityDataConsumer;
import com.gj.kafka.consumer.ConsumerCreator;
import com.gj.kafka.consumer.PopulationConsumer;
import com.gj.kafka.model.City;
import com.gj.kafka.model.CityAggregation;
import com.gj.kafka.producer.CityDataProducer;
import com.gj.kafka.streams.MovieStream;
import com.gj.kafka.streams.aggregates.Aggregation;
import com.gj.kafka.streams.aggregates.RecordChangesAggregation;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class App {

    public static void main(String[] args) {

        String grpName = System.getProperty("GroupName");
        String broker = System.getProperty("broker");
        String topic = System.getProperty("topic");
        String action = System.getProperty("action");
        System.out.println("GrpName: " + grpName);
        System.out.println("action: " + action);
        System.out.println("broker: " + broker);
        if (action == null || action.trim().isEmpty()) {
            System.out.println("Please provide the action possible values are producer/consumer, returning without any processing");
            return;
        }
        if (action.equalsIgnoreCase("producer")) {
            runCityProducer(broker, topic);
        }
        if (action.equalsIgnoreCase("consumer")) {
            if (topic != null && topic.equalsIgnoreCase("population")) {
                runPolutionConsumer(grpName, broker, topic);
            }

            if (topic != null && topic.equalsIgnoreCase("cityinfo")) {
                runCityConsumer(grpName, broker, topic);
            }
        }
        if (action.equalsIgnoreCase("stream")) {
            Aggregation.streamTotalPopulationPerState();
        }

        if (action.equalsIgnoreCase("tempStream")) {
            RecordChangesAggregation.streamTotalPopulationPerState();
        }
        if (action.equalsIgnoreCase("moviestream")) {
            MovieStream.movieStream();
        }

    }

    static void runCityProducer(String broker, String topic) {
        List<City> list = CityDataProducer.loadData();
        list.addAll(list);
        list.addAll(list);
        list.addAll(list);
        System.out.println("List size: " + list.size());
        CityDataProducer.produce(broker, topic, list);
    }
    static void runCityConsumer(String grpName, String broker, String topic) {
        List<City> list = CityDataConsumer.consumeData(grpName, broker, topic);
        list.stream().forEach(record -> {
            System.out.println("Key :" + record.getKey() + " Value :" + record.toString());
        });

    }

    static void runPolutionConsumer(String grpName, String broker, String topic) {
        List<CityAggregation> list =   PopulationConsumer.consumeData(grpName, broker, topic);
        list.stream().forEach(record -> {
           // System.out.println("Key :" + record.getKey() + " Value :" + record.toString());
        });

    }



}
