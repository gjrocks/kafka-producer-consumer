package com.gj.kafka.serializer;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.storage.Converter;
import org.apache.kafka.connect.storage.ConverterType;
import org.apache.kafka.connect.storage.HeaderConverter;

import java.io.IOException;
import java.util.Map;

public class CityConvertor implements Converter, HeaderConverter{
    @Override
    public void close() throws IOException {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] fromConnectData(String s, Schema schema, Object o) {
        return new byte[0];
    }

    @Override
    public byte[] fromConnectData(String topic, Headers headers, Schema schema, Object value) {
        return Converter.super.fromConnectData(topic, headers, schema, value);
    }

    @Override
    public SchemaAndValue toConnectData(String s, byte[] bytes) {
        return null;
    }

    @Override
    public SchemaAndValue toConnectData(String topic, Headers headers, byte[] value) {
        return Converter.super.toConnectData(topic, headers, value);
    }

    @Override
    public ConfigDef config() {
        return Converter.super.config();
    }

    @Override
    public SchemaAndValue toConnectHeader(String s, String s1, byte[] bytes) {
        return null;
    }

    @Override
    public byte[] fromConnectHeader(String s, String s1, Schema schema, Object o) {
        return new byte[0];
    }
}
