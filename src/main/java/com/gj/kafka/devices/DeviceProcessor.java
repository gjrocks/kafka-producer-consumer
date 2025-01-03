package com.gj.kafka.devices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceProcessor implements Processor<String,String,String,String> {
    private KeyValueStore<String, String> kvStore;

    @Override
    public void init(ProcessorContext<String, String> context) {
        //Processor.super.init(context);

        context.schedule(Duration.ofSeconds(1), PunctuationType.STREAM_TIME, timestamp -> {
            try (final KeyValueIterator<String, String> iter = kvStore.all()) {
                while (iter.hasNext()) {
                    final KeyValue<String, String> entry = iter.next();
                    context.forward(new Record<>(entry.key, entry.value.toString(), timestamp));
                }
            }
        });
        kvStore = context.getStateStore("device-store");
    }

    @Override
    public void process(Record<String, String> record) {
        System.out.println("Key :" +record.key() + " Value :" + record.value());
        kvStore.put(record.key(), record.value());

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode map = mapper.readValue(record.value(), JsonNode.class);
            if(map!=null && map.get("tags").has("ProgramName")){

                String deviceName=map.get("deviceName").asText();
                if(kvStore.get(deviceName)!=null){
                    CycleTimeEvent cycleTimeEvent=new CycleTimeEvent();

                    JsonNode existingNode=mapper.readValue(kvStore.get(deviceName), JsonNode.class);
                    cycleTimeEvent.setDeviceName(deviceName);

                    JsonNode existingTags=existingNode.get("tags").get("ProgramName");
                    JsonNode existingProgramName=existingTags.get("tagValue");
                    JsonNode existingTime=existingTags.get("timestamp");
                    cycleTimeEvent.setPreviousProgramStartTime(existingTime.asLong());
                    cycleTimeEvent.setPreviousProgramName(existingProgramName.asText());
                    cycleTimeEvent.setCurrentProgramStartTime(map.get("tags").get("ProgramName").get("timestamp").asLong());
                    cycleTimeEvent.setProgramName(map.get("tags").get("ProgramName").get("tagValue").asText());
                    cycleTimeEvent.setCurrentProgramName(map.get("tags").get("ProgramName").get("tagValue").asText());
                    cycleTimeEvent.setCycleTime((cycleTimeEvent.getCurrentProgramStartTime()-cycleTimeEvent.getPreviousProgramStartTime())/(1000));
                   // cycleTimeEvents.add(cycleTimeEvent);

                    kvStore.put(deviceName,record.value()); //setting up new raw event as previous event
                }else {
                    kvStore.put(deviceName, record.value());
                }
            }
            //System.out.println(map);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
       // Processor.super.close();
    }

    public void deriveCycleTime(List<String> events) throws Exception{
        Map<String, JsonNode> externalContext=new HashMap<>();
        List<CycleTimeEvent> cycleTimeEvents=new ArrayList<>();
        events.stream().forEach(event->{
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode map = mapper.readValue(event, JsonNode.class);
                if(map!=null && map.get("tags").has("ProgramName")){
                    //System.out.println(map);
                    String deviceName=map.get("deviceName").asText();
                    if(externalContext.get(deviceName)!=null){
                        CycleTimeEvent cycleTimeEvent=new CycleTimeEvent();
                        JsonNode existingNode=externalContext.get(deviceName);
                        cycleTimeEvent.setDeviceName(deviceName);

                        JsonNode existingTags=existingNode.get("tags").get("ProgramName");
                        JsonNode existingProgramName=existingTags.get("tagValue");
                        JsonNode existingTime=existingTags.get("timestamp");
                        cycleTimeEvent.setPreviousProgramStartTime(existingTime.asLong());
                        cycleTimeEvent.setPreviousProgramName(existingProgramName.asText());
                        cycleTimeEvent.setCurrentProgramStartTime(map.get("tags").get("ProgramName").get("timestamp").asLong());
                        cycleTimeEvent.setProgramName(map.get("tags").get("ProgramName").get("tagValue").asText());
                        cycleTimeEvent.setCurrentProgramName(map.get("tags").get("ProgramName").get("tagValue").asText());
                        cycleTimeEvent.setCycleTime((cycleTimeEvent.getCurrentProgramStartTime()-cycleTimeEvent.getPreviousProgramStartTime())/(1000));
                        cycleTimeEvents.add(cycleTimeEvent);

                        externalContext.put(deviceName,map); //setting up new raw event as previous event
                    }else {
                        externalContext.put(deviceName, map);
                    }
                }
                //System.out.println(map);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        cycleTimeEvents.stream().forEach(System.out::println);
    }
}
