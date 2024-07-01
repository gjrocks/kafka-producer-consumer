package com.gj.kafka.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.model.RSSI;
import com.gj.kafka.streams.aggregates.RSSIAggregation;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class RSSIAggrDeserializer implements Deserializer<RSSIAggregation> {
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public RSSIAggregation deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		RSSIAggregation object = null;
		try {
			object = mapper.readValue(data, RSSIAggregation.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}
}
