package com.gj;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

public class CycleTimeCalculator {

    @Test
    public void testDataPump() {
        // This is a placeholder for the data pump test.
        // You can implement your data pump logic here.
        System.out.println("Data Pump Test Executed");
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<JsonNode> asList = mapper.readValue(new File("/Users/cqktss0/Desktop/codeBase/kafka-work/kafka-producer-consumer/src/main/resources/cycl.json"), new TypeReference<List<JsonNode>>() {
            });
            // System.out.println(asList.size());
            for (JsonNode node : asList) {
                //System.out.println(node.toString());

                node.get("tagData").iterator().forEachRemaining(
                        nd-> {
                            if(nd.get("tagName").asText().equalsIgnoreCase("HellerStandard/PartNumberOp")) {
                                long timestamp = nd.get("timestamp").asLong();

                                Date date = new Date(timestamp);

                                System.out.println("TimeStamp: " + date.toString());
                                long newT=timestamp + (randomMin() * 60 * 1000);
                                 //nd.put("timeStamp", newT);
                                System.out.println(new Date(newT));
                            }


                        }
                        );
               // System.out.println((com.fasterxml.jackson.databind.node.ArrayNode)node.get("tagData"));
                //System.out.println(node.get("tagData").get("timeStamp").asLong());
            }

        }catch (Exception e){e.printStackTrace();}

    }

    private long randomMin() {
        //random number between 1 and 6
        return (long) (Math.random() * 6 + 1);
    }


    static Map<String, String> deviceCycleTimes = new HashMap<>();
    static  List<String> cycleTimes = new ArrayList<>();
    public void processNode(JsonNode node) {
        // This method will contain the logic to process each node
        // and calculate cycle time based on the device data and tag information.
        // Implementation details will depend on the specific requirements
        // and data structure used in the application.
        String deviceName = node.get("deviceName").asText();
        String tagName = node.get("tagData").get(0).get("tagName").asText();
        ObjectMapper mapper = new ObjectMapper();
        if(tagName!=null && tagName.equalsIgnoreCase("HellerStandard/PartNumberOp")){
            ObjectNode nd= mapper.createObjectNode()
                    .put("deviceName", deviceName)
                    .put("tagName", tagName)
                    .put("timestamp", node.get("tagData").get(0).get("timestamp").asLong());
            nd.put("programName", node.get("tagData").get(0).get("value").asText());
            // Add logic to calculate cycle time based on the value and timestamp
            // For example, you might want to calculate the difference between timestamps
            // or perform some aggregation based on the value.
            deviceCycleTimes.put(deviceName, nd.toString());
            // Here you can add the calculated cycle time to a list or map for further processing
            // For example:
            // cycleTimes.add("CycleTime for " + deviceName + ": " + calculatedCycleTime);
        }
        if(tagName!=null && tagName.equalsIgnoreCase("HellerStandard/PartCounterTotal")){
            String val=  deviceCycleTimes.get(deviceName);
            try {
                JsonNode nd= mapper.readTree(val);
                if(nd!=null && nd.get("cycleDataStartTime")==null){
                    ((ObjectNode) nd).put("cycleDataStartTime", node.get("tagData").get(0).get("timestamp").asLong());
                    ((ObjectNode) nd).put("cycleStartDataCount", node.get("tagData").get(0).get("value").asText());
                    deviceCycleTimes.put(deviceName, nd.toString());
                }
                else{
                    long endTime = node.get("tagData").get(0).get("timestamp").asLong();
                    String endCountStr = node.get("tagData").get(0).get("value").asText();

                    long start=  nd.get("cycleDataStartTime").asLong();
                    int startCount=  nd.get("cycleStartDataCount").asText()!= null ? Integer.parseInt(nd.get("cycleStartDataCount").asText()) : 0;
//                    long end= nd.get("cycleDataEndTime").asLong();
//                    int endCount=  nd.get("cycleEndDataCount").asText()!= null ? Integer.parseInt(nd.get("cycleEndDataCount").asText()) : 0;
                    int endCount = endCountStr != null ? Integer.parseInt(endCountStr) : 0;
                    long cycleTime = endTime - start;
                    int cycleCount = endCount - startCount;

                    if(cycleCount>0) {
                        ((ObjectNode) nd).put("cycleDataEndTime", endTime);
                        ((ObjectNode) nd).put("cycleEndDataCount", endCountStr);
                        ((ObjectNode) nd).put("cycleTime", cycleTime);
                        ((ObjectNode) nd).put("cycleCount", cycleCount);
                        // System.out.println("Device: " + deviceName + ", Cycle Time: " + cycleTime + " ms, Cycle Count: " + cycleCount);
                        // Here you can add the
                        cycleTimes.add(nd.toString());

                        ((ObjectNode) nd).put("cycleDataStartTime", node.get("tagData").get(0).get("timestamp").asLong());
                        ((ObjectNode) nd).put("cycleStartDataCount", node.get("tagData").get(0).get("value").asText());
                        deviceCycleTimes.put(deviceName, nd.toString());
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
            }


        }

    }

    @Test
    public void calculateCycleTime() {
        // This method will contain the logic to calculate cycle time
        // based on the device data and tag information.
        // Implementation details will depend on the specific requirements
        // and data structure used in the application.

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<JsonNode> asList = mapper.readValue(new File("src/main/resources/cycles.json"), new TypeReference<List<JsonNode>>() {
            });
            System.out.println(asList.size());
            //List<String> cycleTimes = new ArrayList<>();
            for (JsonNode node : asList) {
                processNode(node);
            }
            int cycles=0;
//           for(int i=0;i<cycleTimes.size();i++) {
//               try {
//                     String node = cycleTimes.get(i);
//                   JsonNode nd = mapper.readTree(node);
//
//                   long timestamp = nd.get("cycleDataStartTime").asLong();
//                   int cycleCount = nd.get("cycleCount").asInt();
//                   if(cycleCount>0) {
//                       cycles++;
//                   }
//                   System.out.println(new Date(timestamp)+","+cycles);
//                   //System.out.println("Device: " + deviceName + ", Tag: " + tagName + ", Timestamp: " + timestamp + ", Value: " + value);
//               } catch (Exception e) {
//                   e.printStackTrace();
//               }
//
//           }CycleTimeCalculator

            cycleTimes.forEach(System.out::println);
            // System.out.println("Device: " + deviceName + ", Tag: " + tagName + ", Timestamp: " + timestamp + ", Value: " + value);

            // Add calculated cycle time to the list (dummy value for now)
            //cycleTimes.add("CycleTime for " + deviceName + ": " + value);
            //}
            // Print all calculated cycle times
            //cycleTimes.forEach(System.out::println);

        }catch (Exception e){e.printStackTrace();}
    }
}
