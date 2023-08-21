package com.gj.kafka;

import com.gj.kafka.consumer.CityDataConsumer;
import com.gj.kafka.consumer.EmployeeDataConsumer;
import com.gj.kafka.consumer.PopulationConsumer;
import com.gj.kafka.model.City;
import com.gj.kafka.model.CityAggregation;
import com.gj.kafka.model.Employee;
import com.gj.kafka.producer.CityDataProducer;
import com.gj.kafka.producer.EmployeeDataProducer;
import com.gj.kafka.streams.MovieStream;
import com.gj.kafka.streams.aggregates.Aggregation;
import com.gj.kafka.streams.aggregates.FilteringSteam;
import com.gj.kafka.streams.aggregates.PopulationAggregationStream;
import com.gj.kafka.streams.aggregates.RecordChangesAggregation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
          //  runCityProducer(broker, topic);
            runEmployeeProducer(broker,topic);
        }
        if (action.equalsIgnoreCase("consumer")) {
            if (topic != null && topic.equalsIgnoreCase("employee")) {
                runEmployeeConsumer(grpName, broker, topic);
            }
            if (topic != null && topic.equalsIgnoreCase("employee1")) {
                runEmployeeConsumer(grpName, broker, topic);
            }
            if (topic != null && topic.equalsIgnoreCase("population")) {
                runPolutionConsumer(grpName, broker, topic);
            }

            if (topic != null && topic.equalsIgnoreCase("cityinfo")) {
                runCityConsumer(grpName, broker, topic);
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
            FilteringSteam.filterAndSendToTopic(broker);
        }
        if (action.equalsIgnoreCase("moviestream")) {
            MovieStream.movieStream();
        }

        if (action.equalsIgnoreCase("populationaggregation")) {
            PopulationAggregationStream.cityPopulationAggregation();
        }
    }

    private void runEmployeeProducer(String broker, String topic) {
        List<Employee> list=new ArrayList<>();
        Employee bob = Employee.newBuilder().setAge(35)
                .setFName("Raj")
                .setLName("Jones")
                .setPhoneNumber(1)
                .setEmpId("1-Version-3")
                .build();
        Employee kevin = Employee.newBuilder().setAge(35)
                .setFName("Keven")
                .setLName("Jones")
                .setPhoneNumber(2)
                .setEmpId("2-Version-3")
                .build();
        Employee george = Employee.newBuilder().setAge(35)
                .setFName("George")
                .setLName("Jones")
                .setPhoneNumber(3)
                .setEmpId("3-Version-3")
                .build();
        list.add(bob);
        list.add(george);
        list.add(kevin);
        EmployeeDataProducer.produce(broker, topic, list);
    }

    static void runCityProducer(String broker, String topic) {
        List<City> list = CityDataProducer.loadData();

        // list.addAll(list);
        //  list.addAll(list);
        // list.addAll(list);
        System.out.println("List size: " + list.size());

        list.stream().forEach(city -> {
                    System.out.println("City Name :" + city.getCity() + " Message Order: " + city.getRanking() + "Temp :" + city.getTemp());
                }
        );
        CityDataProducer.produce(broker, topic, list);
    }

    static void runEmployeeConsumer(String grpName, String broker, String topic) {
        List<Employee> list = EmployeeDataConsumer.consumeData(grpName, broker, topic);
        list.stream().forEach(record -> {
            if(record!=null)
            System.out.println("Key :" + record.getFName() + " Value :" + record.toString());
            else
                System.out.println("Record itself is null");
        });

    }

    static void runCityConsumer(String grpName, String broker, String topic) {
        List<City> list = CityDataConsumer.consumeData(grpName, broker, topic);
        list.stream().forEach(record -> {
            System.out.println("Key :" + record.getKey() + " Value :" + record.toString());
        });

    }

    static void runPolutionConsumer(String grpName, String broker, String topic) {
        List<CityAggregation> list = PopulationConsumer.consumeData(grpName, broker, topic);
        list.stream().forEach(record -> {
            // System.out.println("Key :" + record.getKey() + " Value :" + record.toString());
        });

    }



}
