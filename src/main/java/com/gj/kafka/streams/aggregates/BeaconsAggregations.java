package com.gj.kafka.streams.aggregates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "beacons"
})
public class BeaconsAggregations {
    private ObjectMapper objectMapper = new ObjectMapper();
    List<String> beacons;
    public BeaconsAggregations(){
        beacons=new ArrayList<>();
    }

    public void add(String beacon){
        beacons.add(beacon);
    }

    @JsonProperty("beacons")
    public List<String> getBeacons() throws Exception {
        return beacons;
    }

    @JsonProperty("beacons")
    public void setBeacons(List<String> list)throws Exception{
        /*String[] top3 = objectMapper.readValue(beacons, String[].class);
        for(String i:top3){
            add(i);
        }*/
        this.beacons = list;
    }
}
