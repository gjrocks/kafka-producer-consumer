package com.gj.kafka.devices;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "deviceName",
        "programName",
        "previousProgramName",
        "getPreviousProgramStartTime",
        "currentProgramName",
        "currentProgramStartTime",
        "cycleTime"
})
public class CycleTimeEvent {
    @JsonProperty("deviceName")
    private String deviceName;
    @JsonProperty("programName")
    private String programName;
    @JsonProperty("previousProgramName")
    private String previousProgramName;
    @JsonProperty("previousProgramStartTime")
    private Long previousProgramStartTime;
    @JsonProperty("currentProgramName")
    private String currentProgramName;
    @JsonProperty("currentProgramStartTime")
    private Long currentProgramStartTime;
    @JsonProperty("cycleTime")
    private Long cycleTime;



    public CycleTimeEvent() {
    }

    @JsonProperty("deviceName")
    public String getDeviceName() {
        return deviceName;
    }

    @JsonProperty("deviceName")
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @JsonProperty("programName")
    public String getProgramName() {
        return programName;
    }

    @JsonProperty("programName")
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    @JsonProperty("previousProgramName")
    public String getPreviousProgramName() {
        return previousProgramName;
    }

    @JsonProperty("previousProgramName")
        public void setPreviousProgramName(String previousProgramName) {
        this.previousProgramName = previousProgramName;
    }

    @JsonProperty("previousProgramStartTime")
    public Long getPreviousProgramStartTime() {
        return previousProgramStartTime;
    }

    @JsonProperty("previousProgramStartTime")
    public void setPreviousProgramStartTime(Long getPreviousProgramStartTime) {
        this.previousProgramStartTime = getPreviousProgramStartTime;
    }

    @JsonProperty("currentProgramName")
    public String getCurrentProgramName() {
        return currentProgramName;
    }

    @JsonProperty("currentProgramName")
    public void setCurrentProgramName(String currentProgramName) {
        this.currentProgramName = currentProgramName;
    }

    @JsonProperty("currentProgramStartTime")
    public Long getCurrentProgramStartTime() {
        return currentProgramStartTime;
    }

    @JsonProperty("currentProgramStartTime")
    public void setCurrentProgramStartTime(Long currentProgramStartTime) {
        this.currentProgramStartTime = currentProgramStartTime;
    }

    @JsonProperty("cycleTime")
    public Long getCycleTime() {
        return cycleTime;
    }

    @JsonProperty("cycleTime")
    public void setCycleTime(Long cycleTime) {
        this.cycleTime = cycleTime;
    }

    @Override
    public String toString() {
        return "CycleTimeEvent{" +
                "deviceName='" + deviceName + '\'' +
                ", programName='" + programName + '\'' +
                ", previousProgramName='" + previousProgramName + '\'' +
                ", previousProgramStartTime=" + previousProgramStartTime +
                ", currentProgramName='" + currentProgramName + '\'' +
                ", currentProgramStartTime=" + currentProgramStartTime +
                ", cycleTime=" + cycleTime +
                '}';
    }
}
