package com.gj.kafka.serdes;

import com.gj.kafka.deserializer.*;
import com.gj.kafka.model.City;
import com.gj.kafka.model.CityAggregation;
import com.gj.kafka.model.RSSI;
import com.gj.kafka.serializer.*;
import com.gj.kafka.streams.aggregates.BeaconsAggregations;
import com.gj.kafka.streams.aggregates.RSSIAggregation;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import java.util.HashMap;
import java.util.Map;

public final class CustomSerdesFactory {

    public static Serde<City> citySerde() {
        return Serdes.serdeFrom(new CitySerializer(), new CityDeserializer());
    }
    public static Serde<CityAggregation> cityAggregationSerde() {
        return Serdes.serdeFrom(new CityAggregateSerializer(), new CityAggregateDeserializer());
    }

    public static Serde<RSSI> rssiSerde() {
        return Serdes.serdeFrom(new RSSISerializer(), new RSSIDeserializer());
    }

    public static Serde<RSSIAggregation> rssiAggrSerde() {
        return Serdes.serdeFrom(new RSSIAggreSerializer(), new RSSIAggrDeserializer());
    }

    static final class RSSIAggregationSerde extends Serdes.WrapperSerde<RSSIAggregation> {
        RSSIAggregationSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }


    public static Serde<RSSIAggregation> rSSIAggregationSerde() {
        RSSIAggregationSerde serde = new RSSIAggregationSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, RSSIAggregation.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }

    static final class BeaconsAggregationSerde extends Serdes.WrapperSerde<BeaconsAggregations> {
        BeaconsAggregationSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }
    public static Serde<BeaconsAggregations> beaconAggregationSerde() {
        BeaconsAggregationSerde serde = new BeaconsAggregationSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, BeaconsAggregations.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }
}
