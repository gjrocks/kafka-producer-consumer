package com.gj.kafka.streams.aggregates;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorSupplier;
import org.apache.kafka.streams.state.StoreBuilder;

import java.util.Set;

public class ProcessSupplierImpl implements ProcessorSupplier<String, String, String, String> {


    @Override
    public Processor<String, String, String, String> get() {
        return null;
    }

    @Override
    public Set<StoreBuilder<?>> stores() {
        return ProcessorSupplier.super.stores();
    }
}
