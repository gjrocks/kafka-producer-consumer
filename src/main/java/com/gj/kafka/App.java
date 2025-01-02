package com.gj.kafka;

import com.gj.kafka.cert.producers.TestProducer;
import com.gj.kafka.consumer.BeaconConsumer;
import com.gj.kafka.consumer.CityDataConsumer;
import com.gj.kafka.consumer.PopulationConsumer;
import com.gj.kafka.consumer.RSSIDataConsumer;
import com.gj.kafka.devices.DeviceStreamProcessor;
import com.gj.kafka.devices.DevicesProducer;
import com.gj.kafka.model.City;
import com.gj.kafka.model.CityAggregation;
import com.gj.kafka.model.RSSI;
import com.gj.kafka.producer.BeaconProducer;
import com.gj.kafka.producer.CityDataProducer;
import com.gj.kafka.producer.RSSIProducer;
import com.gj.kafka.streams.MovieStream;
import com.gj.kafka.streams.aggregates.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import freemarker.template.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class App implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    @Override
    public void run(String... args) throws Exception{

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
        if (action.equalsIgnoreCase("testproducer")) {
            TestProducer.produce(broker, topic,TestProducer.loadData());
        }
        if (action.equalsIgnoreCase("rssiproducer")) {
            runRSSIProducer(broker, topic);
        }
        if (action.equalsIgnoreCase("devicesproducer")) {
            DevicesProducer.runProducer(broker, topic);
        }
        if (action.equalsIgnoreCase("beaconproducer")) {
            BeaconProducer.produceBeaconData(broker, topic);
        }
        if (action.equalsIgnoreCase("beaconconsumer")) {
            BeaconConsumer.comsume(broker, topic,grpName);
        }
        if (action.equalsIgnoreCase("beaconstream")) {
            RSSIStream.getBeaconsData();
        }
        if (action.equalsIgnoreCase("consumer")) {
           /* if (topic != null && topic.equalsIgnoreCase("population")) {
                runPolutionConsumer(grpName, broker, topic);
            }*/

            if (topic != null && topic.equalsIgnoreCase("population")) {
                runPolutionConsumer(grpName, broker, topic);
            }

            if (topic != null && (topic.equalsIgnoreCase("cityinfo") || topic.equalsIgnoreCase("cityinfo1"))) {
              //  runCityConsumer(grpName, broker, topic);
                //CityDataConsumer.seekDataUsingStartOffset(grpName, broker, topic,210L,0);
                //seekDataUsingStartTimeStamp

                CityDataConsumer.seekDataUsingStartTimeStamp(grpName, broker, topic,1730779776164L,0);
            }

            if (topic != null && topic.equalsIgnoreCase("rssi")) {
                runRSSIConsumer(grpName, broker, topic);
            }

            if (topic != null && topic.equalsIgnoreCase("1840034016")) {
                runCityConsumer(grpName, broker, topic);
            }
            if (topic != null && topic.equalsIgnoreCase("1840020491")) {
                runCityConsumer(grpName, broker, topic);
            }
            if (topic != null && topic.equalsIgnoreCase("1840000494")) {
                runCityConsumer(grpName, broker, topic);
            }
        }
        if (action.equalsIgnoreCase("stream")) {
            Aggregation.streamTotalPopulationPerState();
        }

        if (action.equalsIgnoreCase("tempStream")) {
            RecordChangesAggregation.streamTotalPopulationPerState();
        }

        if (action.equalsIgnoreCase("filteringSteam")) {
            FilteringSteam.processAndSendToDynamicTopic(broker);
        }

        if (action.equalsIgnoreCase("filteringSteam2")) {
            FilteringSteam.processAndSendToDynamicTopic_flatmap(broker);
        }
        if (action.equalsIgnoreCase("moviestream")) {
            MovieStream.movieStream();
        }

        if (action.equalsIgnoreCase("populationaggregation")) {
            PopulationAggregationStream.cityPopulationAggregation();
        }

        if (action.equalsIgnoreCase("rssistream")) {
            RSSIStream.rssiOrdered();
        }
        if (action.equalsIgnoreCase("cityFilteringStream")) {
            FilteringSteam.filterByCityName(broker,topic,"Chicago");
        }
        if (action.equalsIgnoreCase("deviceStream")) {
            DeviceStreamProcessor.topologyStream();
        }
    }
    public static String[] beacons=new String[]{"B1","B2","B3","B4","B5"};
    public static String[] hubs=new String[]{"H1","H2","H3"};
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    private void runRSSIProducer(String broker, String topic) {
        List<RSSI> list=new ArrayList<>();
        for(int i=0;i<1000;i++) {

            String beacon = beacons[getRandomNumber(0, beacons.length)];
            String hub = hubs[getRandomNumber(0, hubs.length)];
            int rssi = getRandomNumber(-20, -110);
            RSSI rssi1=new RSSI();
            rssi1.setRssi(rssi);
            rssi1.setHub(hub);
            rssi1.setBeacon(beacon);
            list.add(rssi1);
        }


      /*  list.stream().forEach(city -> {
                    System.out.println("City Name :" + city.getCity() + " Message Order: " + city.getRanking() + "Temp :" + city.getTemp());
                }
        );*/
        RSSIProducer.produce(broker, topic, list);
    }

    static void runCityProducer(String broker, String topic) {
        List<City> list = CityDataProducer.loadData();

        // list.addAll(list);
        //  list.addAll(list);
        // list.addAll(list);
        System.out.println("List size: " + list.size());
        System.out.println("Topic name: " + topic);
        list.stream().forEach(city -> {
                    System.out.println("City Name :" + city.getCity() + " Message Order: " + city.getRanking() + "Temp :" + city.getTemp());
                }
        );
        CityDataProducer.produce(broker, topic, list);
    }

    static void runCityConsumer(String grpName, String broker, String topic) {
        List<City> list = CityDataConsumer.consumeData(grpName, broker, topic);
       /* list.stream().forEach(record -> {
            System.out.println("Key :" + record.getKey() + " Value :" + record.toString());
        });*/

    }

    static void runRSSIConsumer(String grpName, String broker, String topic) {
        List<RSSI> list = RSSIDataConsumer.consumeData(grpName, broker, topic);
        list.stream().forEach(record -> {
            System.out.println(" Value :" + record.toString());
        });

    }
    static void runPolutionConsumer(String grpName, String broker, String topic) {
        List<CityAggregation> list = PopulationConsumer.consumeData(grpName, broker, topic);
        list.stream().forEach(record -> {
            // System.out.println("Key :" + record.getKey() + " Value :" + record.toString());
        });

    }



}
