package com.gj.kafka.streams;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.City;
import com.gj.kafka.serdes.CustomSerdesFactory;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class CityStreamCount {

    public static void stream() {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, City> views = builder.stream(
                "city",
                Consumed.with(stringSerde, CustomSerdesFactory.citySerde())
        );
        //views.print(Printed.toSysOut());
        System.out.println("Views: " + views);
        final KTable<String, Long> counts = views.map((key, value) -> {
                    System.out.println("Got here :" + key);
                    return new KeyValue<String, String>(value.getStateId(), value.getCity());
                })
                .groupByKey(Grouped.with(Serdes.String(), Serdes.String())).count();
        //.groupBy((key, value)->{return value.getStateId();}).count();
        //counts.toStream().print(Printed.toSysOut());
        counts.toStream().to("citycount", Produced.with(Serdes.String(), Serdes.Long()));
        System.out.println("counts: " + counts);
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
        //views.mapValues(city->{
        //return city.getStateId();});
        //filter(city->{return })
    }
}
