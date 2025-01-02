package com.gj.kafka.devices;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.time.Duration;

public class DeviceProcessor implements Processor<String,String,String,String> {
    private KeyValueStore<String, String> kvStore;

    @Override
    public void init(ProcessorContext<String, String> context) {
        //Processor.super.init(context);

        context.schedule(Duration.ofSeconds(1), PunctuationType.STREAM_TIME, timestamp -> {
            try (final KeyValueIterator<String, String> iter = kvStore.all()) {
                while (iter.hasNext()) {
                    final KeyValue<String, String> entry = iter.next();
                    context.forward(new Record<>(entry.key, entry.value.toString(), timestamp));
                }
            }
        });
        kvStore = context.getStateStore("device-store");
    }

    @Override
    public void process(Record<String, String> record) {
        System.out.println("Key :" +record.key() + " Value :" + record.value());
        kvStore.put(record.key(), record.value());
    }

    @Override
    public void close() {
       // Processor.super.close();
    }
}
