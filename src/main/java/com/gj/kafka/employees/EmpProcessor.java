package com.gj.kafka.employees;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmpProcessor implements Processor<String,String,String,String> {
private KeyValueStore<String, String> kvStore;
private KeyValueStore<String, String> cycleStore;
private ProcessorContext<String,String> context; 
private String cityName;

private static final Logger logger = LoggerFactory.getLogger(EmpProcessor.class);
int count=0;

//add  default constructors
 public EmpProcessor(String cityName) {
     this.cityName = cityName;
 }

 public EmpProcessor() {
     this.cityName = "Unknown";
 }

@Override
public void init(ProcessorContext<String, String> context) {
    //Processor.super.init(context);
    this.context = context;
    logger.debug("EmpDumpProcessor reached here");
    cycleStore=context.getStateStore(cityName.replace(" ","_").toLowerCase());

}


    public void processNode(JsonNode node,Record<String, String> record) {

        logger.debug("Emp Dump came here1"  + Thread.currentThread().getName());
        String deviceName = node.get("deviceName").asText();
        String tagName = node.get("tagData").get(0).get("tagName").asText();
        logger.debug("Tag Dump came here2");
        ObjectMapper mapper = new ObjectMapper();
        if(tagName!=null && tagName.equalsIgnoreCase("HellerStandard/PartNumberOp")){

            logger.debug("EmpDumpProcessor came here3");
            this.context.forward(new Record<>(record.key(), record.value(), record.timestamp()));

        }

    }

@Override
public void process(Record<String, String> record) {
   ObjectMapper mapper = new ObjectMapper();
    try {
        JsonNode map = mapper.readValue(record.value(), JsonNode.class);
        logger.debug("Emp Processing record GJ2:" + map.toString());
       // processNode(map,record);

//        if(map.get("Age").asInt()>100){
//                logger.debug("Invalid Age found for EmployeeID:  Age: " + map.get("Age").asInt());
//              throw new Exception("Invalid Age found for EmployeeID:  Age: " + map.get("Age").asInt());
//        }
        if(map.get("City").asText().equalsIgnoreCase(cityName)){
            this.context.forward(new Record<>(record.key(), record.value(), record.timestamp()));
        }
       Thread.sleep(100);  //assme processing needs
    } catch (Exception e) {
        e.printStackTrace();
        //throw new RuntimeException("Error in processing record in EmpDumpProcessor", e);
    }

}

@Override
public void close() {
    logger.debug("EmpDumpProcessor came in close");
}


}
