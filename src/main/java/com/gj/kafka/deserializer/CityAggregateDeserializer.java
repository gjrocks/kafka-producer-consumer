package com.gj.kafka.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gj.kafka.model.City;
import com.gj.kafka.model.CityAggregation;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CityAggregateDeserializer implements Deserializer<CityAggregation> {
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public CityAggregation deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		CityAggregation object = null;
		try {
			String dataString= StringUtils.newString(data, StandardCharsets.UTF_8.toString());
			object = mapper.readValue(dataString, CityAggregation.class);
			
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}
}