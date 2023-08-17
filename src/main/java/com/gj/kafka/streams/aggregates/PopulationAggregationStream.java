package com.gj.kafka.streams.aggregates;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.City;
import com.gj.kafka.model.CityAggregation;
import com.gj.kafka.serdes.CustomSerdesFactory;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class PopulationAggregationStream {

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
    public static void cityPopulationAggregation() {
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

        KGroupedStream<String,City> streamPopulation=views.map( (key,value)->{
                    System.out.println("Got here noww :"+ key);
                    return 	new KeyValue<String, City>(value.getStateId(),value);})
          .groupByKey(Grouped.with(Serdes.String(), CustomSerdesFactory.citySerde()));



         KTable<String, CityAggregation> ktable= streamPopulation.aggregate(() -> new CityAggregation(0L),
                (key, value, aggregate) -> {
                    System.out.println("agrregate for key" + key);
                    aggregate.setPopulation(aggregate.getPopulation()+value.getPopulation());
                    return  aggregate;
                },
                Materialized.with(Serdes.String(), CustomSerdesFactory.cityAggregationSerde()));

        ktable.toStream().to("populationaggregation", Produced.with(Serdes.String(), CustomSerdesFactory.cityAggregationSerde()));//print(Printed.<String, CityAggregation>toSysOut().withLabel("City Population Aggregate"));
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
        /** working onw
        KTable<String, Long> totalViews = views
                .mapValues(v -> {
                    System.out.println("Printing v" + v);
                    return Long.parseLong(v.getPopulation()+"");
                })
                .groupByKey(Grouped.with(stringSerde, longSerde))
                .reduce(Long::sum);**/
        //.groupBy((key, value)->{return value.getStateId();}).count();
        //   counts.toStream().foreach((key,value)->{
        //       System.out.println("Key: " + key +"  value: " + value);
        //   });//.print(Printed.toSysOut());
        //totalViews.toStream().to("population", Produced.with(Serdes.String(), Serdes.Long()));
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
}
