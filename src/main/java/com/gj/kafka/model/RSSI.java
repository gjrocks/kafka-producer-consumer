package com.gj.kafka.model;


import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "beacon",
        "hub",
        "rssi"
})
@Generated("jsonschema2pojo")
public class RSSI implements Serializable,java.lang.Comparable {

    @JsonProperty("beacon")
    private String beacon;
    @JsonProperty("hub")
    private String hub;

    @JsonProperty("rssi")
    public Integer getRssi() {
        return rssi;
    }

    @JsonProperty("rssi")
    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    @JsonProperty("rssi")
    private Integer rssi;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("beacon")
    public String getBeacon() {
        return beacon;
    }

    @JsonProperty("beacon")
    public void setBeacon(String beacon) {
        this.beacon = beacon;
    }

    @JsonProperty("hub")
    public String getHub() {
        return hub;
    }

    @JsonProperty("hub")
    public void setHub(String hub) {
        this.hub = hub;
    }




    @Override
    public String toString() {
        return "RSSI{" +
                "beacon='" + beacon + '\'' +
                ", hub='" + hub + '\'' +
                ", rssi=" + rssi +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        RSSI i=(RSSI)o;
        return i.getRssi().compareTo(this.getRssi());
    }
}
