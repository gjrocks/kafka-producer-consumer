package com.gj.kafka.streams.aggregates;

import java.io.Serializable;

public class Payload implements Serializable {


    String destinationTopicName;
    Object payload;

    String className;

    String functionality;
    public Payload(String destinationTopicName, Object payload, String className,String functionality) {
        this.destinationTopicName = destinationTopicName;
        this.payload = payload;
        this.className = className;
        this.functionality=functionality;
    }

    public String getFunctionality() {
        return functionality;
    }

    public void setFunctionality(String functionality) {
        this.functionality = functionality;
    }

    public Payload() {

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDestinationTopicName() {
        return destinationTopicName;
    }

    public void setDestinationTopicName(String destinationTopicName) {
        this.destinationTopicName = destinationTopicName;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }



}
