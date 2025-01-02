package com.gj.kafka.devices;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.City;
import com.gj.kafka.serdes.CustomSerdesFactory;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

public class DeviceStreamProcessor {

    public static void stream() {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        final StreamsBuilder builder = new StreamsBuilder();
        //builder.add

        KStream<String, String> views = builder.stream(
                "devices",
                Consumed.with(stringSerde, stringSerde)
        );

        StoreBuilder<KeyValueStore<String, String>> indexStore = Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore("Data"), Serdes.String(), Serdes.String());

//        StateStoreSupplier countStore = Stores.create("Counts")
//                .withKeys(Serdes.String())
//                .withValues(Serdes.Long())
//                .persistent()
//                .build();
               // .withLoggingEnabled(changelogConfig);
        //views.print(Printed.toSysOut());
        builder.addStateStore(indexStore);

        System.out.println("Views: " + views);
        ValueTransformer f=null;
        //views.
        //final KTable<String, String> counts =
        views.map((key, value) -> {
                   System.out.println("Got here :" + key); return null;

                   //return new KeyValue<String, String>(value, value);
             });;
      //  views.transformValues()
//        final KTable<String, Long> counts = views.map((key, value) -> {
//                    System.out.println("Got here :" + key);
//                    return new KeyValue<String, String>(value.getStateId(), value.getCity());
//                })
//                .groupByKey(Grouped.with(Serdes.String(), Serdes.String())).count();
        //.groupBy((key, value)->{return value.getStateId();}).count();
        //counts.toStream().print(Printed.toSysOut());
     //  counts.toStream().to("citycount", Produced.with(Serdes.String(), Serdes.Long()));
      //  System.out.println("counts: " + counts);
        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-devices-1");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology = builder.build();
        System.out.println("topology :" + topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);
        ProcessorContext ctx=null;

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



    public static void topologyStream() {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();


        Topology topologyBuilder = new Topology();
        StoreBuilder<KeyValueStore<String, String>> deviceStoreBuilder =
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore("device-store"),
                        Serdes.String(),
                        Serdes.String());
        //topologyBuilder
        //topologyBuilder.
        topologyBuilder.addSource("Source", "devices")
                .addProcessor("Process", DeviceProcessor::new, "Source")
                .addStateStore(deviceStoreBuilder, "Process")
               // .connectProcessorAndStateStores("Process", "device-store")
                .addSink("Sink", "devices-output", "Process");

        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-devices-1");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //Topology topology = builder.build();

        System.out.println("topologyBuilder :" + topologyBuilder.describe());
        final KafkaStreams streams = new KafkaStreams(topologyBuilder, props);
        //ProcessorContext ctx=null;

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
