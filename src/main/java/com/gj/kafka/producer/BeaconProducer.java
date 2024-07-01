package com.gj.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.model.Beacon;
import com.gj.kafka.model.Payload;

import java.util.ArrayList;
import java.util.List;

public class BeaconProducer {

    public static void produceBeaconData(String broker, String topic) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        List<Payload> list=new ArrayList<>();
        for(int i=0;i<5;i++){
            Beacon message = new Beacon("B" + i);
            message.setManufactuerer("B");
            message.setYearMonthOfManufacturer("999");
            String jsonMessage = mapper.writeValueAsString(message);
            Payload payload=new Payload();
            payload.setPayload(message.getUuid());
            payload.setKey(message.getUuid());
            list.add(payload);
        }
    JsonProducer.produce(broker,topic,list);
    }
}
