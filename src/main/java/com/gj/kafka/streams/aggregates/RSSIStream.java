package com.gj.kafka.streams.aggregates;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.Beacon;
import com.gj.kafka.model.City;
import com.gj.kafka.model.CityAggregation;
import com.gj.kafka.model.RSSI;
import com.gj.kafka.serdes.CustomSerdesFactory;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.WindowStore;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import static org.apache.kafka.common.serialization.Serdes.ListSerde;

public class RSSIStream {

    public static void streamTotalPopulationPerState() {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();
        HashMap<String,City> internalStore=new HashMap<>();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, City> views = builder.stream(
                "cityinfo",
                Consumed.with(stringSerde, CustomSerdesFactory.citySerde())
        );

        views.foreach((key,newEvent)->{
            if(internalStore.get(key)!=null){
               City oldEvent=internalStore.get(key);

            }


        });


     //views.filter() send it to city wise. try this too. try to use multiple filters and to
        //check if same message is delivered to all
//see if the
       /* KStream<String,CityAggregation> cityPops=views.map((key,value)->{
            KeyValue<String,CityAggregation> keyVal=new KeyValue<>(value.getStateId(),new CityAggregation(value.getPopulation()));
           return keyVal;
        });*/

      //  KGroupedStream<String, City> KGS0=views.groupByKey(Grouped.with(Serdes.String(), CustomSerdesFactory.citySerde()));

      /* KTable<String,City > KT0 = KGS0.reduce((aggValue, newValue) -> {
           //newValue.setPopulation(newValue.getPopulation() + aggValue.getPopulation());
           System.out.println("aggValue key: " + aggValue.getKey() + " aggValue value: " + aggValue.getTemp());
           System.out.println("newValue key: " + newValue.getKey() + " newValue value: " + newValue.getTemp());
           if(aggValue.getTemp()!=null && newValue.getTemp()!=null){
               if(aggValue.getTemp()!=newValue.getTemp()){
                   aggValue.setDuplicate(0);
                   newValue.setDuplicate(0);
               }
           }
           return newValue;
        });*/



       // KT0.toStream().print(Printed.<String, CityAggregation>toSysOut().withLabel("[Total Earning]"));
     //  KT0.toStream().peek((key,val)->{
      //     System.out.println("Key :"+ key + " Value :" +val.getTemp());
          // System.out.println("Value Type :" + val!=null?val.getClass().getName():"null");
          // if(val!=null){
            //   System.out.println(val.getTemp());
          // }
    //   });


       //.to("cityunique", Produced.with(Serdes.String(), CustomSerdesFactory.citySerde()));
      //  System.out.println("counts: " +totalViews);
        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-totalviews4");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology= builder.build();
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

    public static void working() {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, City> views = builder.stream(
                "city",
                Consumed.with(stringSerde, CustomSerdesFactory.citySerde())
        );
        //views.print(Printed.toSysOut());
        System.out.println("Views: " +views);
        views.filter((k,v)->{return false;});
        /*final KTable<String, Integer> counts=views.map((key, value)->{
                    System.out.println("Got here now :"+ key);
                    return 	new KeyValue<String, Integer>(value.getStateId(),value.getPopulation());})
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                .reduce(Integer::sum); */

      /* final KTable<String, Long> totalViews=views.map( (key,value)->{
                    System.out.println("Got here noww :"+ key);
                    return 	new KeyValue<String, Long>(value.getStateId(),Long.parseLong(value.getPopulation()+""));})
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                .reduce(Long::sum);
    */
        /** working
         views.map( (key,value)->{
         System.out.println("Got here noww :"+ key);
         return 	new KeyValue<String, Long>(value.getStateId(),Long.parseLong(value.getPopulation()+""));})
         .to("population", Produced.with(Serdes.String(), Serdes.Long()));

         **/

  /* KGroupedStream<String,Long> streamPopulation=views.map( (key,value)->{
                    System.out.println("Got here noww :"+ key);
                    return 	new KeyValue<String, Long>(value.getStateId(),Long.parseLong(value.getPopulation()+""));})
          .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()));
*/
        //.to("population", Produced.with(Serdes.String(), Serdes.Long()));
        // streamPopulation.aggregate()
      /* KTable<String,CityAggregation> ktable= streamPopulation.aggregate(() -> new CityAggregation(0L),
                (key, value, aggregate) -> {
                    CityAggregation temp=      new CityAggregation();
                    System.out.println("agrregate for key" + key);
                    temp.setPopulation(temp.getPopulation()+aggregate.getPopulation());
                    return  temp;
                },
                Materialized.with(Serdes.String(), CustomSerdesFactory.cityAggregationSerde()));

        ktable.toStream().to("population", Produced.with(Serdes.String(), CustomSerdesFactory.cityAggregationSerde()));//print(Printed.<String, CityAggregation>toSysOut().withLabel("City Population Aggregate"));*/
        /** working onw **/
        KTable<String, Long> totalViews = views
                .mapValues(v -> {
                    System.out.println("Printing v" + v);
                    return Long.parseLong(v.getPopulation()+"");
                })
                .groupByKey(Grouped.with(stringSerde, longSerde))
                .reduce(Long::sum);
        //.groupBy((key, value)->{return value.getStateId();}).count();
        //   counts.toStream().foreach((key,value)->{
        //       System.out.println("Key: " + key +"  value: " + value);
        //   });//.print(Printed.toSysOut());
        totalViews.toStream().to("population", Produced.with(Serdes.String(), Serdes.Long()));
        //  System.out.println("counts: " +totalViews);
        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-totalviews4");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology= builder.build();
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

    /**
     * /*,
     *                 (k,oldValue,aggValue) -> {
     *                     aggValue.remove(oldValue);
     *                     return aggValue;
     *                 }*/

    public static void rssiOrdered() {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, RSSI> views = builder.stream(
                "rssi",
                Consumed.with(stringSerde, CustomSerdesFactory.rssiSerde())
        );
        //views.print(Printed.toSysOut());
        System.out.println("Views: " +views);
       // views.filter((k,v)->{ return v.getBeacon().equalsIgnoreCase("B1");}).foreach((k1,v1)-> System.out.println(v1));

        TimeWindowedKStream<String,RSSI> rssiGrpByHubStreamForB1Beacon=views.filter((key,value)->{
                                                            return value.getBeacon().equalsIgnoreCase("B1");}
                                                            ).map( (key,value)->{
            System.out.println("Got here noww :"+ key);
            return 	new KeyValue<String, RSSI>(value.getHub(),value);})
          .groupByKey(Grouped.with(Serdes.String(), CustomSerdesFactory.rssiSerde()))
               .windowedBy(SlidingWindows.withTimeDifferenceAndGrace(Duration.ofSeconds(25), Duration.ofSeconds(1)))     ;
      //write aggregate code for rssiGrpByHubStreamForB1Beacon
        /*rssiGrpByHubStreamForB1Beacon.aggregate(RSSIAggregation::new,
                (k, newValue, aggValue) -> {
                    aggValue.add(newValue);
                    return aggValue;
                },
            Materialized<K, VR, WindowStore<Bytes, byte[]>> var3);*/
        KTable<Windowed<String>, RSSIAggregation> ktableRssi=  rssiGrpByHubStreamForB1Beacon.aggregate(
                RSSIAggregation::new,
                (k, newValue, aggValue) -> {
                    aggValue.add(newValue);
                    return aggValue;
                },
                Materialized.<String,RSSIAggregation, WindowStore<Bytes,byte[]>>as("top-rssi")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(CustomSerdesFactory.rSSIAggregationSerde())
        );
        ktableRssi.toStream().peek((k,v)->{
            System.out.println("++++++++++++============================");
            System.out.println("KEY : " + k );
            if (v==null){
                System.out.println("Value is null");
            }
            if(v!=null && v.getRssiSet()==null){
                System.out.println("Value rssi set is null");
            }
            if(v!=null && v.getRssiSet()!=null && v.getRssiSet().isEmpty()) {
                System.out.println("Value rssi set is empty");
            }
            if(v!=null && v.getRssiSet()!=null && !v.getRssiSet().isEmpty()) {
                System.out.println("Values size:"+ v.getRssiSet().size());
                v.print();
            }
            System.out.println("++++++++++++============================");
        });


        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-rssi-1");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology= builder.build();
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

    public void rssiStreams_full()
        {
            final Serde<String> stringSerde = Serdes.String();
            final Serde<Long> longSerde = Serdes.Long();

            final StreamsBuilder builder = new StreamsBuilder();

            KStream<String, RSSI> views = builder.stream(
                    "rssi",
                    Consumed.with(stringSerde, CustomSerdesFactory.rssiSerde())
            );

          /* GlobalKTable<String,String> beacons= builder.globalTable("beacons",
                    Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as("beacon-details")
                            .withKeySerde(Serdes.String())
                            .withValueSerde(Serdes.String()));*/

           // ReadOnlyKeyValueStore<String, String> leveragePriceView = builder.store(StoreQueryParameters.fromNameAndType("beacons", QueryableStoreTypes.keyValueStore()));
            //views.print(Printed.toSysOut());
            System.out.println("Views: " +views);
            // views.filter((k,v)->{ return v.getBeacon().equalsIgnoreCase("B1");}).foreach((k1,v1)-> System.out.println(v1));
            /*views.map((key,value)->{
                System.out.println("Got here noww :"+ key);
                return 	new KeyValue<String, RSSI>(value.getBeacon(),value);});*/
            TimeWindowedKStream<String,RSSI> rssiGrpByHubStreamForB1Beacon=views.filter((key,value)->{
                        return value.getBeacon().equalsIgnoreCase("B1");}
                    ).map( (key,value)->{
                        System.out.println("Got here noww :"+ key);
                        return 	new KeyValue<String, RSSI>(value.getHub(),value);})
                    .groupByKey(Grouped.with(Serdes.String(), CustomSerdesFactory.rssiSerde()))
                    .windowedBy(SlidingWindows.withTimeDifferenceAndGrace(Duration.ofSeconds(25), Duration.ofSeconds(1)))     ;
            //write aggregate code for rssiGrpByHubStreamForB1Beacon
        /*rssiGrpByHubStreamForB1Beacon.aggregate(RSSIAggregation::new,
                (k, newValue, aggValue) -> {
                    aggValue.add(newValue);
                    return aggValue;
                },
            Materialized<K, VR, WindowStore<Bytes, byte[]>> var3);*/
            KTable<Windowed<String>, RSSIAggregation> ktableRssi=  rssiGrpByHubStreamForB1Beacon.aggregate(
                    RSSIAggregation::new,
                    (k, newValue, aggValue) -> {
                        aggValue.add(newValue);
                        return aggValue;
                    },
                    Materialized.<String,RSSIAggregation, WindowStore<Bytes,byte[]>>as("top-rssi")
                            .withKeySerde(Serdes.String())
                            .withValueSerde(CustomSerdesFactory.rSSIAggregationSerde())
            );
            ktableRssi.toStream().peek((k,v)->{
                System.out.println("++++++++++++============================");
                System.out.println("KEY : " + k );
                if (v==null){
                    System.out.println("Value is null");
                }
                if(v!=null && v.getRssiSet()==null){
                    System.out.println("Value rssi set is null");
                }
                if(v!=null && v.getRssiSet()!=null && v.getRssiSet().isEmpty()) {
                    System.out.println("Value rssi set is empty");
                }
                if(v!=null && v.getRssiSet()!=null && !v.getRssiSet().isEmpty()) {
                    System.out.println("Values size:"+ v.getRssiSet().size());
                    v.print();
                }
                System.out.println("++++++++++++============================");
            });


            final Properties props = new Properties();
            props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-rssi-1");
            props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
            Topology topology= builder.build();
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

    public static void globalTableDemo()
    {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> views = builder.stream(
                "beacons",
                Consumed.with(stringSerde, stringSerde)
        );
        /*views.foreach((k,v)->{
            System.out.println(" Key :" +k + "Value :" + v);
        });*/

       KGroupedStream<String,String> groupedByBeaconIds= views.groupByKey(Grouped.with(stringSerde,stringSerde));
        groupedByBeaconIds.count(Materialized.as("CountsKeyValueStore"));
//Materialized<String, String, KeyValueStore<Bytes, byte[]>.as("CountsKeyValueStore")
       /* GlobalKTable<String,String> beacons= builder.globalTable("beacons",
                Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as("beacon-details")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.String()));*/


        //groupedByBeaconIds


        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-beacons-231");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology= builder.build();
        System.out.println("topology :" + topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);

        streams.setStateListener(new KafkaStreams.StateListener() {
            @Override
            public void onChange(KafkaStreams.State newState, KafkaStreams.State oldState) {
                if(newState == KafkaStreams.State.RUNNING) {
                    ReadOnlyKeyValueStore<String, Long> keyValueStore =  streams.store(StoreQueryParameters.fromNameAndType("CountsKeyValueStore", QueryableStoreTypes.keyValueStore()));

                    System.out.println("count for B1:" + keyValueStore.get("B1"));
                    //return customer;
                }
            }
        });

        final CountDownLatch latch = new CountDownLatch(1);

        try {
            streams.start();

    Thread t=new Thread(new MyRunnable(streams));
    t.start();
            if(streams.state() == KafkaStreams.State.RUNNING) {
                ReadOnlyKeyValueStore<String, Long> keyValueStore =  streams.store(StoreQueryParameters.fromNameAndType("CountsKeyValueStore", QueryableStoreTypes.keyValueStore()));

                System.out.println("count for B1:" + keyValueStore.get("B1"));
                //return customer;
            }


            latch.await();
        } catch (final Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread("streams-totalviews45") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        System.out.println("Stream Complete");
        System.exit(0);

    }

    public static void getBeaconsData()
    {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> views = builder.stream(
                "beacons1",
                Consumed.with(stringSerde, stringSerde)
        );
       /* views.foreach((k,v)-> {
                    System.out.println(" Key :" + k + "Value :" + v);
                });*/
        /*views.foreach((k,v)->{
            System.out.println(" Key :" +k + "Value :" + v);

            TimeWindowedKStream<String,RSSI> rssiGrpByHubStreamForB1Beacon=views.filter((key,value)->{
                        return value.getBeacon().equalsIgnoreCase("B1");}
                    ).map( (key,value)->{
                        System.out.println("Got here noww :"+ key);
                        return 	new KeyValue<String, RSSI>(value.getHub(),value);})
                    .groupByKey(Grouped.with(Serdes.String(), CustomSerdesFactory.rssiSerde()))
                    .windowedBy(SlidingWindows.withTimeDifferenceAndGrace(Duration.ofSeconds(25), Duration.ofSeconds(1)))
        });

        TimeWindowedKStream<String,String> groupedByBeaconIds= views.groupByKey(Grouped.with(stringSerde,stringSerde))
                                                                    .windowedBy(SlidingWindows.withTimeDifferenceAndGrace(Duration.ofSeconds(2), Duration.ofSeconds(1)));
        KTable<Windowed<String>, BeaconsAggregations> beaconsTable=  groupedByBeaconIds.aggregate(
                BeaconsAggregations::new,
                (k, newValue, aggValue) -> {
                    aggValue.add(newValue);
                    return aggValue;
                },
                Materialized.<String,BeaconsAggregations, WindowStore<Bytes,byte[]>>as("CountsKeyValueStore")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(CustomSerdesFactory.beaconAggregationSerde())

        );*/
        KGroupedStream<String,String> groupedByBeaconIds= views.groupByKey(Grouped.with(stringSerde,stringSerde));
       /* KTable<String, BeaconsAggregations> beaconsTable= groupedByBeaconIds.aggregate(
                BeaconsAggregations::new,
                (k, newValue, aggValue) -> {
                    aggValue.add(newValue.replaceAll("\\u0000", ""));
                    return aggValue;
                },
                Materialized.<String,BeaconsAggregations, KeyValueStore<Bytes,byte[]>>as("CountsKeyValueStore")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(CustomSerdesFactory.beaconAggregationSerde())

        );*/

       /* KTable<String, List> beaconsTable= groupedByBeaconIds.aggregate(
                new ArrayList<String>(),
                (k, newValue, aggValue) -> {
                    aggValue.add(newValue);
                    return aggValue;
                },
                Materialized.<String, List<String>, KeyValueStore<Bytes,byte[]>>as("CountsKeyValueStore")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(new Serdes.ListSerde<String>())

        );*/
        //GenericRecord record=null;
        KTable<String,  ArrayList<String>> beaconsTable= groupedByBeaconIds.aggregate(
                new Initializer<List<String>>() {
                    @Override
                    public ArrayList<String> apply() {
                        return new ArrayList<String>();
                    }
                } ,
                new Aggregator() {
                    @Override
                    public Object apply(Object aggKey, Object value, Object aggregate) {
                        ArrayList<String> list = (ArrayList<String>)aggregate;
                        list.add((String) value);

                        return list;
                    }},
                Materialized.<String, List<String>, KeyValueStore<Bytes,byte[]>>as("CountsKeyValueStore")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(ListSerde(ArrayList.class,Serdes.String()))


        );
        beaconsTable.toStream().foreach((kk,vv)->{
            System.out.println("Values :" +vv);
        });
        //groupedByBeaconIds.count(Materialized.as("CountsKeyValueStore"));
        //groupedByBeaconIds.count(Materialized.as("CountsKeyValueStore"));
//Materialized<String, String, KeyValueStore<Bytes, byte[]>.as("CountsKeyValueStore")
       /* GlobalKTable<String,String> beacons= builder.globalTable("beacons",
                Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as("beacon-details")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.String()));*/


        //groupedByBeaconIds


        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-beacons-231");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology= builder.build();
        System.out.println("topology :" + topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);

     /*   streams.setStateListener(new KafkaStreams.StateListener() {
            @Override
            public void onChange(KafkaStreams.State newState, KafkaStreams.State oldState) {
                if(newState == KafkaStreams.State.RUNNING) {
                    ReadOnlyKeyValueStore<String, Long> keyValueStore =  streams.store(StoreQueryParameters.fromNameAndType("CountsKeyValueStore", QueryableStoreTypes.keyValueStore()));

                    System.out.println("count for B1:" + keyValueStore.get("B1"));
                    //return customer;
                }
            }
        });*/

        final CountDownLatch latch = new CountDownLatch(1);

        try {
            streams.start();

           // Thread t=new Thread(new MyRunnable(streams));
          //  t.start();
           /* if(streams.state() == KafkaStreams.State.RUNNING) {
                ReadOnlyKeyValueStore<String, Long> keyValueStore =  streams.store(StoreQueryParameters.fromNameAndType("CountsKeyValueStore", QueryableStoreTypes.keyValueStore()));

                System.out.println("count for B1:" + keyValueStore.get("B1"));
                //return customer;
            }
*/

            latch.await();
        } catch (final Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread("streams-totalviews45") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        System.out.println("Stream Complete");
        System.exit(0);

    }

    public static void getBeaconsData1()
    {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();
        ObjectMapper mapper = new ObjectMapper();
        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> views = builder.stream(
                "beacons",
                Consumed.with(stringSerde, stringSerde)
        );
        views.foreach((k,v)-> {
            try {
                System.out.println(" Key :" + k + "Value :" + mapper.readValue(v, Beacon.class));
            }catch(Exception w){
                w.printStackTrace();
            }
            });
        /*views.foreach((k,v)->{
            System.out.println(" Key :" +k + "Value :" + v);

            TimeWindowedKStream<String,RSSI> rssiGrpByHubStreamForB1Beacon=views.filter((key,value)->{
                        return value.getBeacon().equalsIgnoreCase("B1");}
                    ).map( (key,value)->{
                        System.out.println("Got here noww :"+ key);
                        return 	new KeyValue<String, RSSI>(value.getHub(),value);})
                    .groupByKey(Grouped.with(Serdes.String(), CustomSerdesFactory.rssiSerde()))
                    .windowedBy(SlidingWindows.withTimeDifferenceAndGrace(Duration.ofSeconds(25), Duration.ofSeconds(1)))
        });

        TimeWindowedKStream<String,String> groupedByBeaconIds= views.groupByKey(Grouped.with(stringSerde,stringSerde))
                                                                    .windowedBy(SlidingWindows.withTimeDifferenceAndGrace(Duration.ofSeconds(2), Duration.ofSeconds(1)));
        KTable<Windowed<String>, BeaconsAggregations> beaconsTable=  groupedByBeaconIds.aggregate(
                BeaconsAggregations::new,
                (k, newValue, aggValue) -> {
                    aggValue.add(newValue);
                    return aggValue;
                },
                Materialized.<String,BeaconsAggregations, WindowStore<Bytes,byte[]>>as("CountsKeyValueStore")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(CustomSerdesFactory.beaconAggregationSerde())

        );*/
      /*  KGroupedStream<String,String> groupedByBeaconIds= views.groupByKey(Grouped.with(stringSerde,stringSerde));
        KTable<String, BeaconsAggregations> beaconsTable= groupedByBeaconIds.aggregate(
                BeaconsAggregations::new,
                (k, newValue, aggValue) -> {
                    aggValue.add(newValue.replaceAll("[\\x00-\\x09\\x11\\x12\\x14-\\x1F\\x7F]", ""));
                    return aggValue;
                },
                Materialized.<String,BeaconsAggregations, KeyValueStore<Bytes,byte[]>>as("CountsKeyValueStore")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(CustomSerdesFactory.beaconAggregationSerde())

        );
        beaconsTable.toStream().foreach((kk,vv)->{
            System.out.println("Values :" +vv);
        });*/
        //groupedByBeaconIds.count(Materialized.as("CountsKeyValueStore"));
        //groupedByBeaconIds.count(Materialized.as("CountsKeyValueStore"));
//Materialized<String, String, KeyValueStore<Bytes, byte[]>.as("CountsKeyValueStore")
       /* GlobalKTable<String,String> beacons= builder.globalTable("beacons",
                Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as("beacon-details")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.String()));*/


        //groupedByBeaconIds


        final Properties props = new Properties();
        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "streams-beacons-231");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS_ALL);
        Topology topology= builder.build();
        System.out.println("topology :" + topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);

     /*   streams.setStateListener(new KafkaStreams.StateListener() {
            @Override
            public void onChange(KafkaStreams.State newState, KafkaStreams.State oldState) {
                if(newState == KafkaStreams.State.RUNNING) {
                    ReadOnlyKeyValueStore<String, Long> keyValueStore =  streams.store(StoreQueryParameters.fromNameAndType("CountsKeyValueStore", QueryableStoreTypes.keyValueStore()));

                    System.out.println("count for B1:" + keyValueStore.get("B1"));
                    //return customer;
                }
            }
        });*/

        final CountDownLatch latch = new CountDownLatch(1);

        try {
            streams.start();

           // Thread t=new Thread(new MyRunnable(streams));
           // t.start();
           /* if(streams.state() == KafkaStreams.State.RUNNING) {
                ReadOnlyKeyValueStore<String, Long> keyValueStore =  streams.store(StoreQueryParameters.fromNameAndType("CountsKeyValueStore", QueryableStoreTypes.keyValueStore()));

                System.out.println("count for B1:" + keyValueStore.get("B1"));
                //return customer;
            }
*/

            latch.await();
        } catch (final Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread("streams-totalviews45") {
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
