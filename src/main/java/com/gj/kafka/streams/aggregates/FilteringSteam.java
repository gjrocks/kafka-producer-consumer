package com.gj.kafka.streams.aggregates;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.City;
import com.gj.kafka.producer.CityDataProducer;
import com.gj.kafka.serdes.CustomSerdesFactory;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                       (key, city) -> {
                           System.out.println("I am here"); return city.getId()==1840034016;},
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


    public static void doFilter(final String brokers) throws InterruptedException {
        //filter kafka stream data
       final Serde<String> stringSerde = Serdes.String();
       final Serde<Long> longSerde = Serdes.Long();
       HashMap<String, City> internalStore = new HashMap<>();
       final StreamsBuilder builder = new StreamsBuilder();

       KStream<String, City> views = builder.stream(
               "cityinfo",  Consumed.with(stringSerde, CustomSerdesFactory.citySerde()));

       views.filter((s, city) -> city.getId()==1840020491).foreach((k, v) -> {//do nothing terminating operator
            });
        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-totalviews5");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology = builder.build();
        System.out.println("topology :" + topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);

        final CountDownLatch latch = new CountDownLatch(1);

        try {
            streams.start();
            latch.await();}catch (Exception e){}
        Runtime.getRuntime().addShutdownHook(new Thread("streams-totalviews") {
            @Override
                public void run() {
                   streams.close();
                latch.countDown();
                System.out.println("Stream Complete");
                System.exit(0);
            }
        });

    }


    public static void processAndSendToDynamicTopic(final String brokers) {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        HashMap<String, City> internalStore = new HashMap<>();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, City> views = builder.stream(
                "cityinfo",
                Consumed.with(stringSerde, CustomSerdesFactory.citySerde())
        );

        views.foreach((key, city) -> {//do nothing terminating operator
            //write a custom logi to derive ur topic name
            List<City> li=new ArrayList<>();
            li.add(city);
            //state
            List<RecordMetadata> mi= CityDataProducer.produce(brokers,city.getStateId(), li);
            //cityid

             // logical topic name


            mi.forEach(record->{
                System.out.println("Record sent to :" + record.topic() + "with key :" + city.getKey());
            });

        });
        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "processAndSendToDynamicTopic");
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


    public static void filterAndSendToTopic_withBranching_dynamicTopicNameExtraction(final String brokers) {
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
                        (key, city) -> city.getStateId().equalsIgnoreCase("CA"),
                        Branched.withConsumer(ks -> {
                            System.out.println("Record for the state CA");
                            //ks.to("1840020491");
                            ks.to((key, city, recordContext) ->{
                                //write your own logic here to get topic name

                                return city.getCity().replaceAll(" ","_").toLowerCase();});
                        }))
                .branch(
                        (key, city) -> city.getStateId().equalsIgnoreCase("NY"),
                        Branched.withConsumer(ks -> {
                            System.out.println("Record for the state NY");
                            //ks.to("1840000494");

                            ks.to((key, city, recordContext) ->{
                                //write your own logic here to get topic name

                                return city.getCity().replaceAll(" ","_").toLowerCase();});

                        }))
                .branch(
                        (key, city) -> {
                            return city.getStateId().equalsIgnoreCase("IL");},
                        Branched.withConsumer(ks -> {
                            System.out.println("Record for the state IL");
                           // ks.to("allcities");

                            ks.to((key, city, recordContext) ->{
                                //write your own logic here to get topic name

                                return city.getCity().replaceAll(" ","_").toLowerCase();});
                        }));
        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "filterAndSendToTopic_withBranching_dynamicTopicNameExtraction");
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

        Runtime.getRuntime().addShutdownHook(new Thread("filterAndSendToTopic_withBranching_dynamicTopicNameExtraction") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        System.out.println("filterAndSendToTopic_withBranching_dynamicTopicNameExtraction Complete");
        System.exit(0);

    }


    public static void processAndSendToDynamicTopic_flatmap(final String brokers) {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        HashMap<String, City> internalStore = new HashMap<>();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, City> views = builder.stream(
                "cityinfo",
                Consumed.with(stringSerde, CustomSerdesFactory.citySerde())
        );
        views.flatMap((key,city)->{
        List<KeyValue<String,City>>  list= new ArrayList<KeyValue<String,City>>();

            KeyValue<String,City> cityName=new KeyValue<>(city.getCity().replaceAll(" ","_"),city);
            KeyValue<String,City> cityConuty=new KeyValue<>(city.getCountyName().replaceAll(" ","_"),city);
            KeyValue<String,City> cityState=new KeyValue<>(city.getStateId(),city);
            list.add(cityName);
            list.add(cityState);
            list.add(cityConuty);
            return list;

        }).to((key, city, recordContext) ->{
            //write your own logic here to get topic name
            System.out.println("Sending Data to Topic :" + key);
            return key;
    },org.apache.kafka.streams.kstream.Produced.with(Serdes.String(), CustomSerdesFactory.citySerde()));

        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "processAndSendToDynamicTopic_flatmap");
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

        Runtime.getRuntime().addShutdownHook(new Thread("processAndSendToDynamicTopic_flatmap") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        System.out.println("Stream processAndSendToDynamicTopic_flatmap Complete");
        System.exit(0);

    }

    public static void processAndSendToDynamicTopic_flatmap_TopicNameDerived_DifferentTypePayload(final String brokers) {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        HashMap<String, City> internalStore = new HashMap<>();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, City> views = builder.stream(
                "cityinfo",
                Consumed.with(stringSerde, CustomSerdesFactory.citySerde())
        );
        views.flatMap((key,city)->{
            List<KeyValue<String, Payload>>  list= new ArrayList<KeyValue<String, Payload>>();

            KeyValue<String, Payload> cityName=new KeyValue<>(key,new Payload(city.getCity().replaceAll(" ","_"),city,City.class.getName(),"SEG_BY_CITYNAME"));
            KeyValue<String, Payload> population=new KeyValue<>(key,new Payload(city.getCity().replaceAll(" ","_"),city.getPopulation(),Integer.class.getName(),"SEG_POPOLATION_BY_CITYNAME"));
            KeyValue<String, Payload> cityState=new KeyValue<>(key, new Payload(city.getStateId(),city,City.class.getName(),"SEG_BY_STATENAME"));
            list.add(cityName);
            list.add(cityState);
            list.add(population);

            return list;

        })


                .split()
                .branch(
                        (key, payload) -> payload.getFunctionality().equalsIgnoreCase("SEG_BY_CITYNAME"),
                        Branched.withConsumer(ks -> {
                            System.out.println("Record for the SEG_BY_CITYNAME");
                            //ks.to("1840020491");
                            ks.to((key, payload, recordContext) ->{
                                //write your own logic here to get topic name

                                return payload.getDestinationTopicName();}, Produced.with(Serdes.String(),null));//.getCity().replaceAll(" ","_").toLowerCase();});
                        })); /*.filter().to((key, citypayload, recordContext) ->{
            //write your own logic here to get topic name
            System.out.println("Sending Data to Topic :" + citypayload.getDestinationTopicName());
            return citypayload.getDestinationTopicName();
        },org.apache.kafka.streams.kstream.Produced.with(Serdes.String(), CustomSerdesFactory.citySerde()));*/

        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "processAndSendToDynamicTopic_flatmap");
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

        Runtime.getRuntime().addShutdownHook(new Thread("processAndSendToDynamicTopic_flatmap") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        System.out.println("Stream processAndSendToDynamicTopic_flatmap Complete");
        System.exit(0);

    }


    public static void filterByCityName(final String brokers,final String topic,final String cityName) throws InterruptedException {
        //filter kafka stream data
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();
        HashMap<String, City> internalStore = new HashMap<>();
        final StreamsBuilder builder = new StreamsBuilder();
        System.out.println("Brokers: " +brokers +" topic: " + topic +"cityName: " +cityName);
        KStream<String, City> views = builder.stream(
                topic,  Consumed.with(stringSerde, CustomSerdesFactory.citySerde()));

        views.filter((s, city) -> city.getCity().equalsIgnoreCase(cityName)).foreach((k, v) -> {

            System.out.println("Key: " +k + "Value: " + v);

        });
        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-totalviews5");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology = builder.build();
        System.out.println("topology :" + topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);

        final CountDownLatch latch = new CountDownLatch(1);

        try {
            streams.start();
            latch.await();}catch (Exception e){}
        Runtime.getRuntime().addShutdownHook(new Thread("streams-totalviews") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
                System.out.println("Stream Complete");
                System.exit(0);
            }
        });

    }

}
