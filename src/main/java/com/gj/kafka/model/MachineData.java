package com.gj.kafka.model;

public class MachineData {
   //pojo for IOT temperature sensor data
    private String machineId;
    private String temperature;
    private String timestamp;

    private String something;

    private Integer everything;

    //generate getters and setters for the fields
    public String getSomething() {
        return something;
    }
    //generate setters for the fields something and everything
    public void setSomething(String something) {
        this.something = something;
    }
    public void setEverything(Integer everything) {
        this.everything=everything;
    }
    public Integer getEverything() {
        return everything;
    }

    public MachineData(String machineId,String temperature){
        this.machineId=machineId;
        this.temperature=temperature;
    }
    public MachineData(){}
    public MachineData(String machineId,String temperature,String timestamp){
        this.machineId=machineId;
        this.temperature=temperature;
        this.timestamp=timestamp;
    }

    public String getMachineId() {
        return machineId;

    }
    public String getTemperature() {
        return temperature;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setMachineId(String machineId) {

        this.machineId = machineId;
    }
    public void setTemperature(String temperature) {
            this.temperature = temperature;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp=timestamp;
    }
}
