package com.gj.kafka.cert.producers;
import java.io.Serializable;
import java.util.HashMap;
public class EventData  implements Serializable {

    java.util.HashMap<String,String> metaData=new HashMap<>();
    byte[] payload;

    public EventData() {
    }
    //getters and setters
    public HashMap<String, String> getMetaData() {
        return metaData;
    }
    public void setMetaData(HashMap<String, String> metaData) {
        this.metaData = metaData;
    }
    //getters and setters
    public byte[] getPayload() {
        return payload;
    }
    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
    //constructor
    public EventData(HashMap<String, String> metaData, byte[] payload) {
        this.metaData = metaData;
        this.payload = payload;
    }
    //add method to add metadata
    public void addMetaData(String key,String value){
        metaData.put(key,value);
    }
}
