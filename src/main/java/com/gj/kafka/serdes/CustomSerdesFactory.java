package com.gj.kafka.serdes;

import com.gj.kafka.deserializer.CityAggregateDeserializer;
import com.gj.kafka.deserializer.CityDeserializer;
import com.gj.kafka.model.City;
import com.gj.kafka.model.CityAggregation;
import com.gj.kafka.serializer.CityAggregateSerializer;
import com.gj.kafka.serializer.CitySerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public final class CustomSerdesFactory {

    public static Serde<City> citySerde() {
        return Serdes.serdeFrom(new CitySerializer(), new CityDeserializer());
    }
    public static Serde<CityAggregation> cityAggregationSerde() {
        return Serdes.serdeFrom(new CityAggregateSerializer(), new CityAggregateDeserializer());
    }
}
