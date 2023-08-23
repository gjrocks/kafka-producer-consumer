package com.gj.kafka.deserializer;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;

import org.apache.avro.Schema;
import org.apache.kafka.common.errors.SerializationException;

public class CustomAvroDeserilizer extends KafkaAvroDeserializer {

    @Override
    public Object deserialize(String s, byte[] bytes) {
        try {

            return deserialize(bytes);

        } catch (SerializationException e) {
            System.out.println("Error in deserialization :" + e.getMessage());
            return null;
        }
    }



    public Object deserialize(String s, byte[] bytes, Schema readerSchema) {
        try {

            System.out.println("Schema type: " + readerSchema.getType());
            return deserialize(bytes, readerSchema);
        } catch (SerializationException e) {
            System.out.println("Error in deserialization :" + e.getMessage());
            return null;
        }
    }
}
