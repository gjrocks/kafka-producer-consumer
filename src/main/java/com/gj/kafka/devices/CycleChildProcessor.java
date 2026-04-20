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

public class CycleChildProcessor implements Processor<String,String,String,String> {
private KeyValueStore<String, String> kvStore;
private KeyValueStore<String, String> cycleStore;
private ProcessorContext<String,String> context;
int count=0;
@Override
public void init(ProcessorContext<String, String> context) {
    //Processor.super.init(context);
    this.context = context;
    System.out.println("CycleChildProcessor reached here");

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
   // kvStore = context.getStateStore("device-store");
    //cycleStore=context.getStateStore("cycle-store");
}


    public void processNode(JsonNode node,Record<String, String> record) {

        System.out.println("came here1" +  Thread.currentThread().getName());
        String deviceName = node.get("deviceName").asText();
        String tagName = node.get("tagData").get(0).get("tagName").asText();
        System.out.println("came here2");
        ObjectMapper mapper = new ObjectMapper();
        if(tagName!=null && tagName.equalsIgnoreCase("HellerStandard/PartNumberOp")){
            ObjectNode nd= mapper.createObjectNode()
                    .put("deviceName", deviceName)
                    .put("tagName", tagName)
                    .put("timestamp", node.get("tagData").get(0).get("timestamp").asLong());
            nd.put("programName", node.get("tagData").get(0).get("value").asText());

            kvStore.put(deviceName+"_Program", nd.toString());
            System.out.println("came here3");

        }
        if(tagName!=null && tagName.equalsIgnoreCase("HellerStandard/PartCounterTotal")){
            String val=kvStore.get(deviceName+"_Cycle")== null ? "" : kvStore.get(deviceName+"_Cycle");
            System.out.println("came here4");
            try {
                JsonNode nd= mapper.readTree(val);
                if(nd==null){
                    nd=mapper.createObjectNode();
                    System.out.println("came here5");
                }

                if( (nd!=null && nd.get("cycleDataStartTime")==null)){
                    System.out.println("came here6");
                    ((ObjectNode) nd).put("cycleDataStartTime", node.get("tagData").get(0).get("timestamp").asLong());
                    ((ObjectNode) nd).put("cycleStartDataCount", node.get("tagData").get(0).get("value").asText());
                    kvStore.put(deviceName+"_Cycle", nd.toString());
                }
                else{
                    System.out.println("came here7");
                    long endTime = node.get("tagData").get(0).get("timestamp").asLong();
                    String endCountStr = node.get("tagData").get(0).get("value").asText();

                    long start=  nd.get("cycleDataStartTime").asLong();
                    int startCount=  nd.get("cycleStartDataCount").asText()!= null ? Integer.parseInt(nd.get("cycleStartDataCount").asText()) : 0;
                    int endCount = endCountStr != null ? Integer.parseInt(endCountStr) : 0;
                    long cycleTime = endTime - start;
                    int cycleCount = endCount - startCount;
                    System.out.println("start :" + start + " end :" + endTime + " cycleTime :" + cycleTime + " startCount :" + startCount + " endCount :" + endCount + " cycleCount :" + cycleCount);
                    if(cycleCount>0) {
                        System.out.println("came here8");
                        StringBuilder builder = new StringBuilder();
                        builder.append(deviceName);
                        builder.append(",");
                        builder.append(new Date(start));
                        builder.append(",");
                        builder.append(new Date(endTime));
                        builder.append(",");
                        builder.append(cycleTime);
                        builder.append(",");
                        builder.append(startCount);
                        builder.append(",");
                        builder.append(endCount);
                        builder.append(",");
                        builder.append(cycleCount);

                       // cycleTimes.add(builder.toString());
                        //cycleStore.put(deviceName+"_Cycle", builder.toString());
                        ((ObjectNode) nd).put("cycleDataStartTime", node.get("tagData").get(0).get("timestamp").asLong());
                        ((ObjectNode) nd).put("cycleStartDataCount", node.get("tagData").get(0).get("value").asText());
                        kvStore.put(deviceName+"_Cycle", nd.toString());
                        System.out.println("forwarding cycle :" + builder.toString());
                        this.context.forward(new Record<>(deviceName+"_Cycle", builder.toString(), record.timestamp()));
                    }
                }


            } catch (Exception e) {

                e.printStackTrace();
            }


        }

    }

@Override
public void process(Record<String, String> record) {

    //kvStore.put(record.key(), record.value());

    ObjectMapper mapper = new ObjectMapper();
    try {
       // JsonNode map = mapper.readValue(record.value(), JsonNode.class);
       // System.out.println("CycleChildProcessor Processing and forwarding record record :" + map.toString());
        //processNode(map,record);
        System.out.println("forwarding in child Processor");
        this.context.forward(new Record<>(record.key(), record.value(), record.timestamp()),"Ops");

    } catch (Exception e) {
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
