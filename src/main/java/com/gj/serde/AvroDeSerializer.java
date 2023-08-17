package com.gj.serde;

import com.gj.model.AvroHttpRequest;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class AvroDeSerializer {

    private static Logger logger = LoggerFactory.getLogger(AvroDeSerializer.class);

    public AvroHttpRequest deSerializeAvroHttpRequestJSON(byte[] data) {
        DatumReader<AvroHttpRequest> reader = new SpecificDatumReader<>(AvroHttpRequest.class);
        Decoder decoder = null;
        try {
            decoder = DecoderFactory.get()
                .jsonDecoder(AvroHttpRequest.getClassSchema(), new String(data));
            return reader.read(null, decoder);
        } catch (IOException e) {
            logger.error("Deserialization error" + e.getMessage());
        }
        return null;
    }
    public AvroHttpRequest  deserializeAvroHttpRequestJSONToFile(String fileName) throws Exception{
        File file=new File(fileName);
        DatumReader<AvroHttpRequest> userDatumReader = new SpecificDatumReader<AvroHttpRequest>(AvroHttpRequest.class);
        DataFileReader<AvroHttpRequest> dataFileReader = new DataFileReader<AvroHttpRequest>(file, userDatumReader);
        AvroHttpRequest request = null;
        while (dataFileReader.hasNext()) {
// Reuse user object by passing it to next(). This saves us from
// allocating and garbage collecting many objects for files with
// many items.
            request = dataFileReader.next(request);
            System.out.println(request);
        }

        return request;
    }
    public AvroHttpRequest deSerializeAvroHttpRequestBinary(byte[] data) {
        DatumReader<AvroHttpRequest> employeeReader = new SpecificDatumReader<>(AvroHttpRequest.class);
        Decoder decoder = DecoderFactory.get()
            .binaryDecoder(data, null);
        try {
            return employeeReader.read(null, decoder);
        } catch (IOException e) {
            logger.error("Deserialization error" + e.getMessage());
        }
        return null;
    }
}
