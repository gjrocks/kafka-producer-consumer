package com.gj.kafka.streams.aggregates;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

public class MyRunnable implements Runnable {

    KafkaStreams streams;

    public MyRunnable(KafkaStreams streams){
      this.streams=streams;
    }

    @Override
    public void run() {
        try{
            while(true){
            if(streams.state() == KafkaStreams.State.RUNNING) {
                ReadOnlyKeyValueStore<String, BeaconsAggregations> keyValueStore =  streams.store(StoreQueryParameters.fromNameAndType("CountsKeyValueStore", QueryableStoreTypes.keyValueStore()));

                //System.out.println("My Runnable count for B1:" + keyValueStore.get("B1"));
                //return customer;
                Thread.sleep(3000);
            }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
