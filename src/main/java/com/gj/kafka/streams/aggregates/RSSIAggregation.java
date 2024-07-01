package com.gj.kafka.streams.aggregates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.model.RSSI;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "topRssi"
})
public class RSSIAggregation implements Serializable {
    private ObjectMapper objectMapper = new ObjectMapper();
    public RSSIAggregation() {
        this.rssiSet = new TreeSet<>(new Comparator<RSSI>() {
            @Override
            public int compare(RSSI s1, RSSI s2) {
                return s2.getRssi().compareTo(s1.getRssi());
            }
        });
    }

    public void add(RSSI rssi){
        this.rssiSet.add(rssi);
    }

    public void remove(RSSI rssi){
        this.rssiSet.remove(rssi);
    }


    private TreeSet<RSSI> rssiSet;


    public TreeSet<RSSI> getRssiSet() {
        return rssiSet;
    }

    public void print(){
        rssiSet.stream().forEach(System.out::println);
    }

    @JsonProperty("topRssi")
    public String getTopRssi() throws JsonProcessingException {
        return objectMapper.writeValueAsString(rssiSet);
    }
    @JsonProperty("topRssi")
    public void setTop3Sorted(String topRSSI) throws IOException {
        RSSI[] top3 = objectMapper.readValue(topRSSI, RSSI[].class);
        for(RSSI i:top3){
            add(i);
        }
    }
}
