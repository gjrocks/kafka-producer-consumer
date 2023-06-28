package com.gj.kafka.streams.aggregates;

import com.gj.kafka.constants.IKafkaConstants;
import com.gj.kafka.model.City;
import com.gj.kafka.model.CityAggregation;
import com.gj.kafka.serdes.CustomSerdesFactory;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class RecordChangesAggregation {

    public static void streamTotalPopulationPerState() {
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, City> views = builder.stream(
                "city",
                Consumed.with(stringSerde, CustomSerdesFactory.citySerde())
        );
        //views.print(Printed.toSysOut());
        System.out.println("Views: " +views);
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
        KTable<String, Long> totalViews = views.map((key,value)->{
            System.out.println("Got here noww :"+ key);
            return 	new KeyValue<String, Long>(value.getStateId(),Long.parseLong(value.getPopulation()+""));})
                .groupByKey(Grouped.with(stringSerde, longSerde))
                .reduce(Long::sum);**/
        //.groupBy((key, value)->{return value.getStateId();}).count();
     //   counts.toStream().foreach((key,value)->{
     //       System.out.println("Key: " + key +"  value: " + value);
     //   });//.print(Printed.toSysOut());
        KStream<String,CityAggregation> cityPops=views.map((key,value)->{
            KeyValue<String,CityAggregation> keyVal=new KeyValue<>(value.getStateId(),new CityAggregation(value.getPopulation()));
           return keyVal;
        });

        KGroupedStream<String, CityAggregation> KGS0=cityPops.groupByKey(Grouped.with(Serdes.String(), CustomSerdesFactory.cityAggregationSerde()));

        KTable<String, CityAggregation> KT0 = KGS0.reduce((aggValue, newValue) -> {
            newValue.setPopulation(newValue.getPopulation() + aggValue.getPopulation());
            return newValue;
        });

       // KT0.toStream().print(Printed.<String, CityAggregation>toSysOut().withLabel("[Total Earning]"));
       KT0.toStream().peek((key,val)->{
           System.out.println("Key :"+ key);
           System.out.println("Value Type :" + val!=null?val.getClass().getName():"null");
           if(val!=null){
               System.out.println(val.toString());
           }
       }).to("population", Produced.with(Serdes.String(), CustomSerdesFactory.cityAggregationSerde()));
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

}
