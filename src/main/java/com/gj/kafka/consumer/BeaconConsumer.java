package com.gj.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.model.Beacon;
import com.gj.kafka.model.Payload;

import java.util.ArrayList;
import java.util.List;

public class BeaconConsumer {

    public static List<Beacon> comsume(String broker, String topic,String grpName) throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        List<Beacon> beaconList=new ArrayList<>();
          //  JsonConsumer consumer=new JsonConsumer();
       List<Payload> payloadList= JsonConsumer.comsume(broker,topic,grpName);
        for (Payload payload:payloadList) {
            try {
                System.out.println("Received message :" + payload.getPayload());
                Beacon message = mapper.readValue(payload.getPayload(), Beacon.class);
                beaconList.add(message);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return beaconList;
    }
}
