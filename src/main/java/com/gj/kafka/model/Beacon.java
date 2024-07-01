package com.gj.kafka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "uuid",
        "major",
        "minor",
        "manufactuerer",
        "yearMonthOfManufacturer"
})
@Generated("jsonschema2pojo")
public class Beacon {
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("major")
    private int major;
    @JsonProperty("minor")
    private int minor;
    @JsonProperty("manufactuerer")
    private String manufactuerer;
    @JsonProperty("yearMonthOfManufacturer")
    private String yearMonthOfManufacturer;


    // Default constructor
    public Beacon() {}

    // Constructor with all fields
    public Beacon(String uuid) {
        this.uuid = uuid;

    }

    // Getters and setters
    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }
    @JsonProperty("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    @JsonProperty("major")
    public int getMajor() {
        return major;
    }
    @JsonProperty("major")
    public void setMajor(int major) {
        this.major = major;
    }
    @JsonProperty("minor")
    public int getMinor() {
        return minor;
    }
    @JsonProperty("minor")
    public void setMinor(int minor) {
        this.minor = minor;
    }
    @JsonProperty("manufactuerer")
    public String getManufactuerer() {
        return manufactuerer;
    }
    @JsonProperty("manufactuerer")
    public void setManufactuerer(String manufactuerer) {
        this.manufactuerer = manufactuerer;
    }
    @JsonProperty("yearMonthOfManufacturer")
    public String getYearMonthOfManufacturer() {
        return yearMonthOfManufacturer;
    }
    @JsonProperty("yearMonthOfManufacturer")
    public void setYearMonthOfManufacturer(String yearMonthOfManufacturer) {
        this.yearMonthOfManufacturer = yearMonthOfManufacturer;
    }

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @Override
    public String toString() {
        return "Beacon{" +
                "uuid='" + uuid + '\'' +
                ", major=" + major +
                ", minor=" + minor +
                ", manufactuerer='" + manufactuerer + '\'' +
                ", yearMonthOfManufacturer='" + yearMonthOfManufacturer + '\'' +
                '}';
    }
}
