package com.gj.kafka.util;

import com.gj.kafka.constants.IKafkaConstants;

public class Util {

    public static Object getBroker(String broker) {
        if(broker==null || broker.trim().isEmpty())
            return IKafkaConstants.KAFKA_BROKERS_ALL;
        if(broker.equalsIgnoreCase("1"))
            return IKafkaConstants.KAFKA_BROKERS_1;
        if(broker.equalsIgnoreCase("2"))
            return IKafkaConstants.KAFKA_BROKERS_2;
        return IKafkaConstants.KAFKA_BROKERS_ALL;
    }
}
