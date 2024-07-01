package com.gj.kafka.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.gj.kafka.model.RSSI;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class RSSIDeserializer implements Deserializer<RSSI> {
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public RSSI deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		RSSI object = null;
		try {
			object = mapper.readValue(data, RSSI.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}
}
