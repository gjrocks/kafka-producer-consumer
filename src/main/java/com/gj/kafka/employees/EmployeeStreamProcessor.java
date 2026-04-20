package com.gj.kafka.employees;

import com.gj.kafka.constants.IKafkaConstants;
import com.newrelic.api.agent.NewRelic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
public class EmployeeStreamProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeStreamProcessor.class);
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

        logger.debug("Views: " + views);
        ValueTransformer f=null;
        //views.
        //final KTable<String, String> counts =
        views.map((key, value) -> {
                   logger.debug("Got here :" + key); return null;

                   //return new KeyValue<String, String>(value, value);
             });;
      //  views.transformValues()
//        final KTable<String, Long> counts = views.map((key, value) -> {
//                    logger.debug("Got here :" + key);
//                    return new KeyValue<String, String>(value.getStateId(), value.getCity());
//                })
//                .groupByKey(Grouped.with(Serdes.String(), Serdes.String())).count();
        //.groupBy((key, value)->{return value.getStateId();}).count();
        //counts.toStream().print(Printed.toSysOut());
     //  counts.toStream().to("citycount", Produced.with(Serdes.String(), Serdes.Long()));
      //  logger.debug("counts: " + counts);
        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-devices-1");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology = builder.build();
        logger.debug("topology :" + topology.describe());
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
        logger.debug("Stream Complete");
        System.exit(0);
        //views.mapValues(city->{
        //return city.getStateId();});
        //filter(city->{return })
    }



    public static void topologyStream() {
       // final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();
        logger.debug("Ganesh here in topologyStream");

        Topology topologyBuilder = new Topology();
        StoreBuilder<KeyValueStore<String, String>> deviceStoreBuilder =
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore("device-store"),
                        Serdes.String(),
                        Serdes.String());
        StoreBuilder<KeyValueStore<String, String>> empStoreBuilder =
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore("emp-store"),
                        Serdes.String(),
                        Serdes.String());
        //topologyBuilder
        //topologyBuilder.
        topologyBuilder.addSource("Source", "emp1")
                //.addProcessor("Process", CycleProcessor::new, "Source")
                //.addProcessor("ChildProcess", CycleChildProcessor::new, "Process")
                .addProcessor("EmpDump", EmpDumpProcessor::new, "Source")
                //.addStateStore(deviceStoreBuilder, "Process")
                .addStateStore(empStoreBuilder, "EmpDump")

               // .connectProcessorAndStateStores("Process", "device-store")
                //.addSink("Sink", "ganesh-1", "Process")
                //.addSink("Ops", "ops-1-9", "ChildProcess")
                .addSink("Sink2", "bangaloreEmp1", "EmpDump");

        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-devices-emp-21");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9095");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //Topology topology = builder.build();

        logger.debug("topologyBuilder :" + topologyBuilder.describe());
         KafkaStreams streams = new KafkaStreams(topologyBuilder, props);
        //ProcessorContext ctx=null;
        streams.setStateListener((newState, oldState) -> {
            logger.debug("State changed from " + oldState + " to " + newState);
            if (newState == KafkaStreams.State.ERROR || newState == KafkaStreams.State.NOT_RUNNING) {
               logger.error("Kafka Streams in  state, exiting", newState);
                streams.close();
            }
        });

         CountDownLatch latch = new CountDownLatch(1);

        try {
            streams.start();

            Thread.sleep(2000);
            ThreadManager t=new ThreadManager(streams, latch);
            Thread thread=new Thread(t);
            thread.start();
            latch.await();
        } catch (final Exception e) {
            //System.exit(1);
            logger.error("Kafka Streams in ERROR state, exiting");
            streams.close();
            latch.countDown();
            logger.debug("Stream Complete");
        }

//        Runtime.getRuntime().addShutdownHook(new Thread("streams-totalviews") {
//            @Override
//            public void run() {
//
//            }
//        });

       // System.exit(0);

    }


    public  void empStream(String city) {
        // final Serde<String> stringSerde = Serdes.String();
       // final Serde<Long> longSerde = Serdes.Long();
        //MDC.put("Stream-Name", city);
        logger.debug("empStream ::" +city);

        Topology topologyBuilder = new Topology();

        StoreBuilder<KeyValueStore<String, String>> empStoreBuilder =
                Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(city.replace(" ","_").toLowerCase()),
                        Serdes.String(),
                        Serdes.String());
        //topologyBuilder
        //topologyBuilder.
        topologyBuilder.addSource("Source", "emp1")

                .addProcessor("EmpDump", ()->new EmpProcessor(city), "Source")

                .addStateStore(empStoreBuilder, "EmpDump")

                .addSink("Sink2", city.replace(" ","_").toLowerCase(), "EmpDump");

        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-emp-" + city.replace(" ","_").toLowerCase());
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9095");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //Topology topology = builder.build();

        logger.debug("topologyBuilder :" + topologyBuilder.describe());
        KafkaStreams streams = new KafkaStreams(topologyBuilder, props);
        //streams.metrics()
        //ProcessorContext ctx=null;
        streams.setStateListener((newState, oldState) -> {
            MDC.put("Stream-Name", city);
            MDC.put("logKey", "Stream-State-Change");
            MDC.put("OldState", oldState.toString());
            MDC.put("NewState", newState.toString());
            logger.debug("State changed from " + oldState + " to " + newState);

            Map<String, Object> eventAttributes = new HashMap<>();
            eventAttributes.put("streamName", city.replace(" ","_"));
            eventAttributes.put("status", newState.toString());
            eventAttributes.put("oldStatus", oldState.toString());

            NewRelic.getAgent().getInsights().recordCustomEvent("StreamPoolStatus", eventAttributes);

            if (newState == KafkaStreams.State.ERROR || newState == KafkaStreams.State.NOT_RUNNING) {
                logger.error("Kafka Streams in  state, exiting", newState);
                streams.close();
            }
        });

        CountDownLatch latch = new CountDownLatch(1);

        try {
            streams.start();
            //streams.

            Thread.sleep(2000);
            ThreadManager t=new ThreadManager(streams,latch);
            Thread thread=new Thread(t);
            thread.start();
            latch.await();
        } catch (final Exception e) {
            //System.exit(1);
            logger.error("Kafka Streams in ERROR state, exiting");
            streams.close();
            latch.countDown();
            logger.debug("Stream Complete");
        }


    logger.debug("empStream Complete" );
    }
}
