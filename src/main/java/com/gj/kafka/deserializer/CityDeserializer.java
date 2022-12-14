package com.gj.kafka.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.model.City;
import com.gj.kafka.pojo.CustomObject;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class CityDeserializer implements Deserializer<City> {
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public City deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		City object = null;
		try {
			object = mapper.readValue(data, City.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}
}