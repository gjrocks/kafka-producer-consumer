package com.gj.kafka.devices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;

import java.io.IOException;
import java.util.*;

public class TagDumpProcessor implements Processor<String,String,String,String> {
private KeyValueStore<String, String> kvStore;
private KeyValueStore<String, String> cycleStore;
private ProcessorContext<String,String> context;
int count=0;
@Override
public void init(ProcessorContext<String, String> context) {
    //Processor.super.init(context);
    this.context = context;
    System.out.println("TagDumpProcessor reached here");

//    context.schedule(Duration.ofSeconds(1), PunctuationType.STREAM_TIME, timestamp -> {
//       // cycleStore.
//        try (final KeyValueIterator<String, String> iter = cycleStore.all()) {
//            while (iter.hasNext()) {
//                count++;
//                final KeyValue<String, String> entry = iter.next();
//                try{
//                    ObjectMapper mapper = new ObjectMapper();
//                    CycleTimeEvent existingNode=mapper.readValue(entry.value.toString(), CycleTimeEvent.class);
//                    if(existingNode.getForwaded()==null || existingNode.getForwaded().equalsIgnoreCase("no")) {
//                        context.forward(new Record<>(entry.key, entry.value.toString(), timestamp));
//                        existingNode.setForwaded("yes");
//                        cycleStore.put(entry.key, mapper.writeValueAsString(existingNode));
//                        System.out.println("Count :" + count);
//                    }
//
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//
//            }
//        }
//    });
    //kvStore = context.getStateStore("device-store");
    cycleStore=context.getStateStore("cycle-store");
    //cycleStore.
}


    public void processNode(JsonNode node,Record<String, String> record) {

        System.out.println("Tag Dump came here1"  + Thread.currentThread().getName());
        String deviceName = node.get("deviceName").asText();
        String tagName = node.get("tagData").get(0).get("tagName").asText();
        System.out.println("Tag Dump came here2");
        ObjectMapper mapper = new ObjectMapper();
        if(tagName!=null && tagName.equalsIgnoreCase("HellerStandard/PartNumberOp")){

            System.out.println("TagDumpProcessor came here3");
            this.context.forward(new Record<>(record.key(), record.value(), record.timestamp()));

        }

    }

@Override
public void process(Record<String, String> record) {

    //kvStore.put(record.key(), record.value());

    ObjectMapper mapper = new ObjectMapper();
    try {
        JsonNode map = mapper.readValue(record.value(), JsonNode.class);
        System.out.println("Processing record :" + map.toString());
        processNode(map,record);
//        if(map!=null && map.get("tags").has("ProgramName")){
//            System.out.println("Key :" +record.key() + " Value :" + record.value());
//            String deviceName=map.get("deviceName").asText();
//            if(kvStore.get(deviceName)!=null){
//                CycleTimeEvent cycleTimeEvent=new CycleTimeEvent();
//
//                JsonNode existingNode=mapper.readValue(kvStore.get(deviceName), JsonNode.class);
//                cycleTimeEvent.setDeviceName(deviceName);
//
//                JsonNode existingTags=existingNode.get("tags").get("ProgramName");
//                JsonNode existingProgramName=existingTags.get("tagValue");
//                JsonNode existingTime=existingTags.get("timestamp");
//                cycleTimeEvent.setPreviousProgramStartTime(existingTime.asLong());
//                cycleTimeEvent.setPreviousProgramName(existingProgramName.asText());
//                cycleTimeEvent.setCurrentProgramStartTime(map.get("tags").get("ProgramName").get("timestamp").asLong());
//                cycleTimeEvent.setProgramName(map.get("tags").get("ProgramName").get("tagValue").asText());
//                cycleTimeEvent.setCurrentProgramName(map.get("tags").get("ProgramName").get("tagValue").asText());
//                cycleTimeEvent.setCycleTime((cycleTimeEvent.getCurrentProgramStartTime()-cycleTimeEvent.getPreviousProgramStartTime())/(1000));
//               // cycleTimeEvents.add(cycleTimeEvent);
//                String keyy=deviceName+UUID.randomUUID();
//                if(cycleStore.get(keyy)!=null){
//                    System.out.println(" we have collision");
//
//                }
//                 System.out.println("Keyy :" +keyy);
//                 cycleStore.put(keyy,mapper.writeValueAsString(cycleTimeEvent));
//
//                kvStore.put(deviceName,record.value()); //setting up new raw event as previous event
//            }else {
//                kvStore.put(deviceName, record.value());
//                //cycleStore.put(deviceName,mapper.writeValueAsString(cycleTimeEvent));
//            }
//        }
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
