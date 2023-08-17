package com.gj.kafka.streams.aggregates;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.City;
import com.gj.kafka.serdes.CustomSerdesFactory;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class FilteringSteam {

    public static void filterAndSendToTopic_manual(final String brokers) {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        HashMap<String, City> internalStore = new HashMap<>();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, City> views = builder.stream(
                "cityinfo",
                Consumed.with(stringSerde, CustomSerdesFactory.citySerde())
        );


        views.filter((key, city) -> city.getId() == 1840020491)
                .to("1840020491");
        views.filter((key, city) -> city.getId() == 1840000494)
                .to("1840000494");
        views.filter((key, city) -> city.getId() == 1840034016)
                .to("1840034016");


        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-totalviews4");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology = builder.build();
        System.out.println("topology :" + topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);

        final CountDownLatch latch = new CountDownLatch(1);

        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread("streams-totalviews") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        System.out.println("Stream Complete");
        System.exit(0);

    }
    public static void filterAndSendToTopic_withBranching(final String brokers) {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        HashMap<String, City> internalStore = new HashMap<>();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, City> views = builder.stream(
                "cityinfo",
                Consumed.with(stringSerde, CustomSerdesFactory.citySerde())
        );


              views.split()
               .branch(
                       (key, city) -> city.getId()==1840020491,
                       Branched.withConsumer(ks -> {
                           System.out.println("going to 1840020491");
                           ks.to("1840020491");
                       }))
               .branch(
                       (key, city) -> city.getId()==1840000494,
                       Branched.withConsumer(ks -> {
                           System.out.println("going to 1840000494");
                           ks.to("1840000494");
                       }))
               .branch(
                       (key, city) -> city.getId()==1840034016,
                       Branched.withConsumer(ks -> {
                           System.out.println("going to 1840034016");
                           ks.to("1840034016");
                       }));
        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-totalviews4");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology = builder.build();
        System.out.println("topology :" + topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);

        final CountDownLatch latch = new CountDownLatch(1);

        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread("streams-totalviews") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        System.out.println("Stream Complete");
        System.exit(0);

    }
   public static void filterAndSendToTopic(final String brokers) {
       final Serde<String> stringSerde = Serdes.String();
       final Serde<Long> longSerde = Serdes.Long();

       HashMap<String, City> internalStore = new HashMap<>();

       final StreamsBuilder builder = new StreamsBuilder();

       KStream<String, City> views = builder.stream(
               "cityinfo",
               Consumed.with(stringSerde, CustomSerdesFactory.citySerde())
       );
       views.filter(new CityPredicate(brokers)).foreach((k, v) -> {//do nothing terminating operator
       });
       final Properties props = new Properties();
       props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-totalviews4");
       props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
       Topology topology = builder.build();
       System.out.println("topology :" + topology.describe());
       final KafkaStreams streams = new KafkaStreams(topology, props);

       final CountDownLatch latch = new CountDownLatch(1);

       try {
           streams.start();
           latch.await();
       } catch (final Throwable e) {
           System.exit(1);
       }

       Runtime.getRuntime().addShutdownHook(new Thread("streams-totalviews") {
           @Override
           public void run() {
               streams.close();
               latch.countDown();
           }
       });
       System.out.println("Stream Complete");
       System.exit(0);

   }



}
