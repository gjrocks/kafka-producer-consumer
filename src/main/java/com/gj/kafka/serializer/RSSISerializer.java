package com.gj.kafka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.gj.kafka.model.RSSI;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class RSSISerializer implements Serializer<RSSI> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, RSSI data) {

        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception exception) {
            System.out.println("Error in serializing object" + data);
        }
        return retVal;
    }

    @Override
    public void close() {

    }
}
